package com.ash.bbus.web.BusTicketBookingSystem.dao;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Booking;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.BookingStatus;

import java.time.LocalDate;
import java.util.List;

public interface BookingDao {

    // ✅ Get bookings by user + status
    List<Booking> findByUserIdAndStatus(Long userId,
                                         BookingStatus status);

    // ✅ Get bookings for a trip on a date
    List<Booking> findByTripDate(LocalDate date);

    // ✅ Count total bookings for a trip
    long countBookingsByTripId(Long tripId);
}