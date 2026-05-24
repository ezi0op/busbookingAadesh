package com.ash.bbus.web.BusTicketBookingSystem.dto;

import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.BusType;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BusDTO {

    private Long id;

    @NotBlank(message = "Bus name is required")
    private String name;

    @NotBlank(message = "Bus number is required")
    private String busNumber;

    @NotNull(message = "Bus type is required")
    private BusType busType;

    @NotNull(message = "Seat type is required")
    private SeatType seatType;

    @Min(value = 1, message = "Total seats must be at least 1")
    private int totalSeats;

    public BusDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBusNumber() { return busNumber; }
    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }

    public BusType getBusType() { return busType; }
    public void setBusType(BusType busType) { this.busType = busType; }

    public SeatType getSeatType() { return seatType; }
    public void setSeatType(SeatType seatType) { this.seatType = seatType; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
}