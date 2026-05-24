package com.ash.bbus.web.BusTicketBookingSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;

public class CancellationDTO {

    private Long id;

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    // Optional reason from user
    private String reason;

    // ✅ Response fields
    private BigDecimal originalAmount;
    private BigDecimal refundAmount;
    private LocalDateTime cancelledAt;
    private boolean refundProcessed;

    public CancellationDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public BigDecimal getOriginalAmount() { return originalAmount; }
    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public boolean isRefundProcessed() { return refundProcessed; }
    public void setRefundProcessed(boolean refundProcessed) {
        this.refundProcessed = refundProcessed;
    }
}