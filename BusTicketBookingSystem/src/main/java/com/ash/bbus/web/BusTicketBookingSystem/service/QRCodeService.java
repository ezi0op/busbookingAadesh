package com.ash.bbus.web.BusTicketBookingSystem.service;

public interface QRCodeService {
    byte[] generateQRCodeForBooking(Long bookingId);
}
