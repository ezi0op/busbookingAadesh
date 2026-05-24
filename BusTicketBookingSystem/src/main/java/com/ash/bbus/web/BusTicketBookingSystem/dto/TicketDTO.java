package com.ash.bbus.web.BusTicketBookingSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class TicketDTO {

    private Long id;
    private String pnrNumber;

    // Trip info
    private String source;
    private String destination;
    private LocalDate travelDate;
    private LocalTime departureTime;
    private LocalTime arrivalTime;

    // Bus info
    private String busName;
    private String busNumber;

    // Passenger + seat details
    private List<PassengerDTO> passengers;
    private List<TripSeatDTO> seats;

    // Payment info
    private BigDecimal totalAmount;
    private String paymentMethod;

    // Ticket meta
    private LocalDateTime issuedAt;
    private boolean valid;

    public TicketDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPnrNumber() { return pnrNumber; }
    public void setPnrNumber(String pnrNumber) { this.pnrNumber = pnrNumber; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getTravelDate() { return travelDate; }
    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate;
    }

    public LocalTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getBusName() { return busName; }
    public void setBusName(String busName) { this.busName = busName; }

    public String getBusNumber() { return busNumber; }
    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }

    public List<PassengerDTO> getPassengers() { return passengers; }
    public void setPassengers(List<PassengerDTO> passengers) {
        this.passengers = passengers;
    }

    public List<TripSeatDTO> getSeats() { return seats; }
    public void setSeats(List<TripSeatDTO> seats) { this.seats = seats; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
}