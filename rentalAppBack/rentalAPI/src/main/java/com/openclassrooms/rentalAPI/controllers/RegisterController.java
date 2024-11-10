package com.openclassrooms.rentalAPI.controllers;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.openclassrooms.rentalAPI.models.RegisterRequest;
import com.openclassrooms.rentalAPI.models.Users;
import com.openclassrooms.rentalAPI.repository.UserRepository;
import com.openclassrooms.rentalAPI.services.JWTService;

@RestController
public class RegisterController {
	
	 	@Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private JWTService jwtService;
	    
	    @Autowired
	    private BCryptPasswordEncoder passwordEncoder;
	    
	    
	    
	    
	    @PostMapping("/api/auth/register")
	    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterRequest request) {
	    	 // Check if any required field is missing
	        if (request.getName() == null || request.getEmail() == null || request.getPassword() == null) {
	            return ResponseEntity.badRequest().body(Map.of("message","All fields are required"));
	        }

	        // Check if the email already exists in the database
	        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
	            return ResponseEntity.badRequest().body(Map.of("message","Email already in use"));
	        }

	        
	        // Create a new user 
	        Users newUser = new Users();
	        newUser.setName(request.getName());
	        newUser.setEmail(request.getEmail());
	        newUser.setPassword(passwordEncoder.encode(request.getPassword()));  // Encrypt password
	        
	        // Save the new user in the database
	        userRepository.save(newUser);

	        // Generate a JWT token for the new user
	        //Authentication authentication = new UsernamePasswordAuthenticationToken(newUser.getEmail(), null, List.of());
	        String token = jwtService.generateTokenFromUser(newUser);
	        
	        Map<String, String> response = new HashMap<>();
	        response.put("token", token);

	        return ResponseEntity.ok(response);
	    }
}
