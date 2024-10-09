package com.openclassrooms.rentalAPI.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.rentalAPI.models.MessageRequest;
import com.openclassrooms.rentalAPI.models.Messages;
import com.openclassrooms.rentalAPI.models.Rentals;
import com.openclassrooms.rentalAPI.models.Users;
import com.openclassrooms.rentalAPI.repository.MessageRepository;
import com.openclassrooms.rentalAPI.repository.RentalRepository;
import com.openclassrooms.rentalAPI.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@RestController
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/api/messages")
    public ResponseEntity<Map<String, String>> sendMessage(
        @RequestBody MessageRequest messageRequest, 
        Authentication authentication) {

        // Vérification si un message ou un rentalId est vide
        if (messageRequest.getRentalId() == null || messageRequest.getUserId() == null || messageRequest.getMessage() == null) {
            return ResponseEntity.badRequest().body(Map.of("message","Bad Request: rental_id, user_id or message is empty"));
        }

        // Récupérer l'utilisateur actuellement connecté
        String userEmail = authentication.getName();
        Users currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Récupérer l'offre de location
        Rentals rental = rentalRepository.findById(messageRequest.getRentalId())
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));

        // Créer et enregistrer le message
        Messages newMessage = new Messages();
        newMessage.setRental(rental);
        newMessage.setUser(currentUser);
        newMessage.setMessage(messageRequest.getMessage());
        messageRepository.save(newMessage);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Message send with success");

        return ResponseEntity.ok(response);
    }
}