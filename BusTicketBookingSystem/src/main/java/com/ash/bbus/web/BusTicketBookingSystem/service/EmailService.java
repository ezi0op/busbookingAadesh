package com.ash.bbus.web.BusTicketBookingSystem.service;

import com.ash.bbus.web.BusTicketBookingSystem.dto.EmailDTO;

public interface EmailService {

    // ✅ Generic send — used internally by all specific methods
    void sendEmail(EmailDTO emailDTO);

    // ✅ Specific email methods — build EmailDTO internally
    void sendBookingConfirmation(String toEmail, String pnrNumber,
                                 String source, String destination);

    void sendCancellationConfirmation(String toEmail, String pnrNumber,
                                      String refundAmount);

    void sendWelcomeEmail(String toEmail, String name);

    // ✅ New — send ticket PDF as email attachment
    void sendTicketWithAttachment(String toEmail, String pnrNumber,
                                   byte[] pdfBytes);
}