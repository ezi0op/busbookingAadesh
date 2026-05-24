package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.service.DynamicPricingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class DynamicPricingServiceImpl implements DynamicPricingService {

    @Override
    public BigDecimal calculateDynamicPrice(Long tripId, BigDecimal basePrice) {
        // Mock dynamic pricing logic: 20% increase during peak hours or close to departure
        int hour = LocalDateTime.now().getHour();
        if ((hour >= 18 && hour <= 21) || (hour >= 8 && hour <= 10)) {
            return basePrice.multiply(new BigDecimal("1.20"));
        }
        return basePrice;
    }
}
