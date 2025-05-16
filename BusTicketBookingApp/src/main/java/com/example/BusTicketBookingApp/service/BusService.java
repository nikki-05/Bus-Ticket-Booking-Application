package com.example.BusTicketBookingApp.service;
import com.example.BusTicketBookingApp.entity.Bus;
import java.util.List;
import java.util.Map;

public interface BusService {
    Bus saveBus(Bus bus);
    List<Bus> getAllBuses();
    Bus getBusById(int id);
    Bus updateBus(int id, Bus bus);
    boolean deleteBus(int id);

    List<Bus> getBusesByRoute(String source, String destination);
    List<Map<String, Object>> getAlternativeRoutes(String source, String destination);
    List<Bus> getAllBusesSortedByFare(String order); // "asc" or "desc"


}