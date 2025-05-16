package com.example.BusTicketBookingApp.service;

import com.example.BusTicketBookingApp.entity.Bus;
import com.example.BusTicketBookingApp.exception.InvalidRouteException;
import com.example.BusTicketBookingApp.repository.BusRepository;
import com.example.BusTicketBookingApp.repository.CategoryRepository;
import com.example.BusTicketBookingApp.repository.OrderRepository;
import com.example.BusTicketBookingApp.response.BusSeatInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusServiceImpl implements BusService {

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Bus saveBus(Bus bus) {
        if (bus.getSeats() <= 0 || bus.getSeats() > 40) {
            throw new IllegalArgumentException("Seats must be between 1 and 40");
        }
        if (bus.getCategory() != null && bus.getCategory().getId() > 0) {
            bus.setCategory(categoryRepository.findById(bus.getCategory().getId()).orElse(null));
        }
        boolean exists = busRepository.existsByBusNameAndSourceAndDestinationAndDepartureTimeAndFareAndDistanceAndTravelDurationAndCategory_Id(
                bus.getBusName(),
                bus.getSource(),
                bus.getDestination(),
                bus.getDepartureTime(),
                bus.getFare(),
                bus.getDistance(),
                bus.getTravelDuration(),
                bus.getCategory().getId()
        );

        if (exists) {
            throw new IllegalArgumentException("Bus with these attributes already exists.");
        }
        return busRepository.save(bus);
    }


    @Override
    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    @Override
    public Bus getBusById(int id) {
        return busRepository.findById(id).orElse(null);
    }

    @Override
    public Bus updateBus(int id, Bus updatedBus) {
        Optional<Bus> optionalBus = busRepository.findById(id);
        if (optionalBus.isPresent()) {
            Bus bus = optionalBus.get();
            bus.setBusName(updatedBus.getBusName());
            bus.setSource(updatedBus.getSource());
            bus.setDestination(updatedBus.getDestination());
            bus.setDepartureTime(updatedBus.getDepartureTime());
            bus.setFare(updatedBus.getFare());
            bus.setDistance(updatedBus.getDistance());
            bus.setTravelDuration(updatedBus.getTravelDuration());
            bus.setSeats(updatedBus.getSeats());
            return busRepository.save(bus);
        }
        return null;
    }

    @Override
    public boolean deleteBus(int id) {
        if (busRepository.existsById(id)) {
            busRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Bus> getBusesByRoute(String source, String destination) {
        List<Bus> buses = busRepository.findBySourceIgnoreCaseAndDestinationIgnoreCase(source, destination);
        if (buses.isEmpty()) {
            throw new InvalidRouteException("No buses available from " + source + " to " + destination);
        }
        return buses;
    }

    public List<Bus> searchBusesWithTimeWindow(String source, String destination, String date, String fromTime, String toTime) {
        List<Bus> allMatchingBuses = busRepository.findBySourceIgnoreCaseAndDestinationIgnoreCase(source, destination);
        if (fromTime != null && toTime != null) {
            LocalTime start = LocalTime.parse(fromTime);
            LocalTime end = LocalTime.parse(toTime);
            return allMatchingBuses.stream()
                    .filter(bus -> {
                        LocalTime departure = bus.getDepartureTime();
                        return (departure.isAfter(start) || departure.equals(start)) &&
                                (departure.isBefore(end) || departure.equals(end));
                    })
                    .collect(Collectors.toList());
        }
        return allMatchingBuses;
    }

    public BusSeatInfoResponse toResponse(Bus bus) {
        int totalSeats = bus.getSeats();
        int booked = orderRepository.countByBusId(bus.getId());
        int available = totalSeats - booked;

        BusSeatInfoResponse response = new BusSeatInfoResponse();
        response.setId(bus.getId());
        response.setBusName(bus.getBusName());
        response.setSource(bus.getSource());
        response.setDestination(bus.getDestination());
        response.setDepartureTime(bus.getDepartureTime());
        response.setFare(bus.getFare());
        response.setDistance(bus.getDistance());
        response.setTravelDuration(bus.getTravelDuration());
        response.setTotalSeats(totalSeats);
        response.setBookedSeats(booked);
        response.setAvailableSeats(available);

        return response;
    }

    public int getAvailableSeats(int busId) {
        Bus bus = busRepository.findById(busId).orElse(null);
        if (bus == null) {
            throw new RuntimeException("Bus not found");
        }
        int booked = orderRepository.countByBusId(busId);
        return bus.getSeats() - booked;
    }
    @Override
    public List<Map<String, Object>> getAlternativeRoutes(String source, String destination){

    List<Map<String, Object>> allRoutes = new ArrayList<>();

        // Direct Routes
        List<Bus> directRoutes = busRepository.findBySourceIgnoreCaseAndDestinationIgnoreCase(source, destination);
        for (Bus bus : directRoutes) {
            Map<String, Object> directRoute = new HashMap<>();
            directRoute.put("type", "direct");
            directRoute.put("route", List.of(bus));
            allRoutes.add(directRoute);
        }

        // Alternative Routes
        List<Object[]> results = busRepository.findAlternativeRouteIds(source, destination);
        for (Object[] row : results) {
            Integer firstId = (Integer) row[0];
            Integer secondId = (Integer) row[1];
            Bus firstLeg = busRepository.findById(firstId).orElse(null);
            Bus secondLeg = busRepository.findById(secondId).orElse(null);

            if (firstLeg != null && secondLeg != null) {
                Map<String, Object> altRoute = new HashMap<>();
                altRoute.put("type", "alternative");
                altRoute.put("route", List.of(firstLeg, secondLeg));
                allRoutes.add(altRoute);
            }
        }

        return allRoutes;
    }
    @Override
    public List<Bus> getAllBusesSortedByFare(String order) {
        List<Bus> buses = busRepository.findAll();
        if ("desc".equalsIgnoreCase(order)) {
            buses.sort((b1, b2) -> Double.compare(b2.getFare(), b1.getFare()));
        } else {
            buses.sort(Comparator.comparingDouble(Bus::getFare));
        }
        return buses;
    }


}
