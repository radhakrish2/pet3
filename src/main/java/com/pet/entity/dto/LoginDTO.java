package com.pet.entity.dto;

import com.pet.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDTO {
	
	private String token;
	private UserDTO user;

}
