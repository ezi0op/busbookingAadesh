package com.ash.bbus.web.BusTicketBookingSystem.controller;

import com.ash.bbus.web.BusTicketBookingSystem.dto.ApiResponse;
import com.ash.bbus.web.BusTicketBookingSystem.dto.TicketDTO;
import com.ash.bbus.web.BusTicketBookingSystem.service.TicketService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // ✅ GET /api/tickets/booking/{bookingId}
    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<TicketDTO>> getTicketByBooking(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
            ApiResponse.success("Ticket fetched",
                ticketService.getTicketByBookingId(bookingId))
        );
    }

    // ✅ GET /api/tickets/pnr/{pnrNumber}
    @GetMapping("/pnr/{pnrNumber}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<TicketDTO>> getTicketByPnr(
            @PathVariable String pnrNumber) {

        return ResponseEntity.ok(
            ApiResponse.success("Ticket fetched",
                ticketService.getTicketByPnr(pnrNumber))
        );
    }

    // ✅ GET /api/tickets/booking/{bookingId}/download
    // Returns PDF as byte array for download
    @GetMapping("/booking/{bookingId}/download")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<byte[]> downloadTicket(
            @PathVariable Long bookingId) {

        byte[] pdf = ticketService.downloadTicketPdf(bookingId);

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=ticket-" + bookingId + ".pdf")
            .body(pdf);
    }
}