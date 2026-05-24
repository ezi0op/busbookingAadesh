package com.ash.bbus.web.BusTicketBookingSystem.repository;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Booking;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);
    List<Booking> findByTripId(Long tripId);
    List<Booking> findByStatus(BookingStatus status);
    boolean existsByIdAndUserId(Long bookingId, Long userId);
}