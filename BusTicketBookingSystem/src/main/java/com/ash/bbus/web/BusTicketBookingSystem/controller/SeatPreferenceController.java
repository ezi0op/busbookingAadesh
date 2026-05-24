package com.ash.bbus.web.BusTicketBookingSystem.controller;

import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatType;
import com.ash.bbus.web.BusTicketBookingSystem.service.SeatPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seat-preferences")
@RequiredArgsConstructor
public class SeatPreferenceController {

    private final SeatPreferenceService seatPreferenceService;

    @GetMapping("/{userId}")
    public ResponseEntity<SeatType> getPreferredSeatType(@PathVariable Long userId) {
        return ResponseEntity.ok(seatPreferenceService.getPreferredSeatType(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> savePreferredSeatType(@PathVariable Long userId, @RequestParam SeatType seatType) {
        seatPreferenceService.savePreferredSeatType(userId, seatType);
        return ResponseEntity.ok("Preference saved successfully");
    }
}
