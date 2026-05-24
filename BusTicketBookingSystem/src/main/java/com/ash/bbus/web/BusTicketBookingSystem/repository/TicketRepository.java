package com.ash.bbus.web.BusTicketBookingSystem.repository;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByPnrNumber(String pnrNumber);
    Optional<Ticket> findByBookingId(Long bookingId);
    boolean existsByBookingId(Long bookingId);
}