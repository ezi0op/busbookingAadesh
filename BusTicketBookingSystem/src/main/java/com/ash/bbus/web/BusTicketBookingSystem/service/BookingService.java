package com.ash.bbus.web.BusTicketBookingSystem.service;

import com.ash.bbus.web.BusTicketBookingSystem.dto.BookingDTO;
import java.util.List;

public interface BookingService {

    BookingDTO createBooking(BookingDTO dto, Long userId);
    BookingDTO getBookingById(Long id);
    List<BookingDTO> getBookingsByUser(Long userId);
    List<BookingDTO> getAllBookings();
    BookingDTO cancelBooking(Long bookingId, Long userId);
}