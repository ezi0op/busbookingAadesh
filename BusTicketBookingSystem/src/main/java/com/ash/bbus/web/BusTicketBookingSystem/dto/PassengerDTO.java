package com.ash.bbus.web.BusTicketBookingSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.Gender;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class PassengerDTO {

    private Long id;

    @NotBlank(message = "Passenger name is required")
    private String name;

    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 120, message = "Age cannot exceed 120")
    private int age;

    @NotNull(message = "Gender is required")
    private Gender gender;

    // Which TripSeat this passenger occupies
    @NotNull(message = "Trip seat ID is required")
    private Long tripSeatId;  // references TripSeat.id

}
