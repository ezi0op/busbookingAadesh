package com.ash.bbus.web.BusTicketBookingSystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

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

    // ✅ Required no-args constructor
    public EmailDTO() {}

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

    // --- Getters & Setters ---

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public boolean isHtml() { return isHtml; }
    public void setHtml(boolean html) { isHtml = html; }

    public byte[] getAttachment() { return attachment; }
    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public boolean hasAttachment() {
        return attachment != null && attachment.length > 0;
    }
}