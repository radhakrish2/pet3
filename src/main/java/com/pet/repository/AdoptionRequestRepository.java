package com.pet.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pet.entity.AdoptionRequest;

@Repository
public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest, Long> {
	// Find requests by status
	List<AdoptionRequest> findByStatus(String status);

	// Find requests by user ID
	List<AdoptionRequest> findByUserId(Long userId);

	// Find requests by pet ID
	List<AdoptionRequest> findByPetId(Long petId);
	
	 List<AdoptionRequest> findByPetIdIn(List<Long> petIds);
}
