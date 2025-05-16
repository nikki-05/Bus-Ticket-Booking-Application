package com.example.BusTicketBookingApp.service;
import com.example.BusTicketBookingApp.entity.*;
import com.example.BusTicketBookingApp.exception.InvalidRouteException;
import com.example.BusTicketBookingApp.exception.ResourceNotFoundException;
import com.example.BusTicketBookingApp.exception.SeatAlreadyBookedException;
import com.example.BusTicketBookingApp.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DiscountRepository discountRepository;



    @Transactional //to prevent users from booking multiple buses
    @Override
    public Orders saveOrder(Orders order) {
        //to check if seat is booked or not
        int busId = order.getBus().getId();
        int seatNumber = order.getSeatNumber();


        // Check if the seat number is already taken for the bus
        boolean seatTaken = orderRepository.existsByBusIdAndSeatNumber(busId, seatNumber);
        if (seatTaken) {
            throw new SeatAlreadyBookedException("Seat " + seatNumber + " is already booked on this bus.");
        }

        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));
        double originalFare = bus.getFare();
        int bookedCount = orderRepository.countByBusId(busId);
        if (bookedCount >= bus.getSeats()) {
            throw new SeatAlreadyBookedException("All seats on this bus are already booked.");
        }

        if (seatNumber < 1 || seatNumber > bus.getSeats()) {
            throw new IllegalArgumentException("Invalid seat number. Seat must be between 1 and " + bus.getSeats());
        }


        User user = userRepository.findById(order.getUser().getUid())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category category = categoryRepository.findById(order.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (bus.getSource().equalsIgnoreCase(bus.getDestination())) {
            throw new InvalidRouteException("Source and destination cannot be the same.");
        }

        int userId = order.getUser().getUid();
        long userOrderCount = orderRepository.countByUser_Uid(userId);

        if (userOrderCount == 0) {
            // First order – apply WELCOME100
            Discount welcomeDiscount = discountRepository.findByCode("WELCOME100")
                    .orElseThrow(() -> new RuntimeException("Default welcome discount not configured"));

            order.setDiscount(welcomeDiscount);
            double discountedFare = originalFare - (originalFare * welcomeDiscount.getPercentage() / 100);
            order.setDiscountedFare(discountedFare);
        }
        else if (originalFare > 1000.0) {
            //  order – apply 5% discount if fare > 1000
            Discount highFareDiscount = discountRepository.findByCode("FARE5")
                    .orElseThrow(() -> new RuntimeException("High fare discount not configured"));

            order.setDiscount(highFareDiscount);
            double discountedFare = originalFare - (originalFare * highFareDiscount.getPercentage() / 100);
            order.setDiscountedFare(discountedFare);
        }
     else {
        // No discount
        order.setDiscount(null);
        order.setDiscountedFare(originalFare);
    }

        order.setUser(user);
        order.setCategory(category);
        order.setBus(bus);

        return orderRepository.save(order);
    }

    @Override
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Orders getOrderById(int id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Override
    public Orders updateOrder(int id, Orders updatedOrder) {
        Optional<Orders> optional = orderRepository.findById(id);
        if (optional.isPresent()) {
            Orders order = optional.get();
            order.setDate(updatedOrder.getDate());
            order.setUser(updatedOrder.getUser());
            order.setCategory(updatedOrder.getCategory());
            return orderRepository.save(order);
        }
        return null;
    }

    @Override
    public boolean deleteOrder(int id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Override
    public int countOrdersByCategoryName(String categoryName) {
        return orderRepository.countOrdersByCategoryName(categoryName);
    }
    @Override
    public int countOrdersByUserId(int userId) {
        return orderRepository.countByUser_Uid(userId);
    }
    @Override
    public List<Orders> getOrdersByUserId(int userId) {
        return orderRepository.findByUser_Uid(userId);
    }




}
