package com.ash.bbus.web.BusTicketBookingSystem.service;

import java.util.List;

import com.ash.bbus.web.BusTicketBookingSystem.entity.BusOperator;
import com.ash.bbus.web.BusTicketBookingSystem.entity.Route;

public interface BusOperatorService {
    BusOperator createOperator(BusOperator operator);
    BusOperator getOperatorById(Long id);
    List<BusOperator> getAllOperators();
    BusOperator updateOperator(Long id, BusOperator operator);
    BusOperator deactivateOperator(Long id);
    List<BusOperator> searchOperators(String name);
    List<Route> getRoutes(Long busId);
}
