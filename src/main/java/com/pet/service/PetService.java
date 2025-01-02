package com.pet.service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pet.entity.Pet;
import com.pet.entity.User;
import com.pet.entity.dto.PetDTO;
import com.pet.exception.ResourceNotFoundException;
import com.pet.mapper.PetMapper;
import com.pet.repository.PetRepository;
import com.pet.repository.UserRepository;
import com.pet.response.ApiResponse;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PetService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${server.tomcat.basedir}")
    private String rootURL;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetMapper petMapper;

    @Autowired
    private UserRepository userRepository;

    // Create a pet
    public ApiResponse<PetDTO> savePet(PetDTO petDTO, List<MultipartFile> images) throws IOException {
        Pet pet = new Pet();
        pet.setName(petDTO.getName());
        pet.setType(petDTO.getType());
        pet.setBreed(petDTO.getBreed());
        pet.setAge(petDTO.getAge());
        pet.setGender(petDTO.getGender());
        pet.setDescription(petDTO.getDescription());
        pet.setStatus(petDTO.getStatus());

        pet = petRepository.save(pet);

        List<String> imagePaths = new ArrayList<>();
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        int i = 0;
        for (MultipartFile image : images) {
            String fileName = "pet" + pet.getId() + "_" + i + getFileExtension(image.getOriginalFilename());
            Path filePath = uploadPath.resolve(fileName);
            image.transferTo(filePath.toFile());
            imagePaths.add("/api/pets/download/image/" + fileName);
            i++;
        }
        pet.setImageUrls(imagePaths);

        if (petDTO.getOwner() != null) {
            User owner = userRepository.findById(petDTO.getOwner().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id " + petDTO.getOwner().getId()));
            pet.setOwner(owner);
        }

        pet = petRepository.save(pet);
        PetDTO savedPetDTO = petMapper.petToPetDTO(pet);
        return new ApiResponse<>("Pet saved successfully", savedPetDTO, HttpStatus.OK.value());
    }

    // Helper to get file extension
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    // Get image by file name
    public ResponseEntity<Resource> getImage(String fileName) {
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(fileName).normalize();

            if (!Files.exists(filePath)) {
                throw new ResourceNotFoundException("File not found: " + fileName);
            }

            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new ResourceNotFoundException("File not found: " + fileName);
            }

            String fileType = Files.probeContentType(filePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileType != null ? fileType : "application/octet-stream"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Other methods (updatePet, getPetById, getAllPets, deletePet) remain unchanged
}
