package com.openclassrooms.rentalAPI.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.rentalAPI.models.RentalRequest;
import com.openclassrooms.rentalAPI.models.RentalResponse;
import com.openclassrooms.rentalAPI.services.RentalService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.multipart.MultipartFile;

@RestController
public class RentalController {
	
    @Autowired
    private RentalService rentalService;

    // Route to create a rental
    @Operation(summary = "Create a rental", description = "This route allows you to create a new rental offer. The parameters are: name, surface area, price, picture, description")
    @PostMapping("/api/rentals")
    public ResponseEntity<Map<String, String>> createRental(@ModelAttribute RentalRequest rentalRequest, Authentication authentication) throws IOException {
        return rentalService.createRental(rentalRequest, authentication);
    }

    // Route to retrieve all rentals
    @Operation(summary = "Retrieve all rentals", description = "This route allows you to retrieve all existing rentals")
    @GetMapping("/api/rentals")
    public ResponseEntity<Object> getAllRentals(Authentication authentication) {
        return rentalService.getAllRentals(authentication);
    }

    // Route to retrieve a rental by its ID
    @Operation(summary = "Retrieve a specific rental", description = "This route allows you to retrieve a specific rental using its ID")
    @GetMapping("/api/rentals/{id}")
    public ResponseEntity<RentalResponse> getRentalById(@PathVariable Integer id, Authentication authentication) {
        return rentalService.getRentalById(id, authentication);
    }

    // Route to update an existing rental
    @Operation(summary = "Update a specific rental", description = "This route allows you to update a specific rental using its ID")
    @PutMapping("/api/rentals/{id}")
    public ResponseEntity<Map<String, String>> updateRental(@PathVariable Integer id, @ModelAttribute RentalRequest rentalRequest, Authentication authentication) throws Exception {
        return rentalService.updateRental(id, rentalRequest, authentication);
    }
}
