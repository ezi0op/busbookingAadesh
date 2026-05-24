package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.dto.TripDTO;
import com.ash.bbus.web.BusTicketBookingSystem.entity.Bus;
import com.ash.bbus.web.BusTicketBookingSystem.entity.Route;
import com.ash.bbus.web.BusTicketBookingSystem.entity.Trip;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.TripStatus;
import com.ash.bbus.web.BusTicketBookingSystem.exception.ResourceNotFoundException;
import com.ash.bbus.web.BusTicketBookingSystem.repository.BusRepository;
import com.ash.bbus.web.BusTicketBookingSystem.repository.RouteRepository;
import com.ash.bbus.web.BusTicketBookingSystem.repository.TripRepository;
import com.ash.bbus.web.BusTicketBookingSystem.service.TripService;
import com.ash.bbus.web.BusTicketBookingSystem.service.TripSeatService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final TripSeatService tripSeatService;

    public TripServiceImpl(TripRepository tripRepository,
                           BusRepository busRepository,
                           RouteRepository routeRepository,
                           TripSeatService tripSeatService) {
        this.tripRepository = tripRepository;
        this.busRepository = busRepository;
        this.routeRepository = routeRepository;
        this.tripSeatService = tripSeatService;
    }

    @Override
    @Transactional
    public TripDTO createTrip(TripDTO dto) {

        Bus bus = busRepository.findById(dto.getBusId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Bus not found: " + dto.getBusId()
            ));

        Route route = routeRepository.findById(dto.getRouteId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Route not found: " + dto.getRouteId()
            ));

        Trip trip = new Trip();
        trip.setBus(bus);
        trip.setRoute(route);
        trip.setTravelDate(dto.getTravelDate());
        trip.setDepartureTime(dto.getDepartureTime());
        trip.setArrivalTime(dto.getArrivalTime());
        trip.setPrice(dto.getPrice());
        trip.setStatus(TripStatus.SCHEDULED);

        // ✅ availableSeats initialized from bus total
        trip.setAvailableSeats(bus.getTotalSeats());

        Trip saved = tripRepository.save(trip);

        // ✅ Auto-generate TripSeats after trip creation
        tripSeatService.generateSeatsForTrip(saved.getId());

        return mapToDTO(saved);
    }

    @Override
    public TripDTO getTripById(Long id) {
        return mapToDTO(findTrip(id));
    }

    @Override
    public List<TripDTO> getAllTrips() {
        return tripRepository.findAll()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<TripDTO> searchTrips(String source,
                                      String destination,
                                      LocalDate travelDate) {
        return tripRepository
            .findByRoute_SourceIgnoreCaseAndRoute_DestinationIgnoreCaseAndTravelDateAndStatus(
                source, destination, travelDate, TripStatus.SCHEDULED
            )
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TripDTO updateTrip(Long id, TripDTO dto) {
        Trip trip = findTrip(id);

        trip.setTravelDate(dto.getTravelDate());
        trip.setDepartureTime(dto.getDepartureTime());
        trip.setArrivalTime(dto.getArrivalTime());
        trip.setPrice(dto.getPrice());

        return mapToDTO(tripRepository.save(trip));
    }

    @Override
    @Transactional
    public void cancelTrip(Long id) {
        Trip trip = findTrip(id);
        trip.setStatus(TripStatus.CANCELLED);
        tripRepository.save(trip);
    }

    private Trip findTrip(Long id) {
        return tripRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Trip not found: " + id
            ));
    }

    private TripDTO mapToDTO(Trip trip) {
        TripDTO dto = new TripDTO();
        dto.setId(trip.getId());
        dto.setTravelDate(trip.getTravelDate());
        dto.setDepartureTime(trip.getDepartureTime());
        dto.setArrivalTime(trip.getArrivalTime());
        dto.setPrice(trip.getPrice());
        dto.setAvailableSeats(trip.getAvailableSeats());
        dto.setTotalSeats(trip.getBus().getTotalSeats());
        dto.setStatus(trip.getStatus());
        dto.setBusId(trip.getBus().getId());
        dto.setBusName(trip.getBus().getName());
        dto.setBusNumber(trip.getBus().getBusNumber());
        dto.setRouteId(trip.getRoute().getId());
        dto.setSource(trip.getRoute().getSource());
        dto.setDestination(trip.getRoute().getDestination());
        return dto;
    }
}