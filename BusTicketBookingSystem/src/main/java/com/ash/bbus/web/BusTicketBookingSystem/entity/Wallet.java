package com.ash.bbus.web.BusTicketBookingSystem.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "wallet")  // ✅ FIX 1: explicit table name
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ FIX 2: precision + scale for exact money storage
    // Supports up to ₹9,999,999,999.99
    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    // ✅ FIX 3: FetchType.LAZY — @OneToOne is EAGER by default
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // ✅ FIX 4: Transaction history
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<WalletTransaction> transactions = new ArrayList<>();

    // ✅ Optimistic locking — prevents concurrent balance corruption
    @Version
    private int version;

    // ✅ FIX 5: wallet creation timestamp
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;



    // ✅ Auto-set createdAt on first save
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        // ✅ Safety: ensure balance is never null on persist
        if (this.balance == null) {
            this.balance = BigDecimal.ZERO;
        }
    }

    // =========================================================
    // ✅ FIX 6: Helper methods — centralized debit/credit logic
    // =========================================================

    // Credit wallet — used when: refund is issued, wallet top-up
    public void credit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "Credit amount must be greater than zero"
            );
        }
        this.balance = this.balance.add(amount);
    }

    // Debit wallet — used when: payment via wallet
    public void debit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "Debit amount must be greater than zero"
            );
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException(
                "Insufficient wallet balance"
            );
        }
        this.balance = this.balance.subtract(amount);
    }

    // Check if wallet has enough balance
    public boolean hasSufficientBalance(BigDecimal amount) {
        return this.balance.compareTo(amount) >= 0;
    }
}