package com.ash.bbus.web.BusTicketBookingSystem.service;

import java.math.BigDecimal;

public interface CancellationPolicyService {
    BigDecimal calculateRefundAmount(Long bookingId);
}
