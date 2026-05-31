package com.ash.bbus.web.BusTicketBookingSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.PaymentMethod;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class PaymentDTO {

    private Long id;

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod method;

    // ✅ Response fields
    private BigDecimal amount;
    private BigDecimal refundAmount;
    private PaymentStatus status;
    private String transactionId;
    private String gatewayOrderId;
    private String failureReason;
    private LocalDateTime createdAt;

}
