package com.openclassrooms.rentalAPI.controllers;



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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.rentalAPI.models.RentalRequest;
import com.openclassrooms.rentalAPI.models.RentalResponse;
import com.openclassrooms.rentalAPI.models.Rentals;
import com.openclassrooms.rentalAPI.models.Users;
import com.openclassrooms.rentalAPI.repository.RentalRepository;
import com.openclassrooms.rentalAPI.repository.UserRepository;


@RestController
public class RentalController {
	
	   @Autowired
	    private RentalRepository rentalRepository;

	    @Autowired
	    private UserRepository userRepository;

	  
	    // Route to create a rental
	    @PostMapping("/api/rentals")
	    public ResponseEntity<Map<String, String>> createRental(@ModelAttribute RentalRequest rentalRequest, Authentication authentication) throws IOException {
	    	 // Check if user is authenticated
	        if (authentication == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Unauthorized"));
	        }

	        // Retrieve the authenticated user
	        String email = authentication.getName();
	        Users owner = userRepository.findByEmail(email).orElse(null);
	        if (owner == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","User not found"));
	        }

	        // Save image to server
	        MultipartFile picture = rentalRequest.getPicture();
	        String imagePath = saveImage(picture);

	        // Create a new rental
	        Rentals rental = new Rentals();
	        rental.setName(rentalRequest.getName());
	        rental.setSurface(rentalRequest.getSurface());
	        rental.setPrice(rentalRequest.getPrice());
	        rental.setPicture(imagePath); // Enregistrer le chemin de l'image
	        rental.setDescription(rentalRequest.getDescription());
	        rental.setOwner(owner); // Passer l'objet owner

	        // Save the rental to the database
	        rentalRepository.save(rental);
	        
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "Rental created !");

	        return ResponseEntity.ok(response);
	    }
	    
	    // Method to save an image and return the path
	    public String saveImage(MultipartFile picture) throws IOException {
	    	// Check if image is empty
	        if (picture.isEmpty()) {
	            throw new IOException("Failed to store empty file.");
	        }

	        // Get the file name
	        String imageFileName = picture.getOriginalFilename();

	        // Retrieve path to save the image in the 'uploads' directory
	        String imagesDirectory = new File("uploads").getAbsolutePath();
	        File imageFile = new File(imagesDirectory, imageFileName);

	        // Create the directory if it doesn’t exist
	        if (!imageFile.getParentFile().exists()) {
	            imageFile.getParentFile().mkdirs();
	        }

	        // Save the image to the directory
	        picture.transferTo(imageFile);

	        // Return the relative path to the image
	        return  imageFileName;
	    }
	    
	    
	    // Route to retrieve all rentals
	    @GetMapping("/api/rentals")
	    public ResponseEntity<Object> getAllRentals(Authentication authentication) {
	        if (authentication == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	        }

	        // Retrieve all rentals from the database
	        List<Rentals> rentals = rentalRepository.findAll();

	        // Format the dates for each rental
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	        List<RentalResponse> rentalResponses = rentals.stream().map(rental -> {
	            String createdAt = rental.getCreatedAt().format(formatter);
	            String updatedAt = rental.getUpdatedAt().format(formatter);
	            
	           
	            return new RentalResponse(
	                    rental.getId(),
	                    rental.getName(),
	                    rental.getSurface(),
	                    rental.getPrice(),
	                    //rental.getPicture(),
	                    "http://localhost:8080/images/" + rental.getPicture(),
	                    rental.getDescription(),
	                    rental.getOwner().getId(), 
	                    createdAt, 
	                    updatedAt  
	                    
	                    
	            );
	            
	        }).collect(Collectors.toList());
	        
	        

	        
	        Map<String, Object> response = new HashMap<>();
	        response.put("rentals", rentalResponses);

	        return ResponseEntity.ok(response);
	        
	        

	    }
	    
	    
	    // Route to retrieve a rental by its ID
	    @GetMapping("/api/rentals/{id}")
	    public ResponseEntity<RentalResponse> getRentalById(@PathVariable Integer id, Authentication authentication) {
	        if (authentication == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	        }

	        // Retrieve the rental by its ID
	        return rentalRepository.findById(id)
	                .map(rental -> {
	                	
	                	// Format dates
	                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	                    String createdAt = rental.getCreatedAt().format(formatter);
	                    String updatedAt = rental.getUpdatedAt().format(formatter);
	                    
	                    RentalResponse response = new RentalResponse(
	                            rental.getId(),
	                            rental.getName(),
	                            rental.getSurface(),
	                            rental.getPrice(),
	                            //rental.getPicture(),
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
	    
	    
	    
	    // Route to update an existing rental
	    @PutMapping("/api/rentals/{id}")
	    public ResponseEntity<Map<String, String>> updateRental(@PathVariable Integer id, @ModelAttribute RentalRequest rentalRequest, Authentication authentication) throws Exception {
	        if (authentication == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Unauthorized"));
	        }

	        // Retrieve the authenticated user (Owner)
	        Optional<Users> Owner = userRepository.findByEmail(authentication.getName());
	        if (Owner.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","User not found"));
	        }

	        Users owner = Owner.get(); // Extraire l'utilisateur authentifié

	        // Retrieve existing rental by ID
	        Optional<Rentals> Rental = rentalRepository.findById(id);
	        if (Rental.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Rental not found"));
	        }

	        Rentals rental = Rental.get();

	        // Check if the authenticated user is the owner
	        if (!rental.getOwner().getId().equals(owner.getId())) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","You are not the owner of this rental"));
	        }

	        // Update rental details
	        rental.setName(rentalRequest.getName());
	        rental.setSurface(rentalRequest.getSurface());
	        rental.setPrice(rentalRequest.getPrice());
	        rental.setDescription(rentalRequest.getDescription());

	        // Save new image if provided and replace old image path
	        MultipartFile picture = rentalRequest.getPicture();
	        if (picture != null && !picture.isEmpty()) {
	            String imagePath = saveImage(picture); // Sauvegarder la nouvelle image
	            rental.setPicture(imagePath); // Mettre à jour le chemin de l'image
	        }

	        // Save changes to the database
	        rentalRepository.save(rental);

	        Map<String, String> response = new HashMap<>();
	        response.put("message", "Rental updated !");

	        return ResponseEntity.ok(response);
	    }
	

}
