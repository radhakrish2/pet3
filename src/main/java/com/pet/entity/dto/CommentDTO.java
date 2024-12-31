package com.pet.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private Long id;
    private String content;
    private String createdDate;
    private Long petId;
    private Long userId;
    
	private String uname;
	private String email;

    // Getters and Setters
}