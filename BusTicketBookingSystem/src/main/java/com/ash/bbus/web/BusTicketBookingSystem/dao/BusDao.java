package com.ash.bbus.web.BusTicketBookingSystem.dao;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Bus;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.BusType;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatType;

import java.util.List;

public interface BusDao {

    // ✅ Filter buses by type
    List<Bus> findByBusType(BusType busType);

    // ✅ Filter buses by seat type
    List<Bus> findBySeatType(SeatType seatType);

    // ✅ Search buses by name (partial match)
    List<Bus> searchByName(String keyword);

    // ✅ Find buses with available trips on a date
    List<Bus> findBusesWithAvailableTrips(String source,
                                           String destination,
                                           String date);
}