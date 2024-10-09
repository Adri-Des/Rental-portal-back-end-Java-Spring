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

	  

	    @PostMapping("/api/rentals")
	    public ResponseEntity<Map<String, String>> createRental(@ModelAttribute RentalRequest rentalRequest, Authentication authentication) throws IOException {
	        // Vérification si l'utilisateur est authentifié
	        if (authentication == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Unauthorized"));
	        }

	        // Récupérer l'utilisateur connecté
	        String email = authentication.getName();
	        Users owner = userRepository.findByEmail(email).orElse(null);
	        if (owner == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","User not found"));
	        }

	        // Enregistrer l'image sur le serveur
	        MultipartFile picture = rentalRequest.getPicture();
	        String imagePath = saveImage(picture);

	        // Créer une nouvelle offre de location
	        Rentals rental = new Rentals();
	        rental.setName(rentalRequest.getName());
	        rental.setSurface(rentalRequest.getSurface());
	        rental.setPrice(rentalRequest.getPrice());
	        rental.setPicture(imagePath); // Enregistrer le chemin de l'image
	        rental.setDescription(rentalRequest.getDescription());
	        rental.setOwner(owner); // Passer l'objet owner

	        // Sauvegarder l'offre de location
	        rentalRepository.save(rental);
	        
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "Rental created !");

	        return ResponseEntity.ok(response);
	    }
	    
	 // Méthode pour enregistrer l'image et retourner le chemin
	    public String saveImage(MultipartFile picture) throws IOException {
	        // Vérifier si l'image est vide
	        if (picture.isEmpty()) {
	            throw new IOException("Failed to store empty file.");
	        }

	        // Obtenir le nom du fichier
	        String imageFileName = picture.getOriginalFilename();

	        // Chemin absolu pour sauvegarder l'image dans src/main/resources/images
	        String imagesDirectory = new File("uploads").getAbsolutePath();
	        File imageFile = new File(imagesDirectory, imageFileName);

	        // Créer le dossier s'il n'existe pas
	        if (!imageFile.getParentFile().exists()) {
	            imageFile.getParentFile().mkdirs();
	        }

	        // Sauvegarder l'image dans le dossier
	        picture.transferTo(imageFile);

	        // Retourner le chemin relatif de l'image
	        return  imageFileName;
	    }
	    
	    
	    
	    @GetMapping("/api/rentals")
	    public ResponseEntity<Object> getAllRentals(Authentication authentication) {
	        if (authentication == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	        }

	        // Récupérer toutes les locations dans la base de données
	        List<Rentals> rentals = rentalRepository.findAll();

	        // Formater les dates pour chaque location
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
	                    rental.getOwner().getId(), // Récupérer l'ID du propriétaire
	                    createdAt, 
	                    updatedAt  
	                    
	                    
	            );
	            
	        }).collect(Collectors.toList());
	        
	        

	        // Créer un objet de réponse avec la clé "rentals"
	        Map<String, Object> response = new HashMap<>();
	        response.put("rentals", rentalResponses);

	        return ResponseEntity.ok(response);
	        
	        

	    }
	    
	    
	    // Route pour afficher une location par son ID
	    @GetMapping("/api/rentals/{id}")
	    public ResponseEntity<RentalResponse> getRentalById(@PathVariable Integer id, Authentication authentication) {
	        if (authentication == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	        }

	        // Récupérer la location par son ID
	        return rentalRepository.findById(id)
	                .map(rental -> {
	                	
	                	 // Formater les dates
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
	                            rental.getOwner().getId(), // Récupérer l'ID du propriétaire
	                            createdAt, 
	                            updatedAt 
	                        );
	                        return ResponseEntity.ok(response);
	                    })
	                    .orElse(ResponseEntity.notFound().build());
	    }
	    
	    
	    
	    
	    @PutMapping("/api/rentals/{id}")
	    public ResponseEntity<Map<String, String>> updateRental(@PathVariable Integer id, @ModelAttribute RentalRequest rentalRequest, Authentication authentication) throws Exception {
	        if (authentication == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Unauthorized"));
	        }

	     // Récupérer l'utilisateur authentifié (Owner)
	        Optional<Users> Owner = userRepository.findByEmail(authentication.getName());
	        if (Owner.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","User not found"));
	        }

	        Users owner = Owner.get(); // Extraire l'utilisateur authentifié

	        // Récupérer l'offre de location existante
	        Optional<Rentals> Rental = rentalRepository.findById(id);
	        if (Rental.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Rental not found"));
	        }

	        Rentals rental = Rental.get();

	        // Vérifier si l'utilisateur est bien le propriétaire de l'offre
	        if (!rental.getOwner().getId().equals(owner.getId())) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","You are not the owner of this rental"));
	        }

	        // Mettre à jour les informations de l'offre
	        rental.setName(rentalRequest.getName());
	        rental.setSurface(rentalRequest.getSurface());
	        rental.setPrice(rentalRequest.getPrice());
	        rental.setDescription(rentalRequest.getDescription());

	        // Si une nouvelle image est fournie, la sauvegarder et remplacer l'ancienne
	        MultipartFile picture = rentalRequest.getPicture();
	        if (picture != null && !picture.isEmpty()) {
	            String imagePath = saveImage(picture); // Sauvegarder la nouvelle image
	            rental.setPicture(imagePath); // Mettre à jour le chemin de l'image
	        }

	        // Enregistrer les modifications dans la base de données
	        rentalRepository.save(rental);

	        Map<String, String> response = new HashMap<>();
	        response.put("message", "Rental updated !");

	        return ResponseEntity.ok(response);
	    }
	

}
