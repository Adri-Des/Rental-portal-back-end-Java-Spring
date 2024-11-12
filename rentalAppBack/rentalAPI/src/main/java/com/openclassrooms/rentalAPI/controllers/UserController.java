package com.openclassrooms.rentalAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.rentalAPI.models.Users;
import com.openclassrooms.rentalAPI.services.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Retrieve information of a specific user", description = "This route retrieves a specific user using its ID")
    // Route to retrieve a user by their ID
    @GetMapping("/api/user/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Integer id, Authentication authentication) {
        return userService.getUserById(id);
    }
    
    // Route to retrieve the currently authenticated user’s information
    @Operation(summary = "Retrieve information of the current user ", description = "This route retrieves the information of the currently logged-in user")
    @GetMapping("/api/auth/me")
    public ResponseEntity<Users> getCurrentUser(Authentication authentication) {
        return userService.getCurrentUser(authentication);
    }
}
