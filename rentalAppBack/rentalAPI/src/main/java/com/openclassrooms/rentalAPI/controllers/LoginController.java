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
import com.openclassrooms.rentalAPI.services.LoginService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class LoginController {

	

	@Autowired
    private LoginService loginService;
	    
	    

	/*public LoginController(JWTService jwtService) {
		this.jwtService = jwtService;
	}*/

	
	@Operation(summary = "Login page", description = "This route allows an existing user to connect")
	@PostMapping("/api/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        Map<String, String> response = loginService.login(loginRequest);

        
        if (response.containsKey("error")) {
            return ResponseEntity.status(401).body(response);
        }

        return ResponseEntity.ok(response);
    }


}