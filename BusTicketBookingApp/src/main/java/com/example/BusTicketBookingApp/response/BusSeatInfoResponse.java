package com.example.BusTicketBookingApp.response;

import java.time.LocalTime;

public class BusSeatInfoResponse {
    private int id;
    private String busName;
    private String source;
    private String destination;
    private LocalTime departureTime;
    private double fare;
    private double distance;
    private double travelDuration;

    private int totalSeats;
    private int bookedSeats;
    private int availableSeats;

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

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(int bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return "BusSeatInfoResponse{" +
                "id=" + id +
                ", busName='" + busName + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", fare=" + fare +
                ", distance=" + distance +
                ", travelDuration=" + travelDuration +
                ", totalSeats=" + totalSeats +
                ", bookedSeats=" + bookedSeats +
                ", availableSeats=" + availableSeats +
                '}';
    }
}
