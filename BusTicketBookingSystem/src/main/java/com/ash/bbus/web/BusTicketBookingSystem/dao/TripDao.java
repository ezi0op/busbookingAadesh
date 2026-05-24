package com.ash.bbus.web.BusTicketBookingSystem.dao;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Trip;

import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;

public interface TripDao {

    // ✅ Search with filters + sorting
    List<Trip> searchTrips(String source, String destination,
                            LocalDate date);

    // ✅ Filter by price range
    List<Trip> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice,
                                 LocalDate date);

    // ✅ Filter by bus type (AC, NON_AC, SLEEPER)
    List<Trip> findByBusType(String busType, LocalDate date);

    // ✅ Sort trips by price ascending
    List<Trip> findAllSortedByPrice(String source,
                                     String destination,
                                     LocalDate date);

    // ✅ Sort trips by departure time
    List<Trip> findAllSortedByDeparture(String source,
                                         String destination,
                                         LocalDate date);
}