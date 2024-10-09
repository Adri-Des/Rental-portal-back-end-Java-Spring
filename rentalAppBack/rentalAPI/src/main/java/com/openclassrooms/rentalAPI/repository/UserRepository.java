package com.openclassrooms.rentalAPI.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.openclassrooms.rentalAPI.models.Users;

public interface UserRepository extends JpaRepository<Users, Integer> {
	
	Optional<Users> findByEmail(String email);
}