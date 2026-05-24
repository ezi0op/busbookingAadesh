package com.ash.bbus.web.BusTicketBookingSystem.controller;

import com.ash.bbus.web.BusTicketBookingSystem.dto.ApiResponse;
import com.ash.bbus.web.BusTicketBookingSystem.dto.TripDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.TripSeatDTO;
import com.ash.bbus.web.BusTicketBookingSystem.entity.User;
import com.ash.bbus.web.BusTicketBookingSystem.service.TripService;
import com.ash.bbus.web.BusTicketBookingSystem.service.TripSeatService;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation
    .AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService     tripService;
    private final TripSeatService tripSeatService;

    public TripController(
            TripService tripService,
            TripSeatService tripSeatService) {
        this.tripService     = tripService;
        this.tripSeatService = tripSeatService;
    }

    // ✅ POST /api/trips — ADMIN only
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TripDTO>>
            createTrip(@Valid @RequestBody TripDTO dto) {

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success("Trip created",
                tripService.createTrip(dto)));
    }

    // ✅ GET /api/trips/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TripDTO>>
            getTripById(@PathVariable Long id) {

        return ResponseEntity.ok(
            ApiResponse.success("Trip fetched",
                tripService.getTripById(id))
        );
    }

    // ✅ GET /api/trips
    @GetMapping
    public ResponseEntity<ApiResponse<List<TripDTO>>>
            getAllTrips() {

        return ResponseEntity.ok(
            ApiResponse.success("All trips fetched",
                tripService.getAllTrips())
        );
    }

    // ✅ GET /api/trips/search
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TripDTO>>>
            searchTrips(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam
            @DateTimeFormat(iso =
                DateTimeFormat.ISO.DATE) LocalDate date) {

        return ResponseEntity.ok(
            ApiResponse.success("Trips found",
                tripService.searchTrips(
                    source, destination, date))
        );
    }

    // ✅ GET /api/trips/{id}/seats
    @GetMapping("/{id}/seats")
    public ResponseEntity<ApiResponse<List<TripSeatDTO>>>
            getTripSeats(@PathVariable Long id) {

        return ResponseEntity.ok(
            ApiResponse.success("Seats fetched",
                tripSeatService.getSeatsByTrip(id))
        );
    }

    // ✅ FIX: POST /api/trips/{tripId}/seats/lock
    // This was MISSING — SeatPage calls lockSeats()
    @PostMapping("/{tripId}/seats/lock")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<TripSeatDTO>>>
            lockSeats(
            @PathVariable Long tripId,
            @RequestBody Map<String, List<Long>> body,
            @AuthenticationPrincipal User currentUser) {

        // ✅ Frontend sends { seatIds: [1, 2, 3] }
        List<Long> seatIds = body.get("seatIds");

        return ResponseEntity.ok(
            ApiResponse.success("Seats locked",
                tripSeatService.lockSeats(
                    tripId, seatIds,
                    currentUser.getId()))
        );
    }

    // ✅ PUT /api/trips/{id} — ADMIN only
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TripDTO>>
            updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody TripDTO dto) {

        return ResponseEntity.ok(
            ApiResponse.success("Trip updated",
                tripService.updateTrip(id, dto))
        );
    }

    // ✅ PUT /api/trips/{id}/cancel — ADMIN only
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>>
            cancelTrip(@PathVariable Long id) {

        tripService.cancelTrip(id);
        return ResponseEntity.ok(
            ApiResponse.success("Trip cancelled")
        );
    }
}