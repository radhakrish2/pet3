package com.pet.service;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PetService {

    @Value("${ftp.server.host}")
    private String ftpHost;

    @Value("${ftp.server.port}")
    private int ftpPort;

    @Value("${ftp.server.username}")
    private String ftpUsername;

    @Value("${ftp.server.password}")
    private String ftpPassword;

    @Value("${ftp.server.upload-dir}")
    private String ftpUploadDir;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetMapper petMapper;

    @Autowired
    private UserRepository userRepository;

    // Save a new pet
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

        // Handle image upload to FTP
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            String fileName = "pet" + pet.getId() + "_" + image.getOriginalFilename();
            uploadFileToFTP(image, fileName);
            imageUrls.add("/api/pets/download/image/" + fileName);
        }
        pet.setImageUrls(imageUrls);

        if (petDTO.getOwner() != null) {
            User owner = userRepository.findById(petDTO.getOwner().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id " + petDTO.getOwner().getId()));
            pet.setOwner(owner);
        }

        pet = petRepository.save(pet);

        PetDTO savedPetDTO = petMapper.petToPetDTO(pet);
        return new ApiResponse<>("Pet saved successfully", savedPetDTO, HttpStatus.OK.value());
    }

    // Update Pet by ID
    public ApiResponse<PetDTO> updatePet(Long petId, PetDTO petDTO, List<MultipartFile> images) throws IOException {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with id " + petId));

        pet.setName(petDTO.getName());
        pet.setType(petDTO.getType());
        pet.setBreed(petDTO.getBreed());
        pet.setAge(petDTO.getAge());
        pet.setGender(petDTO.getGender());
        pet.setDescription(petDTO.getDescription());
        pet.setStatus(petDTO.getStatus());

        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                String fileName = "pet" + petId + "_" + image.getOriginalFilename();
                uploadFileToFTP(image, fileName);
                imageUrls.add("/api/pets/download/image/" + fileName);
            }
            pet.setImageUrls(imageUrls);
        }

        if (petDTO.getOwner() != null) {
            User owner = userRepository.findById(petDTO.getOwner().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id " + petDTO.getOwner().getId()));
            pet.setOwner(owner);
        }

        pet = petRepository.save(pet);

        PetDTO updatedPetDTO = petMapper.petToPetDTO(pet);
        return new ApiResponse<>("Pet updated successfully", updatedPetDTO, HttpStatus.OK.value());
    }

    // Get pet by ID
    public ApiResponse<PetDTO> getPetById(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with id " + petId));
        PetDTO petDTO = petMapper.petToPetDTO(pet);
        return new ApiResponse<>("Pet retrieved successfully", petDTO, HttpStatus.OK.value());
    }

    // Get all pets
    public ApiResponse<List<PetDTO>> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        List<PetDTO> petDTOs = pets.stream().map(PetMapper::petToPetDTO).collect(Collectors.toList());
        return new ApiResponse<>("Pets retrieved successfully", petDTOs, HttpStatus.OK.value());
    }

    // Delete pet by ID
    public ApiResponse<String> deletePet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with id " + petId));
        petRepository.delete(pet);
        return new ApiResponse<>("Pet deleted successfully", "Success", HttpStatus.NO_CONTENT.value());
    }

    // FTP file upload method
    private void uploadFileToFTP(MultipartFile file, String fileName) throws IOException {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpHost, ftpPort);
            ftpClient.login(ftpUsername, ftpPassword);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.changeWorkingDirectory(ftpUploadDir);

            try (InputStream inputStream = file.getInputStream()) {
                ftpClient.storeFile(fileName, inputStream);
            }
        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
    }

    // Get image by filename from FTP
    public ResponseEntity<Resource> getImage(String fileName) {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(ftpHost, ftpPort);
            ftpClient.login(ftpUsername, ftpPassword);

            File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
            try (InputStream inputStream = ftpClient.retrieveFileStream(ftpUploadDir + "/" + fileName)) {
                if (inputStream != null) {
                    Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Resource resource = new UrlResource(tempFile.toURI());
                    return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/jpeg"))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                            .body(resource);
                }
            } finally {
                ftpClient.logout();
                ftpClient.disconnect();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}