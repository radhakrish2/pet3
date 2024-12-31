package com.pet.entity.dto;

import com.pet.entity.User.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public class UserDTO {
	
	    private Long id;
	    private String name;
	    private String email;
	    private Role role; // OWNER, VOLUNTEER, ADMIN
	    private String phone;
	    private String address;
	
	    // Getters and Setters
	}