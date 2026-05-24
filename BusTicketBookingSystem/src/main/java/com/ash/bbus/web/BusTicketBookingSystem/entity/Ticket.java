package com.ash.bbus.web.BusTicketBookingSystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique PNR number shown on ticket — e.g. "PNR8473920"
    @Column(nullable = false, unique = true)
    private String pnrNumber;

    // Links ticket to booking
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    // Path where PDF is stored — e.g. "/tickets/PNR8473920.pdf"
    // Null until PDF is generated
    private String pdfPath;

    // When ticket was issued
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime issuedAt;

    // Is ticket still valid (false if booking cancelled)
    @Column(nullable = false)
    @Builder.Default
    private boolean valid = true;

    @PrePersist
    protected void onCreate() {
        this.issuedAt = LocalDateTime.now();
    }

    // Mark ticket invalid on cancellation
    public void invalidate() {
        this.valid = false;
    }
}
