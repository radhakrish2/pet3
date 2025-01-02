package com.pet.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
	

	
	@Autowired
	private PetRepository petRepository;
	@Autowired
	private PetMapper petMapper;
	@Autowired
	private UserRepository userRepository;

	// Create a pet
	public ApiResponse<PetDTO> savePet(PetDTO petDTO, List<MultipartFile> images) throws IOException {
	
		   // Map PetDTO to Pet entity (you can use a mapper for this, or do it manually)
	    Pet pet = new Pet();
	    pet.setName(petDTO.getName());
	    pet.setType(petDTO.getType());
	    pet.setBreed(petDTO.getBreed());
	    pet.setAge(petDTO.getAge());
	    pet.setGender(petDTO.getGender());
	    pet.setDescription(petDTO.getDescription());
	    pet.setStatus(petDTO.getStatus());
	    
	    // Save the pet entity first to generate an ID
	    pet = petRepository.save(pet);

	    // Handle image upload
	    List<String> imagePaths = new ArrayList<>();
	    
	    Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);
	    
	    
	    int i = 0; // Index for naming the files
	    for (MultipartFile image : images) {
	        // Generate the file name as "pet" + pet.id + "_" + i
	        String fileName = "pet" + pet.getId() + "_" + i + image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
	        Path filePath = uploadPath.resolve(fileName);
            image.transferTo(filePath.toFile());
            imagePaths.add("/api/pets/download/image/" + fileName);
            i++;
	    }
	    pet.setImageUrls(imagePaths);

	    // Handle owner assignment (use the ownerId from PetDTO)
	    if (petDTO.getOwner() != null) {
	        User owner = userRepository.findById(petDTO.getOwner().getId())
	                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id " + petDTO.getOwner().getId()));
	        pet.setOwner(owner);
	    }

	    // Save the pet entity to the database again with the image URLs
	    pet = petRepository.save(pet);

	    // Map the saved Pet entity back to PetDTO to return
	    PetDTO savedPetDTO = petMapper.petToPetDTO(pet);
	    return new ApiResponse<>("Pet saved successfully", savedPetDTO, HttpStatus.OK.value());

	
	}

	// Update existing Pet by ID
	public ApiResponse<PetDTO> updatePet(Long petId, PetDTO petDTO, List<MultipartFile> images) throws IOException {
		// Check if pet exists
		Pet pet = petRepository.findById(petId)
				.orElseThrow(() -> new ResourceNotFoundException("Pet not found with id " + petId));

		// Manually set fields from PetDTO to Pet entity
		pet.setName(petDTO.getName());
		pet.setType(petDTO.getType());
		pet.setBreed(petDTO.getBreed());
		pet.setAge(petDTO.getAge());
		pet.setGender(petDTO.getGender());
		pet.setDescription(petDTO.getDescription());
		pet.setStatus(petDTO.getStatus());

		// Handle image upload (if new images are provided)
		if (images != null && !images.isEmpty()) {
			List<String> imagePaths = new ArrayList<>();
			for (MultipartFile image : images) {
				String filePath = uploadDir + File.separator + image.getOriginalFilename();
				File file = new File(filePath);
				image.transferTo(file);
			//	imagePaths.add(filePath);
				imagePaths.add("dog/"+image.getOriginalFilename());
			}
			pet.setImageUrls(imagePaths);
		}

		// Handle owner assignment (if necessary, you can use petDTO.getOwnerId() to set
		// the owner)
		 if (petDTO.getOwner() != null) {
		        User owner = userRepository.findById(petDTO.getOwner().getId())
		                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id " + petDTO.getOwner().getId()));
		        pet.setOwner(owner);
		}

		// Save the updated pet to the database
		pet = petRepository.save(pet);

		// Convert the updated Pet entity back to PetDTO
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
		petRepository.delete(pet); // Delete the pet from database
		return new ApiResponse<>("Pet deleted successfully", "Success", HttpStatus.NO_CONTENT.value());
	}
	
	
	
	//get get image through filename
	public ResponseEntity<Resource> getImage(String fileName)
	{
		  
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
	
}
