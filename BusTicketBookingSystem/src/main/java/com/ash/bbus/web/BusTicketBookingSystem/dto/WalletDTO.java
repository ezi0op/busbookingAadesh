package com.ash.bbus.web.BusTicketBookingSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WalletDTO {

    private Long id;
    private BigDecimal balance;
    private Long userId;
    private String userName;
    private LocalDateTime createdAt;

    public WalletDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}