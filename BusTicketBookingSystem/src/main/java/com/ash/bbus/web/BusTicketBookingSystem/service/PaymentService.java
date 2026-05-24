package com.ash.bbus.web.BusTicketBookingSystem.service;

import com.ash.bbus.web.BusTicketBookingSystem.dto.PaymentDTO;

public interface PaymentService {

    PaymentDTO processPayment(Long bookingId, PaymentDTO dto);
    PaymentDTO getPaymentByBookingId(Long bookingId);
    PaymentDTO processRefund(Long bookingId);
}