package com.openclassrooms.rentalAPI.services;


import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.openclassrooms.rentalAPI.models.MessageRequest;
import com.openclassrooms.rentalAPI.models.Messages;
import com.openclassrooms.rentalAPI.models.Rentals;
import com.openclassrooms.rentalAPI.models.Users;
import com.openclassrooms.rentalAPI.repository.MessageRepository;
import com.openclassrooms.rentalAPI.repository.RentalRepository;
import com.openclassrooms.rentalAPI.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    // Method to send a message
    public ResponseEntity<Map<String, String>> sendMessage(MessageRequest messageRequest, String userEmail) {
        // Check if a field is empty
        if (messageRequest.getRentalId() == null || 
            messageRequest.getUserId() == null || 
            messageRequest.getMessage() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Bad Request: rental_id, user_id or message is empty"));
        }

        // Retrieve the current user
        Users currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Retrieve the location specified in the message request
        Rentals rental = rentalRepository.findById(messageRequest.getRentalId())
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));

        // Create a message and save it into the database
        Messages newMessage = new Messages();
        newMessage.setRental(rental);
        newMessage.setUser(currentUser);
        newMessage.setMessage(messageRequest.getMessage());
        messageRepository.save(newMessage);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Message sent with success");

        return ResponseEntity.ok(response);
    }
}
