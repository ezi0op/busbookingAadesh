package com.ash.bbus.web.BusTicketBookingSystem.controller;

import com.ash.bbus.web.BusTicketBookingSystem.dto.ApiResponse;
import com.ash.bbus.web.BusTicketBookingSystem.dto.BusDTO;
import com.ash.bbus.web.BusTicketBookingSystem.service.BusService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buses")
public class BusController {

    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    // ✅ POST /api/buses — ADMIN only
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BusDTO>> addBus(
            @Valid @RequestBody BusDTO dto) {

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success("Bus added", busService.addBus(dto)));
    }

    // ✅ GET /api/buses/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BusDTO>> getBusById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
            ApiResponse.success("Bus fetched", busService.getBusById(id))
        );
    }

    // ✅ GET /api/buses
    @GetMapping
    public ResponseEntity<ApiResponse<List<BusDTO>>> getAllBuses() {

        return ResponseEntity.ok(
            ApiResponse.success("All buses fetched",
                busService.getAllBuses())
        );
    }

    // ✅ PUT /api/buses/{id} — ADMIN only
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BusDTO>> updateBus(
            @PathVariable Long id,
            @Valid @RequestBody BusDTO dto) {

        return ResponseEntity.ok(
            ApiResponse.success("Bus updated",
                busService.updateBus(id, dto))
        );
    }

    // ✅ DELETE /api/buses/{id} — ADMIN only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteBus(
            @PathVariable Long id) {

        busService.deleteBus(id);
        return ResponseEntity.ok(ApiResponse.success("Bus deleted"));
    }
}