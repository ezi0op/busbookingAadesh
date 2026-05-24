package com.ash.bbus.web.BusTicketBookingSystem.service;

public interface InvoiceService {
    byte[] generateInvoice(Long bookingId);
}
