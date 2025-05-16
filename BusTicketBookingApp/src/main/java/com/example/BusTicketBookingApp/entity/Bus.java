package com.example.BusTicketBookingApp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalTime;

@Entity
@Table(name="Bus")
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank(message = "Bus name is required")
    private String busName;

    @NotBlank(message = "Source is required")
    private String source;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotNull(message = "Departure time is required")
    private LocalTime departureTime;

    @Positive(message = "Fare must be a positive value")
    @Min(value = 20, message = "Fare must be at least 20")
    private double fare;

    @Positive(message = "Distance must be a positive number")
    private double distance; // in kms

    @NotNull(message = "Travel duration is required")
    private double travelDuration;

    @NotNull(message = "Seats must not be null")
    @Min(value = 1, message = "Seats must be at least 1")
    @Max(value = 40, message = "Seats must not be more than 40")
    private int seats;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    public Bus() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTravelDuration() {
        return travelDuration;
    }

    public void setTravelDuration(double travelDuration) {
        this.travelDuration = travelDuration;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "id=" + id +
                ", busName='" + busName + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", fare=" + fare +
                ", distance=" + distance +
                ", travelDuration=" + travelDuration +
                ", seats=" + seats +
                ", category=" + category +
                '}';
    }
}
