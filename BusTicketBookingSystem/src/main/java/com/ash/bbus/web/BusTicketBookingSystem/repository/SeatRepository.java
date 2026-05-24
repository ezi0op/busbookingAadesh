package com.ash.bbus.web.BusTicketBookingSystem.repository;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    // ✅ Used by TripSeatServiceImpl.generateSeatsForTrip()
    List<Seat> findByBusId(Long busId);

    boolean existsByBusIdAndSeatNumber(Long busId, String seatNumber);
}