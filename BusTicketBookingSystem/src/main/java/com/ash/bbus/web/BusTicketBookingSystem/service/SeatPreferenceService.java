package com.ash.bbus.web.BusTicketBookingSystem.service;

import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatType;

public interface SeatPreferenceService {
    SeatType getPreferredSeatType(Long userId);
    void savePreferredSeatType(Long userId, SeatType seatType);
}
