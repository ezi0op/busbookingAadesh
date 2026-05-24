package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.SeatType;
import com.ash.bbus.web.BusTicketBookingSystem.service.SeatPreferenceService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SeatPreferenceServiceImpl implements SeatPreferenceService {

    // Simple in-memory mock storage for preferences
    private final Map<Long, SeatType> userPreferences = new ConcurrentHashMap<>();

    @Override
    public SeatType getPreferredSeatType(Long userId) {
        return userPreferences.getOrDefault(userId, SeatType.SEATER); // Default
    }

    @Override
    public void savePreferredSeatType(Long userId, SeatType seatType) {
        userPreferences.put(userId, seatType);
    }
}
