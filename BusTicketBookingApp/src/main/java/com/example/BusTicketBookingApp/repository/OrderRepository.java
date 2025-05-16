package com.example.BusTicketBookingApp.repository;
import com.example.BusTicketBookingApp.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository  extends JpaRepository<Orders, Integer> {
    @Query("SELECT COUNT(o) FROM Orders o WHERE o.category.name = :categoryName")
    int countOrdersByCategoryName(@Param("categoryName") String categoryName);
    int countByBusId(int busId);
    int countByUser_Uid(int userId);
    boolean existsByBusIdAndSeatNumber(int busId, int seatNumber);
    List<Orders> findByUser_Uid(int userId);


}
