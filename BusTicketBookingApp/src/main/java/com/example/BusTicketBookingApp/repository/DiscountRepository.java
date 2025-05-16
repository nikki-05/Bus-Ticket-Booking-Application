package com.example.BusTicketBookingApp.repository;

import com.example.BusTicketBookingApp.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    Optional<Discount> findByCode(String code);
}
