package com.openclassrooms.rentalAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.rentalAPI.models.RegisterRequest;
import com.openclassrooms.rentalAPI.services.RegisterService;

import io.swagger.v3.oas.annotations.Operation;

import java.util.Map;

@RestController
public class RegisterController {

    @Autowired
    private RegisterService registerService;
    
    
    @Operation(summary = "Register page", description = "This route allows a new user to register")
    @PostMapping("/api/auth/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterRequest request) {
        Map<String, String> response = registerService.registerUser(request);

        // Check if there was an error in the registration process
        if (response.containsKey("message")) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }
}
