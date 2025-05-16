package com.example.BusTicketBookingApp.controller;

import com.example.BusTicketBookingApp.entity.Discount;
import com.example.BusTicketBookingApp.repository.DiscountRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    @Autowired
    private DiscountRepository discountRepository;

    // Create a new discount
    @PostMapping
    public ResponseEntity<Discount> createDiscount(@Valid @RequestBody Discount discount) {
        Discount saved = discountRepository.save(discount);
        return ResponseEntity.ok(saved);
    }

    // Get all discounts
    @GetMapping
    public ResponseEntity<List<Discount>> getAllDiscounts() {
        return ResponseEntity.ok(discountRepository.findAll());
    }

    // Get a discount by code
    @GetMapping("/code/{code}")
    public ResponseEntity<Discount> getByCode(@PathVariable String code) {
        Optional<Discount> discount = discountRepository.findByCode(code);
        return discount.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get a discount by ID
    @GetMapping("/{id}")
    public ResponseEntity<Discount> getById(@PathVariable int id) {
        return discountRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete a discount
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (!discountRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        discountRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
