package com.example.BusTicketBookingApp.repository;

import com.example.BusTicketBookingApp.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BusRepository  extends JpaRepository<Bus, Integer> {
    List<Bus> findBySourceIgnoreCaseAndDestinationIgnoreCase(String source, String destination);
    List<Bus> findBySourceIgnoreCase(String source);
    @Query(value = """
SELECT b1.id, b2.id
FROM bus b1
JOIN bus b2 ON LOWER(b1.destination) = LOWER(b2.source)
WHERE LOWER(b1.source) = LOWER(:source)
  AND LOWER(b2.destination) = LOWER(:destination)
""", nativeQuery = true)
    List<Object[]> findAlternativeRouteIds(String source, String destination);
    boolean existsByBusNameAndSourceAndDestinationAndDepartureTimeAndFareAndDistanceAndTravelDurationAndCategory_Id(
            String busName,
            String source,
            String destination,
            java.time.LocalTime departureTime,
            double fare,
            double distance,
            double travelDuration,
            int categoryId
    );


}
