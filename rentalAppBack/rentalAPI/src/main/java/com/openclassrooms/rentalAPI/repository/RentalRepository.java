package com.openclassrooms.rentalAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.openclassrooms.rentalAPI.models.Rentals;

public interface RentalRepository extends JpaRepository<Rentals, Integer> {
}