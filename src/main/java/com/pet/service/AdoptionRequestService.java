package com.pet.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pet.entity.AdoptionRequest;
import com.pet.entity.Pet;
import com.pet.entity.User;
import com.pet.entity.dto.AdoptionRequestDTO;
import com.pet.repository.AdoptionRequestRepository;
import com.pet.repository.PetRepository;
import com.pet.repository.UserRepository;
import com.pet.response.ApiResponse;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AdoptionRequestService {
    @Autowired
    private final AdoptionRequestRepository adoptionRequestRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;


    public AdoptionRequestService(AdoptionRequestRepository adoptionRequestRepository, 
                                  PetRepository petRepository, 
                                  UserRepository userRepository) {
        this.adoptionRequestRepository = adoptionRequestRepository;
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new adoption request.
     */
    public ApiResponse<AdoptionRequestDTO> createAdoptionRequest(AdoptionRequestDTO dto) {
        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + dto.getPetId()));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + dto.getUserId()));

        AdoptionRequest adoptionRequest = new AdoptionRequest();
        adoptionRequest.setRequestDate(LocalDate.parse(dto.getRequestDate()));
        adoptionRequest.setStatus(dto.getStatus());
        adoptionRequest.setRemarks(dto.getRemarks());
        adoptionRequest.setPet(pet);
        adoptionRequest.setUser(user);

        AdoptionRequest savedRequest = adoptionRequestRepository.save(adoptionRequest);
        return new ApiResponse<>("Adoption request created successfully", mapToDTO(savedRequest), 201);
    }

    /**
     * Get all adoption requests.
     */
    public ApiResponse<List<AdoptionRequestDTO>> getAllAdoptionRequests() {
        List<AdoptionRequest> adoptionRequests = adoptionRequestRepository.findAll();
        List<AdoptionRequestDTO> dtoList = adoptionRequests.stream().map(this::mapToDTO).collect(Collectors.toList());
        return new ApiResponse<>("Adoption requests fetched successfully", dtoList, 200);
    }

    /**
     * Get adoption request by ID.
     */
    public ApiResponse<AdoptionRequestDTO> getAdoptionRequestById(Long id) {
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adoption request not found with ID: " + id));
        return new ApiResponse<>("Adoption request fetched successfully", mapToDTO(adoptionRequest), 200);
    }

    /**
     * Update an adoption request.
     */
    public ApiResponse<AdoptionRequestDTO> updateAdoptionRequest(Long id, AdoptionRequestDTO dto) {
        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adoption request not found with ID: " + id));

        adoptionRequest.setRequestDate(LocalDate.parse(dto.getRequestDate()));
        adoptionRequest.setStatus(dto.getStatus());
        adoptionRequest.setRemarks(dto.getRemarks());

        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + dto.getPetId()));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + dto.getUserId()));

        adoptionRequest.setPet(pet);
        adoptionRequest.setUser(user);

        AdoptionRequest updatedRequest = adoptionRequestRepository.save(adoptionRequest);
        return new ApiResponse<>("Adoption request updated successfully", mapToDTO(updatedRequest), 200);
    }

    /**
     * Delete an adoption request by ID.
     */
    public ApiResponse<String> deleteAdoptionRequest(Long id) {
        if (!adoptionRequestRepository.existsById(id)) {
            throw new EntityNotFoundException("Adoption request not found with ID: " + id);
        }
        adoptionRequestRepository.deleteById(id);
        return new ApiResponse<>("Adoption request deleted successfully", null, 200);
    }

 
    
    /**
     * Get all adoption requests for pets owned by a specific owner.
     */
    public ApiResponse<List<AdoptionRequestDTO>> getRequestsForOwner(Long ownerId) {
        List<Pet> pets = petRepository.findByOwnerId(ownerId);

        if (pets.isEmpty()) {
            throw new EntityNotFoundException("No pets found for owner ID: " + ownerId);
        }

        List<Long> petIds = pets.stream().map(Pet::getId).collect(Collectors.toList());
        List<AdoptionRequest> adoptionRequests = adoptionRequestRepository.findByPetIdIn(petIds);

        List<AdoptionRequestDTO> dtoList = adoptionRequests.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new ApiResponse<>("Adoption requests fetched successfully for owner's pets", dtoList, 200);
    }

    
    
    
    /**
     * Helper method to map entity to DTO.
     */
    private AdoptionRequestDTO mapToDTO(AdoptionRequest adoptionRequest) {
        AdoptionRequestDTO dto = new AdoptionRequestDTO();
        dto.setId(adoptionRequest.getId());
        dto.setRequestDate(adoptionRequest.getRequestDate().toString());
        dto.setStatus(adoptionRequest.getStatus());
        dto.setRemarks(adoptionRequest.getRemarks());
        dto.setPetId(adoptionRequest.getPet().getId());
        dto.setUserId(adoptionRequest.getUser().getId());
        return dto;
    }
    

    
    
    
}
