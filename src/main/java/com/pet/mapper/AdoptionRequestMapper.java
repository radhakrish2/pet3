package com.pet.mapper;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.pet.entity.AdoptionRequest;
import com.pet.entity.dto.AdoptionRequestDTO;

@Component
public class AdoptionRequestMapper {

    // Convert AdoptionRequest Entity to AdoptionRequestDTO
    public static AdoptionRequestDTO toDTO(AdoptionRequest adoptionRequest) {
        if (adoptionRequest == null) {
            return null;
        }
        AdoptionRequestDTO dto = new AdoptionRequestDTO();
        dto.setId(adoptionRequest.getId());
        dto.setPetId(adoptionRequest.getPet().getId()); // Assuming Pet is an entity and has an ID
        dto.setUserId(adoptionRequest.getUser().getId()); // Assuming User is an entity and has an ID
        dto.setStatus(adoptionRequest.getStatus());
    
        return dto;
    }

    // Convert AdoptionRequestDTO to AdoptionRequest Entity
    public static AdoptionRequest toEntity(AdoptionRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        AdoptionRequest adoptionRequest = new AdoptionRequest();
        // Assuming that the Pet and User entities have been fetched or set from elsewhere
        adoptionRequest.setId(dto.getId());
        // Set Pet and User objects manually, assuming you have a way to retrieve them by ID
        // adoptionRequest.setPet(fetchPetById(dto.getPetId())); 
        // adoptionRequest.setUser(fetchUserById(dto.getUserId()));
        adoptionRequest.setStatus(dto.getStatus());
      
     
        return adoptionRequest;
    }

    // Convert List of AdoptionRequest Entities to List of AdoptionRequestDTOs
    public static List<AdoptionRequestDTO> toDTOList(List<AdoptionRequest> adoptionRequests) {
        return adoptionRequests.stream()
                .map(AdoptionRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Convert List of AdoptionRequestDTOs to List of AdoptionRequest Entities
    public static List<AdoptionRequest> toEntityList(List<AdoptionRequestDTO> dtos) {
        return dtos.stream()
                .map(AdoptionRequestMapper::toEntity)
                .collect(Collectors.toList());
    }
}

