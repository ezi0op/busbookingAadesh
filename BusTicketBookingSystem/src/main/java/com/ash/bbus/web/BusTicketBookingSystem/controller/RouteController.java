package com.ash.bbus.web.BusTicketBookingSystem.controller;

import com.ash.bbus.web.BusTicketBookingSystem.dto.ApiResponse;
import com.ash.bbus.web.BusTicketBookingSystem.dto.RouteDTO;
import com.ash.bbus.web.BusTicketBookingSystem.service.RouteService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    // ✅ POST /api/routes — ADMIN only
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RouteDTO>> addRoute(
            @Valid @RequestBody RouteDTO dto) {

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success("Route added",
                routeService.addRoute(dto)));
    }

    // ✅ GET /api/routes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RouteDTO>> getRouteById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
            ApiResponse.success("Route fetched",
                routeService.getRouteById(id))
        );
    }

    // ✅ GET /api/routes
    @GetMapping
    public ResponseEntity<ApiResponse<List<RouteDTO>>> getAllRoutes() {

        return ResponseEntity.ok(
            ApiResponse.success("All routes fetched",
                routeService.getAllRoutes())
        );
    }

    // ✅ PUT /api/routes/{id} — ADMIN only
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RouteDTO>> updateRoute(
            @PathVariable Long id,
            @Valid @RequestBody RouteDTO dto) {

        return ResponseEntity.ok(
            ApiResponse.success("Route updated",
                routeService.updateRoute(id, dto))
        );
    }

    // ✅ DELETE /api/routes/{id} — ADMIN only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRoute(
            @PathVariable Long id) {

        routeService.deleteRoute(id);
        return ResponseEntity.ok(ApiResponse.success("Route deleted"));
    }
}