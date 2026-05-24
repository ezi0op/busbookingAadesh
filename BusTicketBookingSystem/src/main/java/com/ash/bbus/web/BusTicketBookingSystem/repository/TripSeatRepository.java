package com.ash.bbus.web.BusTicketBookingSystem.repository;

import com.ash.bbus.web.BusTicketBookingSystem.entity.TripSeat;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TripSeatRepository extends JpaRepository<TripSeat, Long> {

    // ✅ Get all seats for a trip (seat layout UI)
    List<TripSeat> findByTripId(Long tripId);

    // ✅ Get specific seats by IDs for a trip (lock/book)
    List<TripSeat> findByIdInAndTripId(List<Long> ids, Long tripId);

    // ✅ Get booked seats for a booking
    List<TripSeat> findByBookingId(Long bookingId);

    // ✅ Get available seats for a trip
    List<TripSeat> findByTripIdAndStatus(Long tripId, SeatStatus status);

    // ✅ Find expired locks — used by scheduler
    List<TripSeat> findByStatusAndLockExpiryTimeBefore(
        SeatStatus status, LocalDateTime time
    );

    // ✅ Count available seats
    int countByTripIdAndStatus(Long tripId, SeatStatus status);
}