package com.ash.bbus.web.BusTicketBookingSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ash.bbus.web.BusTicketBookingSystem.entity.BusOperator;

@Repository
public interface BusOperatorRepository extends JpaRepository<BusOperator, Long> {
    List<BusOperator> findByOperatorNameContainingIgnoreCase(String name);
}
