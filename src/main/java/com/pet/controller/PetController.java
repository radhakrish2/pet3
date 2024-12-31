package com.pet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.entity.dto.PetDTO;
import com.pet.response.ApiResponse;
import com.pet.service.PetService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {
	@Autowired
	private PetService petService;

	// Create or update a pet
	// Save a new Pet
	@PostMapping
	public ApiResponse<PetDTO> savePet(@RequestParam("pet") String petJson,
			@RequestParam("images") List<MultipartFile> images) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		PetDTO petDTO = objectMapper.readValue(petJson, PetDTO.class);
		return petService.savePet(petDTO, images);
	}

	// Update Pet by ID
	@PutMapping("/{id}")
	public ApiResponse<PetDTO> updatePet(@PathVariable("id") Long petId, @RequestParam("pet") String petJson,
			@RequestParam(value = "images", required = false) List<MultipartFile> images) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		PetDTO petDTO = objectMapper.readValue(petJson, PetDTO.class);
		return petService.updatePet(petId, petDTO, images);
	}

	// Get pet by ID
	@GetMapping("/{petId}")
	public ApiResponse<PetDTO> getPetById(@PathVariable Long petId) {
		return petService.getPetById(petId);
	}

	// Get all pets
	@GetMapping
	public ApiResponse<List<PetDTO>> getAllPets() {
		return petService.getAllPets();
	}

	// Delete pet by ID
	@DeleteMapping("/{petId}")
	public ApiResponse<String> deletePet(@PathVariable Long petId) {
		return petService.deletePet(petId);
	}
	
	
	
	 @GetMapping("/download/image/{fileName}")
	 public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
		 
		 return petService.getImage(fileName);
	      
	    }
	
	
}
