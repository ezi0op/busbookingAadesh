package com.ash.bbus.web.BusTicketBookingSystem.dto;

import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.BerthType;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatGroup;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatPosition;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatStatus;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatType;

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

    public TripSeatDTO() {}

    // ✅ id — was tripSeatId before (caused setId() errors)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // ✅ tripId — new field, used in mapToDTO
    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getRowNumber() { return rowNumber; }
    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public SeatType getSeatType() { return seatType; }
    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    public BerthType getBerthType() { return berthType; }
    public void setBerthType(BerthType berthType) {
        this.berthType = berthType;
    }

    public SeatPosition getPosition() { return position; }
    public void setPosition(SeatPosition position) {
        this.position = position;
    }

    public SeatGroup getSeatGroup() { return seatGroup; }
    public void setSeatGroup(SeatGroup seatGroup) {
        this.seatGroup = seatGroup;
    }

    public SeatStatus getStatus() { return status; }
    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) {
        this.available = available;
    }
}