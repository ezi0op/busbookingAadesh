package com.ash.bbus.web.BusTicketBookingSystem.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "trip_seat",   // ✅ FIX 1: explicit table name
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_tripseat_trip_seat",
            columnNames = {"trip_id", "seat_id"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ FIX 2: FetchType.LAZY on both — prevents eager chain loading
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    // ✅ FIX 3: Replaced raw Long bookingId with proper @ManyToOne
    // TripSeat belongs to a Booking (nullable — null when seat is AVAILABLE/LOCKED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = true)
    private Booking booking;

    // ✅ FIX 4: nullable=false — every TripSeat must have a status from creation
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status;

    // Seat locking fields
    private Long lockedByUserId;

    private LocalDateTime lockTime;

    // ✅ FIX 5: lockExpiryTime — seat auto-released after 10 minutes
    // Scheduler checks this and resets status to AVAILABLE
    private LocalDateTime lockExpiryTime;

    // ✅ Optimistic locking — prevents double booking race condition
    @Version
    private int version;

    // Passengers linked to this specific seat
    @OneToMany(mappedBy = "tripSeat", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Passenger> passengers = new ArrayList<>();


    // ✅ Helper method — lock this seat for a user for 10 minutes
    public void lockSeat(Long userId) {
        this.lockedByUserId = userId;
        this.lockTime = LocalDateTime.now();
        this.lockExpiryTime = LocalDateTime.now().plusMinutes(10);
        this.status = SeatStatus.LOCKED;
    }

    // ✅ Helper method — release lock, make available again
    public void releaseLock() {
        this.lockedByUserId = null;
        this.lockTime = null;
        this.lockExpiryTime = null;
        this.status = SeatStatus.AVAILABLE;
    }

    // ✅ Helper method — confirm booking on this seat
    public void confirmBooking(Booking booking) {
        this.booking = booking;
        this.status = SeatStatus.BOOKED;
        this.lockedByUserId = null;
        this.lockTime = null;
        this.lockExpiryTime = null;
    }

    // ✅ Helper method — check if lock has expired
    public boolean isLockExpired() {
        return lockExpiryTime != null &&
               LocalDateTime.now().isAfter(lockExpiryTime);
    }
}