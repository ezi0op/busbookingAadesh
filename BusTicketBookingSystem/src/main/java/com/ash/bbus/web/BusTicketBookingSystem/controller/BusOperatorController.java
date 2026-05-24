package com.ash.bbus.web.BusTicketBookingSystem.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ash.bbus.web.BusTicketBookingSystem.entity.BusOperator;
import com.ash.bbus.web.BusTicketBookingSystem.entity.Route;
import com.ash.bbus.web.BusTicketBookingSystem.service.BusOperatorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/operators")
@RequiredArgsConstructor
public class BusOperatorController {

    private final BusOperatorService busOperatorService;

    @PostMapping
    public ResponseEntity<BusOperator> createOperator(@RequestBody BusOperator operator) {
        return ResponseEntity.ok(busOperatorService.createOperator(operator));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusOperator> getOperatorById(@PathVariable Long id) {
        return ResponseEntity.ok(busOperatorService.getOperatorById(id));
    }

    @GetMapping
    public ResponseEntity<List<BusOperator>> getAllOperators() {
        return ResponseEntity.ok(busOperatorService.getAllOperators());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusOperator> updateOperator(@PathVariable Long id, @RequestBody BusOperator operator) {
        return ResponseEntity.ok(busOperatorService.updateOperator(id, operator));
    }

    @DeleteMapping("/{id}/deactivate")
    public ResponseEntity<BusOperator> deactivateOperator(@PathVariable Long id) {
        return ResponseEntity.ok(busOperatorService.deactivateOperator(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BusOperator>> searchOperators(@RequestParam String name) {
        return ResponseEntity.ok(busOperatorService.searchOperators(name));
    }

    @GetMapping("/bus/{busId}/routes")
    public ResponseEntity<List<Route>> getRoutesByBusId(@PathVariable Long busId) {
        return ResponseEntity.ok(busOperatorService.getRoutes(busId));
    }
}
