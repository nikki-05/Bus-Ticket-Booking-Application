package com.example.BusTicketBookingApp.repository;

import com.example.BusTicketBookingApp.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByBus_Id(int busId);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.bus.id = :busId")
    Double findAverageRatingByBusId(@Param("busId") int busId);

}