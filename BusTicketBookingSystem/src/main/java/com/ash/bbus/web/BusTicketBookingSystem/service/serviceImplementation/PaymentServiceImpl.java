package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.dto.PaymentDTO;
import com.ash.bbus.web.BusTicketBookingSystem.entity.Booking;
import com.ash.bbus.web.BusTicketBookingSystem.entity.Payment;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.BookingStatus;
import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.PaymentStatus;
import com.ash.bbus.web.BusTicketBookingSystem.exception.ResourceNotFoundException;
import com.ash.bbus.web.BusTicketBookingSystem.repository.BookingRepository;
import com.ash.bbus.web.BusTicketBookingSystem.repository.PaymentRepository;
import com.ash.bbus.web.BusTicketBookingSystem.service.PaymentService;
import com.ash.bbus.web.BusTicketBookingSystem.service.TicketService;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final TicketService ticketService;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            BookingRepository bookingRepository,
            // ✅ @Lazy prevents circular dependency
            // PaymentService → TicketService → BookingRepository
            @Lazy TicketService ticketService) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.ticketService = ticketService;
    }

    @Override
    @Transactional
    public PaymentDTO processPayment(Long bookingId, PaymentDTO dto) {

        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Booking not found: " + bookingId
            ));

        // ✅ Prevent double payment
        if (paymentRepository.existsByBookingId(bookingId)) {
            throw new IllegalStateException(
                "Payment already processed for booking: " + bookingId
            );
        }

        // ✅ Build payment record
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalAmount());
        payment.setMethod(dto.getMethod());
        payment.setStatus(PaymentStatus.SUCCESS);

        // ✅ Generate unique transaction ID
        payment.setTransactionId(
            "TXN" + UUID.randomUUID()
                .toString().replace("-", "")
                .substring(0, 10).toUpperCase()
        );

        paymentRepository.save(payment);

        // ✅ Update booking status to CONFIRMED
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        // ✅ Auto-generate ticket after successful payment
        ticketService.generateTicket(bookingId);

        return mapToDTO(payment);
    }

    @Override
    public PaymentDTO getPaymentByBookingId(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Payment not found for booking: " + bookingId
            ));
        return mapToDTO(payment);
    }

    @Override
    @Transactional
    public PaymentDTO processRefund(Long bookingId) {

        Payment payment = paymentRepository.findByBookingId(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Payment not found for booking: " + bookingId
            ));

        // ✅ 50% refund calculation
        BigDecimal refundAmount = payment.getAmount()
            .divide(new BigDecimal("2"));

        payment.setRefundAmount(refundAmount);
        payment.setStatus(PaymentStatus.REFUNDED);

        paymentRepository.save(payment);

        return mapToDTO(payment);
    }

    private PaymentDTO mapToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setBookingId(payment.getBooking().getId());
        dto.setAmount(payment.getAmount());
        dto.setRefundAmount(payment.getRefundAmount());
        dto.setStatus(payment.getStatus());
        dto.setMethod(payment.getMethod());
        dto.setTransactionId(payment.getTransactionId());
        dto.setGatewayOrderId(payment.getGatewayOrderId());
        dto.setFailureReason(payment.getFailureReason());
        dto.setCreatedAt(payment.getCreatedAt());
        return dto;
    }
}