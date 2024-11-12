package com.openclassrooms.rentalAPI.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.openclassrooms.rentalAPI.models.Users;
import com.openclassrooms.rentalAPI.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Retrieve a user by their ID
    public ResponseEntity<Users> getUserById(Integer id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user)) // Return the user if found
                .orElse(ResponseEntity.notFound().build()); // Return 404 if user not found
    }

    // Retrieve the currently authenticated user’s information
    public ResponseEntity<Users> getCurrentUser(Authentication authentication) {
        String userEmail = authentication.getName(); // Extract the authenticated user's email
        return userRepository.findByEmail(userEmail)
                .map(user -> ResponseEntity.ok(user)) // Return the user if found
                .orElse(ResponseEntity.notFound().build()); // Return 404 if user not found
    }
}
