package com.ash.bbus.web.BusTicketBookingSystem.service;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Trip;
import java.util.List;

public interface RecommendationService {
    List<Trip> recommendTrips(Long userId, String destination);
}
