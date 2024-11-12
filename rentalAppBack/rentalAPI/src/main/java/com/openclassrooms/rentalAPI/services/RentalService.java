package com.openclassrooms.rentalAPI.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.rentalAPI.models.RentalRequest;
import com.openclassrooms.rentalAPI.models.RentalResponse;
import com.openclassrooms.rentalAPI.models.Rentals;
import com.openclassrooms.rentalAPI.models.Users;
import com.openclassrooms.rentalAPI.repository.RentalRepository;
import com.openclassrooms.rentalAPI.repository.UserRepository;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    // Method to create a rental
    public ResponseEntity<Map<String, String>> createRental(RentalRequest rentalRequest, Authentication authentication) throws IOException {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
        }

        // Retrieve the current user
        String email = authentication.getName();
        Users owner = userRepository.findByEmail(email).orElse(null);
        if (owner == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not found"));
        }

        String imagePath = saveImage(rentalRequest.getPicture());

        Rentals rental = new Rentals();
        rental.setName(rentalRequest.getName());
        rental.setSurface(rentalRequest.getSurface());
        rental.setPrice(rentalRequest.getPrice());
        rental.setPicture(imagePath);
        rental.setDescription(rentalRequest.getDescription());
        rental.setOwner(owner);

        rentalRepository.save(rental);

        return ResponseEntity.ok(Map.of("message", "Rental created!"));
    }

    // Method to save an image and return the path
    private String saveImage(MultipartFile picture) throws IOException {
        if (picture.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }

        String imageFileName = picture.getOriginalFilename();
        String imagesDirectory = new File("uploads").getAbsolutePath();
        File imageFile = new File(imagesDirectory, imageFileName);

        if (!imageFile.getParentFile().exists()) {
            imageFile.getParentFile().mkdirs();
        }

        picture.transferTo(imageFile);
        return imageFileName;
    }

    // Method to retrieve all rentals
    public ResponseEntity<Object> getAllRentals(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Rentals> rentals = rentalRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        List<RentalResponse> rentalResponses = rentals.stream().map(rental -> {
            String createdAt = rental.getCreatedAt().format(formatter);
            String updatedAt = rental.getUpdatedAt().format(formatter);

            return new RentalResponse(
                    rental.getId(),
                    rental.getName(),
                    rental.getSurface(),
                    rental.getPrice(),
                    "http://localhost:8080/images/" + rental.getPicture(),
                    rental.getDescription(),
                    rental.getOwner().getId(),
                    createdAt,
                    updatedAt
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("rentals", rentalResponses));
    }

    // Method to retrieve a rental by its ID
    public ResponseEntity<RentalResponse> getRentalById(Integer id, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return rentalRepository.findById(id)
                .map(rental -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    String createdAt = rental.getCreatedAt().format(formatter);
                    String updatedAt = rental.getUpdatedAt().format(formatter);

                    RentalResponse response = new RentalResponse(
                            rental.getId(),
                            rental.getName(),
                            rental.getSurface(),
                            rental.getPrice(),
                            "http://localhost:8080/images/" + rental.getPicture(),
                            rental.getDescription(),
                            rental.getOwner().getId(),
                            createdAt,
                            updatedAt
                    );
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Method to update an existing rental
    public ResponseEntity<Map<String, String>> updateRental(Integer id, RentalRequest rentalRequest, Authentication authentication) throws Exception {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
        }

        Optional<Users> ownerOpt = userRepository.findByEmail(authentication.getName());
        if (ownerOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not found"));
        }

        Users owner = ownerOpt.get();

        Optional<Rentals> rentalOpt = rentalRepository.findById(id);
        if (rentalOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Rental not found"));
        }

        Rentals rental = rentalOpt.get();

        if (!rental.getOwner().getId().equals(owner.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "You are not the owner of this rental"));
        }

        rental.setName(rentalRequest.getName());
        rental.setSurface(rentalRequest.getSurface());
        rental.setPrice(rentalRequest.getPrice());
        rental.setDescription(rentalRequest.getDescription());

        MultipartFile picture = rentalRequest.getPicture();
        if (picture != null && !picture.isEmpty()) {
            String imagePath = saveImage(picture);
            rental.setPicture(imagePath);
        }

        rentalRepository.save(rental);
        return ResponseEntity.ok(Map.of("message", "Rental updated!"));
    }
}
