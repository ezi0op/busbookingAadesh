package com.ash.bbus.web.BusTicketBookingSystem.controller;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Trip;
import com.ash.bbus.web.BusTicketBookingSystem.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping
    public ResponseEntity<List<Trip>> getRecommendations(@RequestParam Long userId, @RequestParam String destination) {
        return ResponseEntity.ok(recommendationService.recommendTrips(userId, destination));
    }
}
