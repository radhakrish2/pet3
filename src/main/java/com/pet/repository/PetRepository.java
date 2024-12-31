package com.pet.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pet.entity.Pet;
import com.pet.entity.Status;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
	// Find pets by type
	List<Pet> findByType(String type);

	// Find pets by status
	List<Pet> findByStatus(Status status);
	
	 List<Pet> findByOwnerId(Long ownerId);
}
