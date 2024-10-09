package com.openclassrooms.rentalAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.rentalAPI.models.Users;
import com.openclassrooms.rentalAPI.repository.UserRepository;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/user/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Integer id, Authentication authentication) {
        // Si Spring Security a validé le JWT, l'utilisateur est authentifié
        // Sinon, Spring Security renverra automatiquement une erreur 401 Unauthorized

        // Récupérer l'utilisateur depuis la base de données
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
        
        
        }
    
    @GetMapping("/api/auth/me")
    public ResponseEntity<Users> getCurrentUser(Authentication authentication) {
       
        String userEmail = authentication.getName();
        
        return userRepository.findByEmail(userEmail)
        		.map(user -> ResponseEntity.ok(user))
        		.orElse(ResponseEntity.notFound().build());
    }
   
}