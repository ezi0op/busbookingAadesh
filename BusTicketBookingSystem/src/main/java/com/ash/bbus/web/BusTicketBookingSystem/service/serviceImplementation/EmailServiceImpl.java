package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.dto.EmailDTO;
import com.ash.bbus.web.BusTicketBookingSystem.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    // ✅ Read sender address from application.properties
    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // ✅ Core method — all specific methods delegate here
    @Async
    @Override
    public void sendEmail(EmailDTO emailDTO) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            // ✅ true = multipart (needed for attachments)
            MimeMessageHelper helper = new MimeMessageHelper(
                message, true, "UTF-8"
            );

            // ✅ Use from field or fallback to config email
            helper.setFrom(
                emailDTO.getFrom() != null
                    ? emailDTO.getFrom()
                    : fromEmail
            );

            helper.setTo(emailDTO.getTo());
            helper.setSubject(emailDTO.getSubject());

            // ✅ Use isHtml flag from DTO
            helper.setText(emailDTO.getBody(), emailDTO.isHtml());

            // ✅ Attach PDF if present
            if (emailDTO.hasAttachment()) {
                helper.addAttachment(
                    emailDTO.getFileName(),
                    new org.springframework.core.io
                        .ByteArrayResource(emailDTO.getAttachment())
                );
            }

            mailSender.send(message);

        } catch (MessagingException e) {
            System.err.println(
                "Email send failed to: " + emailDTO.getTo()
                + " | Error: " + e.getMessage()
            );
        }
    }

    // ✅ Booking confirmation email
    @Async
    @Override
    public void sendBookingConfirmation(String toEmail,
                                         String pnrNumber,
                                         String source,
                                         String destination) {
        EmailDTO email = new EmailDTO(
            toEmail,
            "Booking Confirmed — PNR: " + pnrNumber,
            buildBookingEmailBody(pnrNumber, source, destination),
            true  // isHtml
        );
        sendEmail(email);
    }

    // ✅ Cancellation confirmation email
    @Async
    @Override
    public void sendCancellationConfirmation(String toEmail,
                                              String pnrNumber,
                                              String refundAmount) {
        EmailDTO email = new EmailDTO(
            toEmail,
            "Booking Cancelled — PNR: " + pnrNumber,
            buildCancellationEmailBody(pnrNumber, refundAmount),
            true
        );
        sendEmail(email);
    }

    // ✅ Welcome email on registration
    @Async
    @Override
    public void sendWelcomeEmail(String toEmail, String name) {
        EmailDTO email = new EmailDTO(
            toEmail,
            "Welcome to BusBooking!",
            buildWelcomeEmailBody(name),
            true
        );
        sendEmail(email);
    }

    // ✅ Send ticket PDF as email attachment
    @Async
    @Override
    public void sendTicketWithAttachment(String toEmail,
                                          String pnrNumber,
                                          byte[] pdfBytes) {
        EmailDTO email = new EmailDTO(
            toEmail,
            "Your Ticket — PNR: " + pnrNumber,
            buildTicketAttachmentBody(pnrNumber),
            true,
            pdfBytes,                          // PDF bytes
            "ticket-" + pnrNumber + ".pdf"     // file name
        );
        sendEmail(email);
    }

    // =========================================================
    // ✅ HTML email body builders
    // =========================================================

    private String buildBookingEmailBody(String pnr,
                                          String source,
                                          String destination) {
        return """
            <html><body style='font-family:Arial,sans-serif;'>
            <div style='max-width:600px;margin:auto;padding:20px;
                        border:1px solid #eee;border-radius:8px;'>
              <h2 style='color:#d84e55;text-align:center;'>
                Booking Confirmed!
              </h2>
              <p>Your bus ticket has been booked successfully.</p>
              <table style='width:100%;border-collapse:collapse;'>
                <tr style='background:#f5f5f5;'>
                  <td style='padding:10px;font-weight:bold;'>PNR Number</td>
                  <td style='padding:10px;'>%s</td>
                </tr>
                <tr>
                  <td style='padding:10px;font-weight:bold;'>Route</td>
                  <td style='padding:10px;'>%s → %s</td>
                </tr>
              </table>
              <p style='margin-top:20px;color:#888;font-size:12px;'>
                Show this PNR at boarding. Have a safe journey!
              </p>
            </div>
            </body></html>
            """.formatted(pnr, source, destination);
    }

    private String buildCancellationEmailBody(String pnr,
                                               String refundAmount) {
        return """
            <html><body style='font-family:Arial,sans-serif;'>
            <div style='max-width:600px;margin:auto;padding:20px;
                        border:1px solid #eee;border-radius:8px;'>
              <h2 style='color:#d84e55;text-align:center;'>
                Booking Cancelled
              </h2>
              <table style='width:100%;border-collapse:collapse;'>
                <tr style='background:#f5f5f5;'>
                  <td style='padding:10px;font-weight:bold;'>PNR Number</td>
                  <td style='padding:10px;'>%s</td>
                </tr>
                <tr>
                  <td style='padding:10px;font-weight:bold;'>
                    Refund Amount
                  </td>
                  <td style='padding:10px;color:green;
                              font-weight:bold;'>₹%s</td>
                </tr>
              </table>
              <p style='margin-top:16px;'>
                Refund has been credited to your wallet.
              </p>
            </div>
            </body></html>
            """.formatted(pnr, refundAmount);
    }

    private String buildWelcomeEmailBody(String name) {
        return """
            <html><body style='font-family:Arial,sans-serif;'>
            <div style='max-width:600px;margin:auto;padding:20px;
                        border:1px solid #eee;border-radius:8px;'>
              <h2 style='color:#d84e55;text-align:center;'>
                Welcome, %s!
              </h2>
              <p>Thank you for registering with BusBooking.</p>
              <p>Start exploring available buses and book your
                 journey today!</p>
            </div>
            </body></html>
            """.formatted(name);
    }

    private String buildTicketAttachmentBody(String pnr) {
        return """
            <html><body style='font-family:Arial,sans-serif;'>
            <div style='max-width:600px;margin:auto;padding:20px;
                        border:1px solid #eee;border-radius:8px;'>
              <h2 style='color:#d84e55;text-align:center;'>
                Your Ticket is Attached!
              </h2>
              <p>Please find your bus ticket PDF attached to this email.</p>
              <p><strong>PNR:</strong> %s</p>
              <p style='color:#888;font-size:12px;'>
                Present this ticket at the boarding point.
              </p>
            </div>
            </body></html>
            """.formatted(pnr);
    }
}