package com.pet.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pet.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	// Find comments by pet ID
	List<Comment> findByPetId(Long petId);

	// Find comments by user ID
	List<Comment> findByUserId(Long userId);
	
	
}
