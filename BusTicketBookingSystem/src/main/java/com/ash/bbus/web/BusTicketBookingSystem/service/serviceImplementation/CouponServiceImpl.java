package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Coupon;
import com.ash.bbus.web.BusTicketBookingSystem.repository.CouponRepository;
import com.ash.bbus.web.BusTicketBookingSystem.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Override
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public Coupon updateCoupon(Long couponId, Coupon coupon) {
        Coupon existingCoupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
        
        existingCoupon.setCouponCode(coupon.getCouponCode());
        existingCoupon.setDescription(coupon.getDescription());
        existingCoupon.setDiscountAmount(coupon.getDiscountAmount());
        existingCoupon.setMinimumBookingAmount(coupon.getMinimumBookingAmount());
        existingCoupon.setIsActive(coupon.getIsActive());
        existingCoupon.setExpiryDate(coupon.getExpiryDate());
        
        return couponRepository.save(existingCoupon);
    }

    @Override
    public void deleteCoupon(Long couponId) {
        if (!couponRepository.existsById(couponId)) {
            throw new RuntimeException("Coupon not found");
        }
        couponRepository.deleteById(couponId);
    }

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public BigDecimal applyCoupon(String couponCode, BigDecimal bookingAmount) {
        Coupon coupon = couponRepository.findByCouponCode(couponCode)
                .orElseThrow(() -> new RuntimeException("Invalid coupon code"));

        if (!coupon.getIsActive() || (coupon.getExpiryDate() != null && coupon.getExpiryDate().isBefore(LocalDateTime.now()))) {
            throw new RuntimeException("Coupon is expired or inactive");
        }

        if (bookingAmount.compareTo(coupon.getMinimumBookingAmount()) < 0) {
            throw new RuntimeException("Booking amount is less than minimum required for this coupon");
        }

        BigDecimal discountedAmount = bookingAmount.subtract(coupon.getDiscountAmount());
        if (discountedAmount.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return discountedAmount;
    }
}
