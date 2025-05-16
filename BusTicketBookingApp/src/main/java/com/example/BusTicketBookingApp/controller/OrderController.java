package com.example.BusTicketBookingApp.controller;

import com.example.BusTicketBookingApp.entity.Bus;
import com.example.BusTicketBookingApp.entity.Orders;
import com.example.BusTicketBookingApp.exception.ResourceNotFoundException;
import com.example.BusTicketBookingApp.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Orders>> getAllOrders() {
        List<Orders> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // USER: Place a new order
    @PostMapping
    public ResponseEntity<Orders> createOrder(@Valid @RequestBody Orders order) {
        Orders savedOrder = orderService.saveOrder(order);
        return ResponseEntity.ok(savedOrder);
    }

    // USER: Get an order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable int id) {
        Orders order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    // USER: Update an order
    @PutMapping("/{id}")
    public ResponseEntity<Orders> updateOrder(@PathVariable int id, @Valid @RequestBody Orders updatedOrder) {
        Orders order = orderService.updateOrder(id, updatedOrder);
        return ResponseEntity.ok(order);
    }

    // USER: Delete an order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable int id) {
        boolean deleted = orderService.deleteOrder(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Order not found with ID: " + id);
        }
        return ResponseEntity.ok().build();
    }
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Integer> countOrdersByUserId(@PathVariable int userId) {
        int count = orderService.countOrdersByUserId(userId);
        return ResponseEntity.ok(count);
    }
    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<Object> getUserOrderSummary(@PathVariable int userId) {
        List<Orders> userOrders = orderService.getOrdersByUserId(userId);
        int totalOrders = userOrders.size();

        List<Bus> buses = userOrders.stream()
                .map(Orders::getBus)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("totalOrders", totalOrders);
        response.put("buses", buses);

        return ResponseEntity.ok(response);
    }


}
