package com.pet.service;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pet.entity.Pet;
import com.pet.entity.dto.PetDTO;
import com.pet.mapper.PetMapper;
import com.pet.repository.PetRepository;
import com.pet.response.ApiResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {

    @Value("${ftp.server.host}")
    private String ftpServerHost;

    @Value("${ftp.server.port}")
    private int ftpServerPort;

    @Value("${ftp.server.username}")
    private String ftpServerUsername;

    @Value("${ftp.server.password}")
    private String ftpServerPassword;

    @Value("${ftp.server.directory}")
    private String ftpServerDirectory;
    
    @Autowired
    private PetRepository petRepository;
    
    @Autowired
    private PetMapper petMapper;

    private FTPClient ftpClient;

    public PetService() {
        this.ftpClient = new FTPClient();
    }

    public ApiResponse<PetDTO> savePet(PetDTO petDTO, List<MultipartFile> images) throws IOException {
        // Create Pet entity
        Pet pet = new Pet();
        pet.setName(petDTO.getName());
        pet.setType(petDTO.getType());
        pet.setBreed(petDTO.getBreed());
        pet.setAge(petDTO.getAge());
        pet.setGender(petDTO.getGender());
        pet.setDescription(petDTO.getDescription());
        pet.setStatus(petDTO.getStatus());

        // Save pet to database
        pet = petRepository.save(pet);

        // Handle file upload to FTP server
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            String fileName = "pet" + pet.getId() + "_" + image.getOriginalFilename();
            boolean uploadSuccess = uploadFileToFtpServer(image, fileName);
            if (uploadSuccess) {
                // Save the FTP URL path
                String ftpFileUrl = "ftp://" + ftpServerHost + ftpServerDirectory + "/" + fileName;
                imageUrls.add(ftpFileUrl); // Store the FTP URL
            }
        }

        // Update pet with image URLs
        pet.setImageUrls(imageUrls);
        pet = petRepository.save(pet);

        PetDTO savedPetDTO = petMapper.petToPetDTO(pet);
        return new ApiResponse<>("Pet saved successfully", savedPetDTO, HttpStatus.OK.value());
    }

    private boolean uploadFileToFtpServer(MultipartFile file, String fileName) throws IOException {
        boolean success = false;
        try {
            // Connect to the FTP server
            ftpClient.connect(ftpServerHost, ftpServerPort);
            ftpClient.login(ftpServerUsername, ftpServerPassword);

            // Set FTP connection to binary transfer mode
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // Change to the appropriate directory
            ftpClient.changeWorkingDirectory(ftpServerDirectory);

            // Upload the file
            try (InputStream inputStream = file.getInputStream()) {
                success = ftpClient.storeFile(fileName, inputStream);
            }

            // Logout and disconnect
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Failed to upload file to FTP server", e);
        } finally {
            // Ensure we disconnect properly
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
        }
        return success;
    }
}
