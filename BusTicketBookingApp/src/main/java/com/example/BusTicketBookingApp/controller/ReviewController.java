package com.example.BusTicketBookingApp.controller;

import com.example.BusTicketBookingApp.entity.Bus;
import com.example.BusTicketBookingApp.entity.Review;
import com.example.BusTicketBookingApp.entity.User;
import com.example.BusTicketBookingApp.repository.BusRepository;
import com.example.BusTicketBookingApp.repository.ReviewRepository;
import com.example.BusTicketBookingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Review> submitReview(@RequestBody Review review) {
        // Optional: verify if the user has booked the bus before allowing to review

        Bus bus = busRepository.findById(review.getBus().getId())
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        User user = userRepository.findById(review.getUser().getUid())
                .orElseThrow(() -> new RuntimeException("User not found"));

        review.setBus(bus);
        review.setUser(user);

        return ResponseEntity.ok(reviewRepository.save(review));
    }

    @GetMapping("/bus/{busId}")
    public ResponseEntity<List<Review>> getReviewsForBus(@PathVariable int busId) {
        return ResponseEntity.ok(reviewRepository.findByBus_Id(busId));
    }
    @GetMapping("/bus/{busId}/details")
    public ResponseEntity<?> getBusReviewDetails(@PathVariable int busId) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        Double avgRating = reviewRepository.findAverageRatingByBusId(busId);
        List<Review> reviews = reviewRepository.findByBus_Id(busId);

        Map<String, Object> response = new HashMap<>();
        response.put("busId", bus.getId());
        response.put("busName", bus.getBusName());
        response.put("averageRating", avgRating != null ? avgRating : "No ratings yet");
        response.put("reviews", reviews);
        return ResponseEntity.ok(response);
    }
}
