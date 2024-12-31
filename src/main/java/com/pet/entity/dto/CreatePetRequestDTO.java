package com.pet.entity.dto;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class CreatePetRequestDTO {

    private String name;
    private String type; // e.g., Dog, Cat
    private String breed;
    private int age;
    private String gender;
    private String description;
    private String status; // AVAILABLE or ADOPTED
    private List<MultipartFile> images;

    // Getters and Setters
}