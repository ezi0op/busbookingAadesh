package com.ash.bbus.web.BusTicketBookingSystem.service;

import com.ash.bbus.web.BusTicketBookingSystem.dto.TicketDTO;

public interface TicketService {

    TicketDTO generateTicket(Long bookingId);
    TicketDTO getTicketByBookingId(Long bookingId);
    TicketDTO getTicketByPnr(String pnrNumber);
    byte[] downloadTicketPdf(Long bookingId);
}