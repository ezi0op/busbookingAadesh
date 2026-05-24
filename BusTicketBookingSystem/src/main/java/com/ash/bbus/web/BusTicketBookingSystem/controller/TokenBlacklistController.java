package com.ash.bbus.web.BusTicketBookingSystem.controller;

import com.ash.bbus.web.BusTicketBookingSystem.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
public class TokenBlacklistController {

    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/blacklist")
    public ResponseEntity<String> blacklistToken(@RequestParam String token) {
        tokenBlacklistService.blacklistToken(token);
        return ResponseEntity.ok("Token blacklisted successfully");
    }

    @GetMapping("/is-blacklisted")
    public ResponseEntity<Boolean> isTokenBlacklisted(@RequestParam String token) {
        return ResponseEntity.ok(tokenBlacklistService.isTokenBlacklisted(token));
    }
}
