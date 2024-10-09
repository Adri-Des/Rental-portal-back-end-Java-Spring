package com.openclassrooms.rentalAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.openclassrooms.rentalAPI.models.Messages;


public interface MessageRepository extends JpaRepository<Messages, Integer> {
}

