package com.pet.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pet.entity.User;
import com.pet.entity.dto.LoginDTO;
import com.pet.entity.dto.UserDTO;
import com.pet.mapper.UserMapper;
import com.pet.repository.UserRepository;
import com.pet.response.ApiResponse;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // Register a new user
    public ApiResponse<String> registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Username is already taken");
        }
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(registerRequest.getRole());
        userRepository.save(user);

        return new ApiResponse<>("User registered successfully", "Success", 201);
    }

    // Authenticate user and return token
    public ApiResponse<LoginDTO> authenticateUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        UserDTO userDto = UserMapper.toDTO(user);
        if(user==null)
        {
        	throw new UsernameNotFoundException("User not found");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail());

        return new ApiResponse<>("Login successful", new LoginDTO(token,userDto), 200);
      
    }
}