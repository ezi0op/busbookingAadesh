package com.ash.bbus.web.BusTicketBookingSystem.service;

import com.ash.bbus.web.BusTicketBookingSystem.dto.RouteDTO;
import java.util.List;

public interface RouteService {

    RouteDTO addRoute(RouteDTO dto);
    RouteDTO getRouteById(Long id);
    List<RouteDTO> getAllRoutes();
    RouteDTO updateRoute(Long id, RouteDTO dto);
    void deleteRoute(Long id);
}