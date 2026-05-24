package com.ash.bbus.web.BusTicketBookingSystem.repository;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    boolean existsBySourceIgnoreCaseAndDestinationIgnoreCase(
        String source, String destination
    );

    Optional<Route> findBySourceIgnoreCaseAndDestinationIgnoreCase(
        String source, String destination
    );
}