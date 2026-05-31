package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.dto.TripSeatDTO;
import com.ash.bbus.web.BusTicketBookingSystem.entity.*;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.*;
import com.ash.bbus.web.BusTicketBookingSystem.exception.ResourceNotFoundException;
import com.ash.bbus.web.BusTicketBookingSystem.exception.SeatAlreadyBookedException;
import com.ash.bbus.web.BusTicketBookingSystem.repository.*;
import com.ash.bbus.web.BusTicketBookingSystem.service.TripSeatService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripSeatServiceImpl implements TripSeatService {

    private final TripSeatRepository tripSeatRepository;
    private final TripRepository tripRepository;
    private final SeatRepository seatRepository;

    public TripSeatServiceImpl(TripSeatRepository tripSeatRepository,
                                TripRepository tripRepository,
                                SeatRepository seatRepository) {
        this.tripSeatRepository = tripSeatRepository;
        this.tripRepository = tripRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    @Transactional
    public void generateSeatsForTrip(Long tripId) {

        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Trip not found: " + tripId
            ));

        // ✅ Get all physical seats of the bus
        List<Seat> busSeats = seatRepository.findByBusId(
            trip.getBus().getId()
        );

        // ✅ Generate one TripSeat per physical Seat
        List<TripSeat> tripSeats = new ArrayList<>();
        for (Seat seat : busSeats) {
            TripSeat tripSeat = new TripSeat();
            tripSeat.setTrip(trip);
            tripSeat.setSeat(seat);
            tripSeat.setStatus(SeatStatus.AVAILABLE);
            tripSeats.add(tripSeat);
        }

        tripSeatRepository.saveAll(tripSeats);
    }

    @Override
    public List<TripSeatDTO> getSeatsByTrip(Long tripId) {
        return tripSeatRepository.findByTripId(tripId)
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<TripSeatDTO> lockSeats(Long tripId,
                                        List<Long> seatIds,
                                        Long userId) {
        List<TripSeat> seats = tripSeatRepository
            .findByIdInAndTripId(seatIds, tripId);

        // ✅ Validate all seats belong to same trip
        if (seats.size() != seatIds.size()) {
            throw new ResourceNotFoundException(
                "One or more seats not found for this trip"
            );
        }

        // ✅ Check none are already booked or locked by someone else
        for (TripSeat seat : seats) {
            if (seat.getStatus() == SeatStatus.BOOKED) {
                throw new SeatAlreadyBookedException(
                    "Seat already booked: " + seat.getSeat().getSeatNumber()
                );
            }
            if (seat.getStatus() == SeatStatus.LOCKED
                    && !seat.getLockedByUserId().equals(userId)
                    && !seat.isLockExpired()) {
                throw new SeatAlreadyBookedException(
                    "Seat is locked by another user: "
                    + seat.getSeat().getSeatNumber()
                );
            }
        }

        // ✅ Lock all selected seats
        seats.forEach(seat -> seat.lockSeat(userId));
        tripSeatRepository.saveAll(seats);

        return seats.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Scheduled(fixedRate = 60000) // runs every 60 seconds
    @Transactional
    public void releaseExpiredLocks() {
        List<TripSeat> expired = tripSeatRepository
            .findByStatusAndLockExpiryTimeBefore(
                SeatStatus.LOCKED, LocalDateTime.now()
            );

        expired.forEach(TripSeat::releaseLock);

        tripSeatRepository.saveAll(expired);
    }

    private TripSeatDTO mapToDTO(TripSeat tripSeat) {
        TripSeatDTO dto = new TripSeatDTO();

        // ✅ was setTripSeatId() → now setId()
        dto.setId(tripSeat.getId());
        dto.setTripId(tripSeat.getTrip().getId());  // ✅ new field
        dto.setStatus(tripSeat.getStatus());
        dto.setAvailable(
            tripSeat.getStatus() == SeatStatus.AVAILABLE
        );
        dto.setSeatNumber(tripSeat.getSeat().getSeatNumber());
        dto.setRowNumber(tripSeat.getSeat().getRowNumber());
        dto.setSeatType(tripSeat.getSeat().getBus().getSeatType()); // ✅
        dto.setBerthType(tripSeat.getSeat().getBerthType());
        dto.setPosition(tripSeat.getSeat().getPosition());
        dto.setSeatGroup(tripSeat.getSeat().getSeatGroup());
        return dto;
    }
}
