package com.ash.bbus.web.BusTicketBookingSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class EmailDTO {

    // ✅ FIX 1: Validation on required fields
    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid recipient email")
    private String to;

    // ✅ FIX 2: from address — flexible sender
    private String from;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Body is required")
    private String body;

    // ✅ FIX 3: isHtml flag — controls HTML vs plain text
    // Default true because all our emails are HTML
    private boolean isHtml = true;

    // ✅ PDF ticket attachment support
    private byte[] attachment;
    private String fileName;

    // ✅ Simple email constructor — no attachment
    public EmailDTO(String to, String subject,
                    String body, boolean isHtml) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.isHtml = isHtml;
    }

    // ✅ Email with attachment constructor — for ticket PDF
    public EmailDTO(String to, String subject, String body,
                    boolean isHtml, byte[] attachment,
                    String fileName) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.isHtml = isHtml;
        this.attachment = attachment;
        this.fileName = fileName;
    }

    public boolean hasAttachment() {
        return attachment != null && attachment.length > 0;
    }
}
