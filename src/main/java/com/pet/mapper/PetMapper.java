package com.pet.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.pet.entity.*;
import com.pet.entity.dto.*;

@Component
public class PetMapper {

	// Convert Pet Entity to PetDTO
	public static PetDTO petToPetDTO(Pet pet) {
		if (pet == null) {
			return null;
		}

		PetDTO petDTO = new PetDTO();
		petDTO.setId(pet.getId());
		petDTO.setName(pet.getName());
		petDTO.setType(pet.getType());
		petDTO.setBreed(pet.getBreed());
		petDTO.setAge(pet.getAge());
		petDTO.setGender(pet.getGender());
		petDTO.setDescription(pet.getDescription());
		petDTO.setStatus(pet.getStatus());
		petDTO.setImageUrls(pet.getImageUrls());
		
		// Map the ownerId (we store only the owner ID in the DTO)
		if (pet.getOwner() != null) {
			User u = pet.getOwner();
			UserDTO userDto = UserDTO.builder().name(u.getName())
					.id(u.getId())
					.role(u.getRole())
					.email(u.getEmail())
					.phone(u.getPhone())
					.address(u.getAddress())
					.build();	
			petDTO.setOwner(userDto);
			}

		return petDTO;
	}

	// Convert PetDTO to Pet Entity
	public static Pet petDTOToPet(PetDTO petDTO) {
		if (petDTO == null) {
			return null;
		}
		Pet pet = new Pet();
		// Manually set fields from PetDTO to Pet entity
		pet.setName(petDTO.getName());
		pet.setType(petDTO.getType());
		pet.setBreed(petDTO.getBreed());
		pet.setAge(petDTO.getAge());
		pet.setGender(petDTO.getGender());
		pet.setDescription(petDTO.getDescription());
		pet.setStatus(petDTO.getStatus());
		pet.setImageUrls(petDTO.getImageUrls());
		return pet;
	}

	// Convert List of Pet Entities to List of PetDTOs
	public static List<PetDTO> toDTOList(List<Pet> pets) {
		return pets.stream().map(PetMapper::petToPetDTO).collect(Collectors.toList());
	}

	// Convert List of PetDTOs to List of Pet Entities
	public static List<Pet> toEntityList(List<PetDTO> petDTOs) {
		return petDTOs.stream().map(PetMapper::petDTOToPet).collect(Collectors.toList());
	}
}
