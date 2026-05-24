package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.service.InvoiceService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Override
    public byte[] generateInvoice(Long bookingId) {
        // Mock PDF generation logic
        String invoiceContent = "Invoice for Booking ID: " + bookingId + "\n" +
                                "Total Amount: $100.00\n" +
                                "Status: PAID\n" +
                                "Thank you for booking with us!";
        return invoiceContent.getBytes(StandardCharsets.UTF_8);
    }
}
