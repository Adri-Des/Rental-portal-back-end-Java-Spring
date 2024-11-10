package com.openclassrooms.rentalAPI.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.rentalAPI.models.LoginRequest;
import com.openclassrooms.rentalAPI.models.Users;
import com.openclassrooms.rentalAPI.repository.UserRepository;
import com.openclassrooms.rentalAPI.services.JWTService;

@RestController
public class LoginController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JWTService jwtService;
	    
	    

	/*public LoginController(JWTService jwtService) {
		this.jwtService = jwtService;
	}*/

	@PostMapping("/api/auth/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
		// Retrieve the user by email
	    Optional<Users> userOptional = userRepository.findByEmail(loginRequest.getEmail());

	    // Check if user exists in the database
	    if (userOptional.isEmpty()) {
	        return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
	        
	    }

	    // Extract the user 
	    Users user = userOptional.get();

	    // Verify the password against the hashed password in the database
	    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
	        return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
	    }

	    // Create an Authentication instance with user details
	    Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, List.of());
	    
	    // Generate a JWT token using the Authentication instance
	    String token = jwtService.generateToken(authentication);
	    
	    // Build a response containing the JWT token
	    Map<String, String> response = new HashMap<>();
	    response.put("token", token);

	    return ResponseEntity.ok(response);
	}


}