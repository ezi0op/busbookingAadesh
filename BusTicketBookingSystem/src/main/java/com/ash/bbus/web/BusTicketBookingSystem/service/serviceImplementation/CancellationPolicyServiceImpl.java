package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Booking;
import com.ash.bbus.web.BusTicketBookingSystem.repository.BookingRepository;
import com.ash.bbus.web.BusTicketBookingSystem.service.CancellationPolicyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CancellationPolicyServiceImpl implements CancellationPolicyService {

	private final BookingRepository bookingRepository;

	@Override
	public BigDecimal calculateRefundAmount(Long bookingId) {

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		/*
		 * Calculate time difference between now and journey departure
		 */
		LocalDateTime now = LocalDateTime.now();

		LocalDateTime journeyDateTime = LocalDateTime.of(booking.getTrip().getTravelDate(),
				booking.getTrip().getDepartureTime());

		long hoursBeforeJourney = Duration.between(now, journeyDateTime).toHours();

		BigDecimal finalAmount = booking.getTotalAmount();

		/*
		 * Cancellation Policy Rules
		 *
		 * >= 24 hrs → 100% refund 
		 * >= 12 hrs → 70% refund 
		 * >= 4 hrs  → 40% refund
		 * < 4 hrs   → No refund
		 */

		if (hoursBeforeJourney >= 24) {
			return finalAmount;
		}

		if (hoursBeforeJourney >= 12) {
			return finalAmount.multiply(new BigDecimal("0.70"));
		}

		if (hoursBeforeJourney >= 4) {
			return finalAmount.multiply(new BigDecimal("0.40"));
		}

		return BigDecimal.ZERO;
	}

}
