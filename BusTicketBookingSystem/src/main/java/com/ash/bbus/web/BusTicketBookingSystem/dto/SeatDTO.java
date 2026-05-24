package com.ash.bbus.web.BusTicketBookingSystem.dto;

import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.BerthType;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatGroup;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatPosition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

    public SeatDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public int getRowNumber() { return rowNumber; }
    public void setRowNumber(int rowNumber) { this.rowNumber = rowNumber; }

    public BerthType getBerthType() { return berthType; }
    public void setBerthType(BerthType berthType) { this.berthType = berthType; }

    public SeatPosition getPosition() { return position; }
    public void setPosition(SeatPosition position) { this.position = position; }

    public SeatGroup getSeatGroup() { return seatGroup; }
    public void setSeatGroup(SeatGroup seatGroup) { this.seatGroup = seatGroup; }

    public Long getBusId() { return busId; }
    public void setBusId(Long busId) { this.busId = busId; }
}