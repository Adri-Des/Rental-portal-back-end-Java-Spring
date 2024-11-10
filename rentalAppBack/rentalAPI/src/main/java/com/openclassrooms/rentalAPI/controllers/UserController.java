package com.openclassrooms.rentalAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.rentalAPI.models.Users;
import com.openclassrooms.rentalAPI.repository.UserRepository;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Route to retrieve a user by their ID
    @GetMapping("/api/user/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Integer id, Authentication authentication) {
    	// If Spring Security validates the JWT, the user is authenticated.
        // Otherwise, Spring Security will automatically return a 401 Unauthorized error.

    	// Retrieve the user from the database using the provided ID
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user))	// Return the user if found
                .orElse(ResponseEntity.notFound().build());	 // Return 404 if user not found
        
        
        }
    
    // Route to retrieve the currently authenticated user’s information
    @GetMapping("/api/auth/me")
    public ResponseEntity<Users> getCurrentUser(Authentication authentication) {
       
    	// Extract the authenticated user's email from the Authentication object
        String userEmail = authentication.getName();
        
        // Find the user by their email
        return userRepository.findByEmail(userEmail)
        		.map(user -> ResponseEntity.ok(user)) // Return the user if found
        		.orElse(ResponseEntity.notFound().build()); // Return 404 if user not found
    }
   
}