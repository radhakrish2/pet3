package com.pet.entity.dto;



import com.pet.entity.Status;

import lombok.Data;

@Data
public class AdoptionRequestDTO {

    private Long id;
    private String requestDate;
    private Status status; // PENDING, APPROVED, REJECTED
    private String remarks;
    private Long petId; // ID of the pet
    private Long userId; // ID of the user who made the request

    // Getters and Setters
}
