package com.ash.bbus.web.BusTicketBookingSystem.service;

import java.math.BigDecimal;

public interface DynamicPricingService {
    BigDecimal calculateDynamicPrice(Long tripId, BigDecimal basePrice);
}
