package com.ash.bbus.web.BusTicketBookingSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.BusType;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
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

}
