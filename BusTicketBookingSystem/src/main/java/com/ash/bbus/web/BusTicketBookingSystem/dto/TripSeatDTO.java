package com.ash.bbus.web.BusTicketBookingSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.BerthType;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatGroup;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatPosition;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatStatus;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatType;

@Getter
@Setter
@NoArgsConstructor
public class TripSeatDTO {

    // ✅ FIX 1: Renamed tripSeatId → id
    // All service files use setId() / getId()
    // Keeping it as "id" makes it consistent everywhere
    private Long id;

    // ✅ FIX 2: Added tripId — needed in TripSeatServiceImpl.mapToDTO()
    private Long tripId;

    private String seatNumber;
    private int rowNumber;
    private SeatType seatType;
    private BerthType berthType;
    private SeatPosition position;
    private SeatGroup seatGroup;
    private SeatStatus status;
    private boolean available;
// ✅ id — was tripSeatId before (caused setId() errors)

    // ✅ tripId — new field, used in mapToDTO

}
