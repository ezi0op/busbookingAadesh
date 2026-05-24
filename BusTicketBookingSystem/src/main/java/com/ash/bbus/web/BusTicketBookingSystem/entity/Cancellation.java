package com.ash.bbus.web.BusTicketBookingSystem.entity;

import java.math.BigDecimal;
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
@Table(name = "cancellation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cancellation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which booking was cancelled
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    // When cancellation was requested
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime cancelledAt;

    // Original booking amount
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal originalAmount;

    // 50% refund amount credited to wallet
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal refundAmount;

    // Reason provided by user (optional)
    private String reason;

    // Was refund successfully credited to wallet?
    @Column(nullable = false)
    private boolean refundProcessed = false;



    @PrePersist
    protected void onCreate() {
        this.cancelledAt = LocalDateTime.now();
    }
}
