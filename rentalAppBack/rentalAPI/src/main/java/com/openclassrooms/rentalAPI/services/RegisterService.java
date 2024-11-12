package com.openclassrooms.rentalAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.rentalAPI.models.RegisterRequest;
import com.openclassrooms.rentalAPI.models.Users;
import com.openclassrooms.rentalAPI.repository.UserRepository;
import com.openclassrooms.rentalAPI.services.JWTService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class RegisterService {
	
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    public Map<String, String> registerUser(RegisterRequest request) {
        // Check if any required field is missing
        if (request.getName() == null || request.getEmail() == null || request.getPassword() == null) {
            return Map.of("message", "All fields are required");
        }

        // Check if the email already exists in the database
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return Map.of("message", "Email already in use");
        }

        // Create a new user 
        Users newUser = new Users();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));  // Encrypt password
        
        // Save the new user in the database
        userRepository.save(newUser);

        // Generate a JWT token for the new user
        String token = jwtService.generateTokenFromUser(newUser);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }

}
