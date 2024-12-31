package com.pet.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pet.entity.Comment;
import com.pet.entity.Pet;
import com.pet.entity.User;
import com.pet.entity.dto.CommentDTO;
import com.pet.repository.CommentRepository;
import com.pet.repository.PetRepository;
import com.pet.repository.UserRepository;
import com.pet.response.ApiResponse;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CommentService {
	 @Autowired
    private final CommentRepository commentRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

   
    public CommentService(CommentRepository commentRepository, 
                          PetRepository petRepository, 
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new comment.
     */
    public ApiResponse<CommentDTO> createComment(CommentDTO dto) {
        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + dto.getPetId()));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + dto.getUserId()));

        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setCreatedDate(LocalDateTime.now());
        comment.setPet(pet);
        comment.setUser(user);

        Comment savedComment = commentRepository.save(comment);

        return new ApiResponse<>("Comment created successfully", mapToDTO(savedComment), 201);
    }

    /**
     * Get all comments.
     */
    public ApiResponse<List<CommentDTO>> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        List<CommentDTO> dtoList = comments.stream().map(this::mapToDTO).collect(Collectors.toList());
        return new ApiResponse<>("Comments fetched successfully", dtoList, 200);
    }

    /**
     * Get a comment by ID.
     */
    public ApiResponse<CommentDTO> getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with ID: " + id));
        return new ApiResponse<>("Comment fetched successfully", mapToDTO(comment), 200);
    }

    /**
     * Update a comment.
     */
    public ApiResponse<CommentDTO> updateComment(Long id, CommentDTO dto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with ID: " + id));

        comment.setContent(dto.getContent());

        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + dto.getPetId()));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + dto.getUserId()));

        comment.setPet(pet);
        comment.setUser(user);

        Comment updatedComment = commentRepository.save(comment);

        return new ApiResponse<>("Comment updated successfully", mapToDTO(updatedComment), 200);
    }

    /**
     * Delete a comment by ID.
     */
    public ApiResponse<String> deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new EntityNotFoundException("Comment not found with ID: " + id);
        }
        commentRepository.deleteById(id);
        return new ApiResponse<>("Comment deleted successfully", null, 200);
    }
    
    
    
    /**
     * Get all comments for a specific pet by pet ID.
     */
    public ApiResponse<List<CommentDTO>> getCommentsByPetId(Long petId) {
        List<Comment> comments = commentRepository.findByPetId(petId);

        if (comments.isEmpty()) {
            throw new EntityNotFoundException("No comments found for pet ID: " + petId);
        }

        List<CommentDTO> commentDTOs = comments.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new ApiResponse<>("Comments fetched successfully for pet ID: " + petId, commentDTOs, 200);
    }
    
    
    
    /**
     * Get all comments by user ID.
     */
    public ApiResponse<List<CommentDTO>> getCommentsByUserId(Long userId) {
        List<Comment> comments = commentRepository.findByUserId(userId);

        if (comments.isEmpty()) {
            throw new EntityNotFoundException("No comments found for user ID: " + userId);
        }

        List<CommentDTO> commentDTOs = comments.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new ApiResponse<>("Comments fetched successfully for user ID: " + userId, commentDTOs, 200);
    }
    

    /**
     * Helper method to map Comment entity to DTO.
     */
    private CommentDTO mapToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedDate(comment.getCreatedDate().toString());
        dto.setPetId(comment.getPet().getId());
        dto.setUserId(comment.getUser().getId());
      
        dto.setUname(comment.getUser().getName());
        dto.setEmail(comment.getUser().getEmail());
        return dto;
    }
}
