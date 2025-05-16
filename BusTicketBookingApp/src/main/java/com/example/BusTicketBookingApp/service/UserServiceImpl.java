package com.example.BusTicketBookingApp.service;

import com.example.BusTicketBookingApp.entity.Orders;
import com.example.BusTicketBookingApp.entity.User;
import com.example.BusTicketBookingApp.exception.EmailAlreadyExistsException;
import com.example.BusTicketBookingApp.exception.ResourceNotFoundException;
import com.example.BusTicketBookingApp.exception.UserDeletionException;
import com.example.BusTicketBookingApp.repository.OrderRepository;
import com.example.BusTicketBookingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateUser(int id, User updatedUser) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            User user = optional.get();
            user.setName(updatedUser.getName());
            return userRepository.save(user);
        }
        return null;
    }


    @Override
    public boolean deleteUser(int userId) {
        // Check if user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        // Check if user has any bookings
        int orderCount = orderRepository.countByUser_Uid(userId);
        if (orderCount > 0) {
            throw new UserDeletionException("User with bookings cannot be deleted.");
        }

        // Safe to delete
        userRepository.deleteById(userId);
        return true;
    }

    @Override
    public User registerUser(User user) {
        Optional<User> existing = userRepository.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public List<Orders> getOrdersByUserId(int userId) {
        return orderRepository.findByUser_Uid(userId);
    }

}
