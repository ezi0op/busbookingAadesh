package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.service.QRCodeService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class QRCodeServiceImpl implements QRCodeService {

    @Override
    public byte[] generateQRCodeForBooking(Long bookingId) {
        // Mock QR code generation: Just return the booking ID as bytes
        String mockQRData = "QR_CODE_FOR_BOOKING_" + bookingId;
        return mockQRData.getBytes(StandardCharsets.UTF_8);
    }
}
