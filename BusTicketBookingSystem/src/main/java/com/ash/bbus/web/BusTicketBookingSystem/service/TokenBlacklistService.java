package com.ash.bbus.web.BusTicketBookingSystem.service;

public interface TokenBlacklistService {
    void blacklistToken(String token);
    boolean isTokenBlacklisted(String token);
}
