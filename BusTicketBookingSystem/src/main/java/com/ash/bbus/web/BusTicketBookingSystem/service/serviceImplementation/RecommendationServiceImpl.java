package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Trip;
import com.ash.bbus.web.BusTicketBookingSystem.repository.TripRepository;
import com.ash.bbus.web.BusTicketBookingSystem.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final TripRepository tripRepository;

    @Override
    public List<Trip> recommendTrips(Long userId, String destination) {
        // Mock recommendation: Just find trips that go to the destination
        return tripRepository.findAll().stream()
                .filter(trip -> trip.getRoute().getDestination().equalsIgnoreCase(destination))
                .collect(Collectors.toList());
    }
}
