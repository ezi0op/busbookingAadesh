package com.ash.bbus.web.BusTicketBookingSystem.repository;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByBookingId(Long bookingId);
    boolean existsByBookingId(Long bookingId);
    Optional<Payment> findByTransactionId(String transactionId);
}