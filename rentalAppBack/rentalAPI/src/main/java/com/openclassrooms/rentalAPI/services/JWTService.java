package com.openclassrooms.rentalAPI.services;

import java.time.Instant;

import java.time.temporal.ChronoUnit;


import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.openclassrooms.rentalAPI.models.Users;

@Service
public class JWTService {

	private JwtEncoder jwtEncoder;
	
	public JWTService(JwtEncoder jwtEncoder) {
		this.jwtEncoder = jwtEncoder;
	}
	
	public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }	
	
	
	public String generateTokenFromUser(Users user) {
	    Instant now = Instant.now();
	    JwtClaimsSet claims = JwtClaimsSet.builder()
	            .issuer("self")
	            .issuedAt(now)
	            .expiresAt(now.plus(1, ChronoUnit.DAYS))
	            .subject(user.getEmail())
	            .build();
	    JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
	    return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
	}
}