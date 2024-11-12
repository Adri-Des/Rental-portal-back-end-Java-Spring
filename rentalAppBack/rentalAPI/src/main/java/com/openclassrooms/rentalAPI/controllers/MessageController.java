package com.openclassrooms.rentalAPI.controllers;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.rentalAPI.models.MessageRequest;
import com.openclassrooms.rentalAPI.services.MessageService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    // Route to send a message
    @Operation(summary = "Send a message", description = "This route allows you to send a message to the rental owner")
    @PostMapping("/api/messages")
    public ResponseEntity<Map<String, String>> sendMessage(
        @RequestBody MessageRequest messageRequest, 
        Authentication authentication) {

        String userEmail = authentication.getName();
        return messageService.sendMessage(messageRequest, userEmail);
    }
}
