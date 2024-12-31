package com.pet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pet.entity.dto.CommentDTO;
import com.pet.response.ApiResponse;
import com.pet.service.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Create a new comment.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CommentDTO>> createComment(@RequestBody CommentDTO commentDTO) {
        ApiResponse<CommentDTO> response = commentService.createComment(commentDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get all comments.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentDTO>>> getAllComments() {
        ApiResponse<List<CommentDTO>> response = commentService.getAllComments();
        return ResponseEntity.ok(response);
    }

    /**
     * Get comments for a specific pet.
     */
    @GetMapping("/by-pet/{petId}")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> getCommentsByPetId(@PathVariable Long petId) {
        ApiResponse<List<CommentDTO>> response = commentService.getCommentsByPetId(petId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a comment by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDTO>> getCommentById(@PathVariable Long id) {
        ApiResponse<CommentDTO> response = commentService.getCommentById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Update a comment by ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDTO>> updateComment(
            @PathVariable Long id,
            @RequestBody CommentDTO commentDTO) {
        ApiResponse<CommentDTO> response = commentService.updateComment(id, commentDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a comment by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable Long id) {
        ApiResponse<String> response = commentService.deleteComment(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get comments made by a specific user.
     */
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> getCommentsByUserId(@PathVariable Long userId) {
        ApiResponse<List<CommentDTO>> response = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(response);
    }
}