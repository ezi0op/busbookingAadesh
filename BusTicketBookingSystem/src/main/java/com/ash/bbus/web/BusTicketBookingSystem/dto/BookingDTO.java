package com.ash.bbus.web.BusTicketBookingSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.BookingStatus;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
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

}
