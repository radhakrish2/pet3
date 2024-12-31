package com.pet.auth;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pet.response.ApiResponse;
import com.pet.auth.RegisterRequest;
import com.pet.entity.dto.LoginDTO;
import com.pet.auth.LoginRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody RegisterRequest registerRequest) {
        ApiResponse<String> response = authService.registerUser(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Authenticate user and return token
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginDTO>> authenticateUser(@RequestBody LoginRequest loginRequest) {
        ApiResponse<LoginDTO> response = authService.authenticateUser(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/get")
    public String getMethodName() {
        return "Welcome";
    }
    
}
