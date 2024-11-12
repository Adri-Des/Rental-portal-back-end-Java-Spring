package com.openclassrooms.rentalAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.openclassrooms.rentalAPI.models.LoginRequest;
import com.openclassrooms.rentalAPI.models.Users;
import com.openclassrooms.rentalAPI.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LoginService {
	
	 @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    @Autowired
	    private JWTService jwtService;

	    public Map<String, String> login(LoginRequest loginRequest) {
	        // Retrieve the user by email
	        Optional<Users> userOptional = userRepository.findByEmail(loginRequest.getEmail());

	        if (userOptional.isEmpty()) {
	            return Map.of("error", "Invalid email or password");
	        }

	        // Extract the user
	        Users user = userOptional.get();

	        // Verify the password against the hashed password
	        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
	            return Map.of("error", "Invalid email or password");
	        }

	        // Create Authentication instance and generate JWT token
	        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, List.of());
	        String token = jwtService.generateToken(authentication);

	        // Return the token in a map
	        Map<String, String> response = new HashMap<>();
	        response.put("token", token);

	        return response;
	    }
	

}
