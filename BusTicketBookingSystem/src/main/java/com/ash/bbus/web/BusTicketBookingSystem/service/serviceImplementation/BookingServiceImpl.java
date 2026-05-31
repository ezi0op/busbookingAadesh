package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.dto.BookingDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.PassengerDTO;
import com.ash.bbus.web.BusTicketBookingSystem.entity.*;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.*;
import com.ash.bbus.web.BusTicketBookingSystem.exception.ResourceNotFoundException;
import com.ash.bbus.web.BusTicketBookingSystem.exception.SeatAlreadyBookedException;
import com.ash.bbus.web.BusTicketBookingSystem.repository.*;
import com.ash.bbus.web.BusTicketBookingSystem.service.BookingService;
import com.ash.bbus.web.BusTicketBookingSystem.service.CancellationPolicyService;
import com.ash.bbus.web.BusTicketBookingSystem.service.WalletService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository;
    private final TripSeatRepository tripSeatRepository;
    private final UserRepository userRepository;
    private final PassengerRepository passengerRepository;
    private final CancellationRepository cancellationRepository;
    private final WalletService walletService;
    private final CancellationPolicyService cancellationPolicyService;

        public BookingServiceImpl(
            BookingRepository bookingRepository,
            TripRepository tripRepository,
            TripSeatRepository tripSeatRepository,
            UserRepository userRepository,
            PassengerRepository passengerRepository,
            CancellationRepository cancellationRepository,
            WalletService walletService,
            CancellationPolicyService cancellationPolicyService) {
        this.bookingRepository = bookingRepository;
        this.tripRepository = tripRepository;
        this.tripSeatRepository = tripSeatRepository;
        this.userRepository = userRepository;
        this.passengerRepository = passengerRepository;
        this.cancellationRepository = cancellationRepository;
        this.walletService = walletService;
        this.cancellationPolicyService = cancellationPolicyService;
    }

    @Override
    @Transactional
    public BookingDTO createBooking(BookingDTO dto, Long userId) {

        // ✅ Load user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found: " + userId
            ));

        // ✅ Load trip
        Trip trip = tripRepository.findById(dto.getTripId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Trip not found: " + dto.getTripId()
            ));

        // ✅ Validate trip is still scheduled
        if (trip.getStatus() != TripStatus.SCHEDULED) {
            throw new IllegalStateException(
                "Trip is not available for booking"
            );
        }

        // ✅ Load and validate selected seats
        List<TripSeat> seats = tripSeatRepository
            .findByIdInAndTripId(dto.getTripSeatIds(), trip.getId());

        if (seats.size() != dto.getTripSeatIds().size()) {
            throw new ResourceNotFoundException(
                "One or more seats not found for this trip"
            );
        }

        // ✅ Validate each seat is LOCKED by this user
        for (TripSeat seat : seats) {
            if (seat.getStatus() != SeatStatus.LOCKED
                    || !userId.equals(seat.getLockedByUserId())) {
                throw new SeatAlreadyBookedException(
                    "Seat not locked by current user: "
                    + seat.getSeat().getSeatNumber()
                );
            }
            if (seat.isLockExpired()) {
                throw new SeatAlreadyBookedException(
                    "Seat lock expired: "
                    + seat.getSeat().getSeatNumber()
                );
            }
        }

        // ✅ Calculate total amount
        BigDecimal totalAmount = trip.getPrice()
            .multiply(BigDecimal.valueOf(seats.size()));

        // ✅ Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTrip(trip);
        booking.setStatus(BookingStatus.PENDING);
        booking.setBookingTime(LocalDateTime.now());
        booking.setTotalAmount(totalAmount);

        Booking saved = bookingRepository.save(booking);

        // ✅ Confirm each seat and link to booking
        seats.forEach(seat -> seat.confirmBooking(saved));
        tripSeatRepository.saveAll(seats);

        // ✅ Create passengers — one per seat
        List<Passenger> passengers = new ArrayList<>();
        for (PassengerDTO pDto : dto.getPassengers()) {
            TripSeat matchedSeat = seats.stream()
                .filter(s -> s.getId().equals(pDto.getTripSeatId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Seat not found for passenger"
                ));

            Passenger passenger = new Passenger();
            passenger.setName(pDto.getName());
            passenger.setAge(pDto.getAge());
            passenger.setGender(pDto.getGender());
            passenger.setBooking(saved);
            passenger.setTripSeat(matchedSeat);
            passengers.add(passenger);
        }
        passengerRepository.saveAll(passengers);

        // ✅ Decrement available seats on trip
        trip.setAvailableSeats(
            trip.getAvailableSeats() - seats.size()
        );
        tripRepository.save(trip);

        return mapToDTO(saved, seats);
    }

    @Override
    public BookingDTO getBookingById(Long id) {
        Booking booking = findBooking(id);
        List<TripSeat> seats = tripSeatRepository
            .findByBookingId(booking.getId());
        return mapToDTO(booking, seats);
    }

    @Override
    public List<BookingDTO> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId)
            .stream()
            .map(b -> mapToDTO(b,
                tripSeatRepository.findByBookingId(b.getId())))
            .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll()
            .stream()
            .map(b -> mapToDTO(b,
                tripSeatRepository.findByBookingId(b.getId())))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingDTO cancelBooking(Long bookingId, Long userId) {

        Booking booking = findBooking(bookingId);

        // ✅ Only the booking owner can cancel
        if (!booking.getUser().getId().equals(userId)) {
            throw new IllegalStateException(
                "You are not authorized to cancel this booking"
            );
        }

        // ✅ Cannot cancel already cancelled booking
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException(
                "Booking is already cancelled"
            );
        }

        // ✅ Cannot cancel past trips
        if (booking.getTrip().getTravelDate()
                .isBefore(LocalDate.now())) {
            throw new IllegalStateException(
                "Cannot cancel a past trip booking"
            );
        }

        // ✅ Cancel booking status
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        // ✅ Release seats back to AVAILABLE
        List<TripSeat> seats = tripSeatRepository
            .findByBookingId(bookingId);
        seats.forEach(seat -> {
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setBooking(null);
        });
        tripSeatRepository.saveAll(seats);

        // ✅ Restore availableSeats on trip
        Trip trip = booking.getTrip();
        trip.setAvailableSeats(
            trip.getAvailableSeats() + seats.size()
        );
        tripRepository.save(trip);

        BigDecimal originalAmount = booking.getTotalAmount();
        String reason = "User requested";

        BigDecimal refundAmount = cancellationPolicyService.calculateRefundAmount(bookingId);

        Cancellation cancellation = Cancellation.builder()
            .booking(booking)
            .originalAmount(originalAmount)
            .refundAmount(refundAmount)
            .reason(reason)
            .refundProcessed(false)
            .build();
        cancellationRepository.save(cancellation);

        walletService.creditWallet(
            userId,
            cancellation.getRefundAmount(),
            "Refund for Booking #" + bookingId,
            bookingId
        );

        cancellation.setRefundProcessed(true);
        cancellationRepository.save(cancellation);

        return mapToDTO(booking, seats);
    }

    // ✅ Private helpers
    private Booking findBooking(Long id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Booking not found: " + id
            ));
    }

    private BookingDTO mapToDTO(Booking booking, List<TripSeat> seats) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setTripId(booking.getTrip().getId());
        dto.setUserId(booking.getUser().getId());
        dto.setUserName(booking.getUser().getName());
        dto.setStatus(booking.getStatus());
        dto.setBookingTime(booking.getBookingTime());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setSource(booking.getTrip().getRoute().getSource());
        dto.setDestination(booking.getTrip().getRoute().getDestination());
        return dto;
    }
}
