package com.ash.bbus.web.BusTicketBookingSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.BookingStatus;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class BookingDTO {

    private Long id;

    // ✅ Request fields
    @NotNull(message = "Trip ID is required")
    private Long tripId;

    @NotEmpty(message = "At least one seat must be selected")
    private List<Long> tripSeatIds;

    @NotEmpty(message = "Passenger details are required")
    private List<PassengerDTO> passengers;

    // ✅ Response fields
    private Long userId;
    private String userName;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private LocalDateTime bookingTime;
    private String source;
    private String destination;
    private List<TripSeatDTO> bookedSeats;

    public BookingDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }

    public List<Long> getTripSeatIds() { return tripSeatIds; }
    public void setTripSeatIds(List<Long> tripSeatIds) {
        this.tripSeatIds = tripSeatIds;
    }

    public List<PassengerDTO> getPassengers() { return passengers; }
    public void setPassengers(List<PassengerDTO> passengers) {
        this.passengers = passengers;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<TripSeatDTO> getBookedSeats() { return bookedSeats; }
    public void setBookedSeats(List<TripSeatDTO> bookedSeats) {
        this.bookedSeats = bookedSeats;
    }
}