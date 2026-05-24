package com.ash.bbus.web.BusTicketBookingSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.TransactionType;

public class WalletTransactionDTO {

    private Long id;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private TransactionType type;
    private String description;
    private Long referenceId;
    private LocalDateTime createdAt;

    public WalletTransactionDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}