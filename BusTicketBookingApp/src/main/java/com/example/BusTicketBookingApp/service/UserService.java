package com.example.BusTicketBookingApp.service;

import com.example.BusTicketBookingApp.entity.Orders;
import com.example.BusTicketBookingApp.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    List<User> getAllUsers();
    User getUserById(int id);
    User updateUser(int id, User user);
    boolean deleteUser(int id);
    User registerUser(User user);
    Optional<User> getUserByEmail(String email);
    List<Orders> getOrdersByUserId(int userId);



}
