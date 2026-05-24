package com.ash.bbus.web.BusTicketBookingSystem.service;

import com.ash.bbus.web.BusTicketBookingSystem.dto.TripSeatDTO;
import java.util.List;

public interface TripSeatService {

    // ✅ Auto-generate seats when trip is created
    void generateSeatsForTrip(Long tripId);

    // ✅ Get all seats for a trip (for seat layout UI)
    List<TripSeatDTO> getSeatsByTrip(Long tripId);

    // ✅ Lock seats when user selects them
    List<TripSeatDTO> lockSeats(Long tripId, List<Long> seatIds, Long userId);

    // ✅ Release expired locks (called by scheduler)
    void releaseExpiredLocks();
}