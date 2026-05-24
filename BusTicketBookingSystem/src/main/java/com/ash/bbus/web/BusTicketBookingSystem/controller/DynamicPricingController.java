package com.ash.bbus.web.BusTicketBookingSystem.controller;

import com.ash.bbus.web.BusTicketBookingSystem.service.DynamicPricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/pricing")
@RequiredArgsConstructor
public class DynamicPricingController {

    private final DynamicPricingService dynamicPricingService;

    @GetMapping("/dynamic")
    public ResponseEntity<BigDecimal> getDynamicPrice(@RequestParam Long tripId, @RequestParam BigDecimal basePrice) {
        return ResponseEntity.ok(dynamicPricingService.calculateDynamicPrice(tripId, basePrice));
    }
}
