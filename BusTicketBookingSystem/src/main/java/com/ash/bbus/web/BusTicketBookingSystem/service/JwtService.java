package com.ash.bbus.web.BusTicketBookingSystem.service;

import org.springframework.stereotype.Service;

import com.ash.bbus.web.BusTicketBookingSystem.entity.User;

@Service
public class JwtService {

    public String generateToken(User user) {
        // simple demo (later enhance)
        return "TOKEN_" + user.getEmail();
    }
}
