package com.ash.bbus.web.BusTicketBookingSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
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

}
