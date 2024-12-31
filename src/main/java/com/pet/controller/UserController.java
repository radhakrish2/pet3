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
import com.pet.entity.User;
import com.pet.entity.dto.UserDTO;
import com.pet.response.ApiResponse;
import com.pet.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserService userService;

	// Create a new User
	@PostMapping
	public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserDTO userDTO) {
		UserDTO createdUser = userService.createUser(userDTO);
		ApiResponse<UserDTO> response = new ApiResponse<>("User created successfully", createdUser,
				HttpStatus.CREATED.value());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Get User by ID
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long userId) {
		UserDTO user = userService.getUserById(userId);
		ApiResponse<UserDTO> response = new ApiResponse<>("User found", user, HttpStatus.OK.value());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Get all Users
	@GetMapping
	public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
		List<UserDTO> users = userService.getAllUsers();
		ApiResponse<List<UserDTO>> response = new ApiResponse<>("Users retrieved successfully", users,
				HttpStatus.OK.value());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Update User by ID
	@PutMapping("/{userId}")
	public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
		UserDTO updatedUser = userService.updateUser(userId, userDTO);
		ApiResponse<UserDTO> response = new ApiResponse<>("User updated successfully", updatedUser,
				HttpStatus.OK.value());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Delete User by ID
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
		ApiResponse<String> response = new ApiResponse<>("User deleted successfully", "Success",
				HttpStatus.NO_CONTENT.value());
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
	}

	// Get Users by Role
	@GetMapping("/role/{role}")
	public ResponseEntity<ApiResponse<List<UserDTO>>> getUsersByRole(@PathVariable String role) {
		User.Role userRole = User.Role.valueOf(role.toUpperCase());
		List<UserDTO> users = userService.getUsersByRole(userRole);
		ApiResponse<List<UserDTO>> response = new ApiResponse<>("Users by role retrieved successfully", users,
				HttpStatus.OK.value());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
