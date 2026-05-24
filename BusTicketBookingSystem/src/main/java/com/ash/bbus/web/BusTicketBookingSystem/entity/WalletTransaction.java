package com.ash.bbus.web.BusTicketBookingSystem.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.TransactionType;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "wallet_transaction")  // ✅ FIX 1: explicit table name
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ FIX 2: precision + scale + nullable=false
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    // ✅ FIX 3: balanceAfter — wallet balance snapshot after this transaction
    // Critical for financial audit trail and dispute resolution
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal balanceAfter;

    // ✅ FIX 4: nullable=false on type
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;   // CREDIT / DEBIT

    // Description is optional but useful
    private String description;

    // ✅ FIX 5: referenceId — links transaction to a Booking
    // e.g., bookingId=88 → "Refund for Booking #88"
    private Long referenceId;

    // ✅ FIX 6: auto-set via @PrePersist, updatable=false — never changes
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    // ✅ FIX 7: FetchType.LAZY + nullable=false on JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;


    // ✅ FIX 8: Auto-set createdAt — never set manually in service
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}