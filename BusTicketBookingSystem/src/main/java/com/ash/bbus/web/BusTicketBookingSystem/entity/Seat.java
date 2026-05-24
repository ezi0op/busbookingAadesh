package com.ash.bbus.web.BusTicketBookingSystem.entity;

import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.BerthType;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatGroup;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatPosition;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(
    name = "seat",  
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_seat_bus_seatnumber",
            columnNames = {"bus_id", "seatNumber"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String seatNumber;
    
    @Column(name="seat_row")
    private int rowNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BerthType berthType;    

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatPosition position;  

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatGroup seatGroup;    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;
}