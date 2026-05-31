package com.ash.bbus.web.BusTicketBookingSystem.repository;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Trip;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("""
        SELECT t
        FROM Trip t
        JOIN FETCH t.bus
        JOIN FETCH t.route
        """)
    List<Trip> findAllWithBusAndRoute();

    // ✅ Used by searchTrips() in TripServiceImpl
    List<Trip> findByRoute_SourceIgnoreCaseAndRoute_DestinationIgnoreCaseAndTravelDateAndStatus(
        String source,
        String destination,
        LocalDate travelDate,
        TripStatus status
    );

    List<Trip> findByBusId(Long busId);
    List<Trip> findByStatus(TripStatus status);
    List<Trip> findByTravelDate(LocalDate date);
}
