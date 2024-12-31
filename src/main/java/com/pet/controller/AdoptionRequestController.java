package com.pet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pet.entity.dto.AdoptionRequestDTO;
import com.pet.response.ApiResponse;
import com.pet.service.AdoptionRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/adoption-requests")
@RequiredArgsConstructor
public class AdoptionRequestController {

	
    private final AdoptionRequestService adoptionRequestService;

     /**
     * Create a new adoption request.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AdoptionRequestDTO>> createAdoptionRequest(
            @RequestBody AdoptionRequestDTO adoptionRequestDTO) {
        ApiResponse<AdoptionRequestDTO> response = adoptionRequestService.createAdoptionRequest(adoptionRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get all adoption requests.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AdoptionRequestDTO>>> getAllAdoptionRequests() {
        ApiResponse<List<AdoptionRequestDTO>> response = adoptionRequestService.getAllAdoptionRequests();
        return ResponseEntity.ok(response);
    }

    /**
     * Get an adoption request by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdoptionRequestDTO>> getAdoptionRequestById(@PathVariable Long id) {
        ApiResponse<AdoptionRequestDTO> response = adoptionRequestService.getAdoptionRequestById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an adoption request.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AdoptionRequestDTO>> updateAdoptionRequest(
            @PathVariable Long id,
            @RequestBody AdoptionRequestDTO adoptionRequestDTO) {
        ApiResponse<AdoptionRequestDTO> response = adoptionRequestService.updateAdoptionRequest(id, adoptionRequestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an adoption request by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAdoptionRequest(@PathVariable Long id) {
        ApiResponse<String> response = adoptionRequestService.deleteAdoptionRequest(id);
        return ResponseEntity.ok(response);
    }
    
    
    /**
     * Get all adoption requests for pets owned by a specific owner.
     */
    @GetMapping("/by-owner/{ownerId}")
    public ResponseEntity<ApiResponse<List<AdoptionRequestDTO>>> getRequestsForOwner(@PathVariable Long ownerId) {
        ApiResponse<List<AdoptionRequestDTO>> response = adoptionRequestService.getRequestsForOwner(ownerId);
        return ResponseEntity.ok(response);
    }
}