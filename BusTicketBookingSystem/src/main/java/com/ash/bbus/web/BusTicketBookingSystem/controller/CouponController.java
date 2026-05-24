package com.ash.bbus.web.BusTicketBookingSystem.controller;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Coupon;
import com.ash.bbus.web.BusTicketBookingSystem.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        return ResponseEntity.ok(couponService.createCoupon(coupon));
    }

    @PutMapping("/{couponId}")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable Long couponId, @RequestBody Coupon coupon) {
        return ResponseEntity.ok(couponService.updateCoupon(couponId, coupon));
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @PostMapping("/apply")
    public ResponseEntity<BigDecimal> applyCoupon(@RequestParam String couponCode, @RequestParam BigDecimal bookingAmount) {
        return ResponseEntity.ok(couponService.applyCoupon(couponCode, bookingAmount));
    }
}
