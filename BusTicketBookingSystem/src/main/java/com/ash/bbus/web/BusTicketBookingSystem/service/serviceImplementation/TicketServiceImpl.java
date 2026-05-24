package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.dto.PassengerDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.TicketDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.TripSeatDTO;
import com.ash.bbus.web.BusTicketBookingSystem.entity.*;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatStatus;
import com.ash.bbus.web.BusTicketBookingSystem.exception.ResourceNotFoundException;
import com.ash.bbus.web.BusTicketBookingSystem.repository.*;
import com.ash.bbus.web.BusTicketBookingSystem.service.TicketService;
import com.ash.bbus.web.BusTicketBookingSystem.util.PdfUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final TripSeatRepository tripSeatRepository;
    private final PdfUtil pdfUtil;

    public TicketServiceImpl(TicketRepository ticketRepository,
                              BookingRepository bookingRepository,
                              PassengerRepository passengerRepository,
                              TripSeatRepository tripSeatRepository,
                              PdfUtil pdfUtil) {
        this.ticketRepository = ticketRepository;
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.tripSeatRepository = tripSeatRepository;
        this.pdfUtil = pdfUtil;
    }

    @Override
    @Transactional
    public TicketDTO generateTicket(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Booking not found: " + bookingId
            ));

        // ✅ Check ticket not already generated
        if (ticketRepository.existsByBookingId(bookingId)) {
            return getTicketByBookingId(bookingId);
        }

        // ✅ Generate unique PNR
        String pnr = "PNR" + UUID.randomUUID()
            .toString().replace("-", "")
            .substring(0, 8).toUpperCase();

        Ticket ticket = Ticket.builder()
            .pnrNumber(pnr)
            .booking(booking)
            .valid(true)
            .build();
        ticketRepository.save(ticket);

        return buildTicketDTO(ticket, booking);
    }

    @Override
    public TicketDTO getTicketByBookingId(Long bookingId) {
        Ticket ticket = ticketRepository.findByBookingId(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Ticket not found for booking: " + bookingId
            ));
        return buildTicketDTO(ticket, ticket.getBooking());
    }

    @Override
    public TicketDTO getTicketByPnr(String pnrNumber) {
        Ticket ticket = ticketRepository.findByPnrNumber(pnrNumber)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Ticket not found for PNR: " + pnrNumber
            ));
        return buildTicketDTO(ticket, ticket.getBooking());
    }

    @Override
    public byte[] downloadTicketPdf(Long bookingId) {
        TicketDTO ticketDTO = getTicketByBookingId(bookingId);
        return pdfUtil.generateTicketPdf(ticketDTO);
    }

    // ✅ Builds full TicketDTO from ticket + booking
    private TicketDTO buildTicketDTO(Ticket ticket, Booking booking) {
        Trip trip = booking.getTrip();
        Route route = trip.getRoute();
        Bus bus = trip.getBus();

        List<TripSeat> seats = tripSeatRepository
            .findByBookingId(booking.getId());
        List<Passenger> passengers = passengerRepository
            .findByBookingId(booking.getId());

        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setPnrNumber(ticket.getPnrNumber());
        dto.setSource(route.getSource());
        dto.setDestination(route.getDestination());
        dto.setTravelDate(trip.getTravelDate());
        dto.setDepartureTime(trip.getDepartureTime());
        dto.setArrivalTime(trip.getArrivalTime());
        dto.setBusName(bus.getName());
        dto.setBusNumber(bus.getBusNumber());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setIssuedAt(ticket.getIssuedAt());
        dto.setValid(ticket.isValid());

        dto.setPassengers(passengers.stream().map(p -> {
            PassengerDTO pDto = new PassengerDTO();
            pDto.setId(p.getId());
            pDto.setName(p.getName());
            pDto.setAge(p.getAge());
            pDto.setGender(p.getGender());
            pDto.setTripSeatId(p.getTripSeat().getId());
            return pDto;
        }).collect(Collectors.toList()));

        dto.setSeats(seats.stream().map(s -> {
            TripSeatDTO sDto = new TripSeatDTO();

            // ✅ was setTripSeatId() → now setId()
            sDto.setId(s.getId());
            sDto.setTripId(s.getTrip().getId());         // ✅ new field
            sDto.setSeatNumber(s.getSeat().getSeatNumber());
            sDto.setRowNumber(s.getSeat().getRowNumber());
            sDto.setBerthType(s.getSeat().getBerthType());
            sDto.setPosition(s.getSeat().getPosition());
            sDto.setSeatGroup(s.getSeat().getSeatGroup());
            sDto.setStatus(SeatStatus.BOOKED);
            sDto.setAvailable(false);
            return sDto;
        }).collect(Collectors.toList()));

        return dto;
    }
}