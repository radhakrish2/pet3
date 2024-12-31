package com.pet.entity.dto;
import java.util.List;

import com.pet.entity.Status;

import lombok.Data;
	
	@Data
	public class PetDTO {
	
	    private Long id;
	    private String name;
	    private String type; // e.g., Dog, Cat
	    private String breed;
	    private int age;
	    private String gender;
	    private String description;
	    private Status status; // AVAILABLE or ADOPTED
	    private List<String> imageUrls;
	    private UserDTO owner;
	    private List<CommentDTO> comments;
	
	    // Getters and Setters
	}
	
