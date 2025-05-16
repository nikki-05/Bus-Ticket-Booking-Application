package com.example.BusTicketBookingApp.repository;

import com.example.BusTicketBookingApp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  CategoryRepository  extends JpaRepository<Category, Integer> {

}
