package com.ash.bbus.web.BusTicketBookingSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.BerthType;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatGroup;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatPosition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class SeatDTO {

    private Long id;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @NotNull(message = "Row number is required")
    private int rowNumber;

    @NotNull(message = "Berth type is required")
    private BerthType berthType;

    @NotNull(message = "Position is required")
    private SeatPosition position;

    @NotNull(message = "Seat group is required")
    private SeatGroup seatGroup;

    private Long busId;

}
