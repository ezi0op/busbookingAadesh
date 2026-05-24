package com.ash.bbus.web.BusTicketBookingSystem.service;

import com.ash.bbus.web.BusTicketBookingSystem.dto.TripDTO;
import java.time.LocalDate;
import java.util.List;

public interface TripService {

    TripDTO createTrip(TripDTO dto);
    TripDTO getTripById(Long id);
    List<TripDTO> getAllTrips();

    // ✅ Search trips — used by frontend search page
    List<TripDTO> searchTrips(String source, String destination,
                              LocalDate travelDate);

    TripDTO updateTrip(Long id, TripDTO dto);
    void cancelTrip(Long id);
}