package com.example.BusTicketBookingApp.controller;

import com.example.BusTicketBookingApp.entity.Bus;
import com.example.BusTicketBookingApp.repository.BusRepository;
import com.example.BusTicketBookingApp.repository.ReviewRepository;
import com.example.BusTicketBookingApp.response.BusSeatInfoResponse;
import com.example.BusTicketBookingApp.service.BusService;
import com.example.BusTicketBookingApp.service.BusServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/buses")
public class BusController {

    @Autowired
    private BusService busService;
    @Autowired
    private BusRepository busRepository;

    @Autowired
    private ReviewRepository reviewRepository;


    @GetMapping("/all")
    public ResponseEntity<List<Bus>> getAllBuses() {
        List<Bus> buses = busRepository.findAll();
        return ResponseEntity.ok(buses);
    }

    // Create a new bus
    @PostMapping
    public ResponseEntity<?> createBus(@Valid @RequestBody Bus bus) {
        try {
            Bus savedBus = busService.saveBus(bus);
            return ResponseEntity.ok(savedBus);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // or BAD_REQUEST
                    .body(Map.of(
                            "error", "Conflict",
                            "message", ex.getMessage(),
                            "status", HttpStatus.CONFLICT.value(),
                            "timestamp", java.time.Instant.now()
                    ));
        }
    }
    // Get a bus by ID
    @GetMapping("/{id}")
    public ResponseEntity<Bus> getBusById(@PathVariable int id) {
        Bus bus = busService.getBusById(id);
        return bus != null ? ResponseEntity.ok(bus) : ResponseEntity.notFound().build();
    }

    // Update a bus by ID
    @PutMapping("/{id}")
    public ResponseEntity<Bus> updateBus(@PathVariable int id, @RequestBody Bus updatedBus) {
        Bus bus = busService.updateBus(id, updatedBus);
        return bus != null ? ResponseEntity.ok(bus) : ResponseEntity.notFound().build();
    }

    // Delete a bus by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable int id) {
        boolean deleted = busService.deleteBus(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // Get buses by source and destination
    @GetMapping("/search")
    public ResponseEntity<List<Bus>> getBusesByRoute(
            @RequestParam String source,
            @RequestParam String destination) {
        List<Bus> buses = busService.getBusesByRoute(source, destination);
        return ResponseEntity.ok(buses);
    }
    @GetMapping("/{id}/seats")
    public ResponseEntity<BusSeatInfoResponse> getBusSeatInfo(@PathVariable int id) {
        Bus bus = busService.getBusById(id);
        if (bus == null) {
            return ResponseEntity.notFound().build();
        }
        BusSeatInfoResponse response = ((BusServiceImpl) busService).toResponse(bus);
        return ResponseEntity.ok(response);
    }
    // Get buses between a time window for a specific route
    @GetMapping("/search/window")
    public ResponseEntity<List<Bus>> searchBusesWithTimeWindow(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam(required = false) String date,
            @RequestParam String fromTime,
            @RequestParam String toTime) {
        List<Bus> buses = ((BusServiceImpl) busService)
                .searchBusesWithTimeWindow(source, destination, date, fromTime, toTime);
        return ResponseEntity.ok(buses);
    }
    @GetMapping("/routes/alternative")
    public ResponseEntity<List<Map<String, Object>>> getAlternativeRoutes(
            @RequestParam String source,
            @RequestParam String destination) {
        List<Map<String, Object>> routes = busService.getAlternativeRoutes(source, destination);
        return ResponseEntity.ok(routes);
    }
    @GetMapping
    public List<Map<String, Object>> getAllBusesWithRatings() {
        List<Bus> buses = busRepository.findAll();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Bus bus : buses) {
            Double avgRating = reviewRepository.findAverageRatingByBusId(bus.getId());

            Map<String, Object> busInfo = new HashMap<>();
            busInfo.put("id", bus.getId());
            busInfo.put("busName", bus.getBusName());
            busInfo.put("source", bus.getSource());
            busInfo.put("destination", bus.getDestination());
            busInfo.put("fare", bus.getFare());
            busInfo.put("averageRating", avgRating != null ? avgRating : "No ratings yet");

            response.add(busInfo);
        }

        return response;
    }
    @GetMapping("/sortedByFare")
    public ResponseEntity<List<Bus>> getBusesSortedByFare(@RequestParam(defaultValue = "asc") String order) {
        List<Bus> sortedBuses = busService.getAllBusesSortedByFare(order);
        return ResponseEntity.ok(sortedBuses);
    }

}
