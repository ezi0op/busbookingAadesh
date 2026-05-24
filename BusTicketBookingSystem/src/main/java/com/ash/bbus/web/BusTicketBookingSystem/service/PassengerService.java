package com.ash.bbus.web.BusTicketBookingSystem.service;

import java.util.List;

import com.ash.bbus.web.BusTicketBookingSystem.dto.PassengerDTO;

public interface PassengerService {

    void savePassengers(Long bookingId, List<PassengerDTO> passengers);
}
