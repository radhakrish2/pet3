package com.pet.auth;

import com.pet.entity.User.Role;

import lombok.Data;

@Data
public class RegisterRequest {

    private String name;
    private String email;
    private String password; // Needed for authentication
    private String phone;
    private String address;
    private Role role;

    // Getters and Setters
}