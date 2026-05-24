package com.ash.bbus.web.BusTicketBookingSystem.controller;

import com.ash.bbus.web.BusTicketBookingSystem.dto.ApiResponse;
import com.ash.bbus.web.BusTicketBookingSystem.dto.BookingDTO;
import com.ash.bbus.web.BusTicketBookingSystem.entity.User;
import com.ash.bbus.web.BusTicketBookingSystem.service.BookingService;
import com.ash.bbus.web.BusTicketBookingSystem.service.EmailService;
import com.ash.bbus.web.BusTicketBookingSystem.service.TicketService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	private final BookingService bookingService;
	private final TicketService ticketService;
	private final EmailService emailService;

	public BookingController(BookingService bookingService, TicketService ticketService, EmailService emailService) {
		this.bookingService = bookingService;
		this.ticketService = ticketService;
		this.emailService = emailService;
	}

	// ✅ POST /api/bookings — create booking
	@PostMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<ApiResponse<BookingDTO>> createBooking(@Valid @RequestBody BookingDTO dto,
			@AuthenticationPrincipal User currentUser) {

		BookingDTO booking = bookingService.createBooking(dto, currentUser.getId());

		// ✅ Send confirmation email async
		try {
			String pnr = ticketService.getTicketByBookingId(booking.getId()).getPnrNumber();
			emailService.sendBookingConfirmation(currentUser.getEmail(), pnr, booking.getSource(),
					booking.getDestination());
		} catch (Exception e) {
			// Email failure should not break booking
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Booking confirmed", booking));
	}

	// ✅ GET /api/bookings/{id}
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<ApiResponse<BookingDTO>> getBookingById(@PathVariable Long id) {

		return ResponseEntity.ok(ApiResponse.success("Booking fetched", bookingService.getBookingById(id)));
	}

	// ✅ GET /api/bookings/my — logged in user's bookings
	@GetMapping("/my")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<ApiResponse<List<BookingDTO>>> getMyBookings(@AuthenticationPrincipal User currentUser) {

		return ResponseEntity
				.ok(ApiResponse.success("Your bookings", bookingService.getBookingsByUser(currentUser.getId())));
	}

	// ✅ FIX: GET /api/bookings — ADMIN only
	// This was MISSING — AdminBookings page needs this
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<BookingDTO>>> getAllBookings() {

		return ResponseEntity.ok(ApiResponse.success("All bookings", bookingService.getAllBookings()));
	}

	// ✅ PUT /api/bookings/{id}/cancel
	@PutMapping("/{id}/cancel")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<ApiResponse<BookingDTO>> cancelBooking(@PathVariable Long id,
			@AuthenticationPrincipal User currentUser) {

		BookingDTO cancelled = bookingService.cancelBooking(id, currentUser.getId());

		// ✅ Send cancellation email async
		try {
			String pnr = ticketService.getTicketByBookingId(id).getPnrNumber();
			emailService.sendCancellationConfirmation(currentUser.getEmail(), pnr,
					cancelled.getTotalAmount().divide(new java.math.BigDecimal("2")).toString());
		} catch (Exception e) {
			// Email failure should not break cancellation
		}

		return ResponseEntity.ok(ApiResponse.success("Booking cancelled", cancelled));
	}
}