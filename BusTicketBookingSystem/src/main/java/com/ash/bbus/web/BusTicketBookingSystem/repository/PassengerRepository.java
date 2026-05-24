package com.ash.bbus.web.BusTicketBookingSystem.repository;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    List<Passenger> findByBookingId(Long bookingId);
    List<Passenger> findByTripSeatId(Long tripSeatId);
}