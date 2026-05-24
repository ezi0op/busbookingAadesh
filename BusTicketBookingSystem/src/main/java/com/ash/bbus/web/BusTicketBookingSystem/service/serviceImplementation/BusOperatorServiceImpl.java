package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Bus;
import com.ash.bbus.web.BusTicketBookingSystem.entity.BusOperator;
import com.ash.bbus.web.BusTicketBookingSystem.entity.Route;
import com.ash.bbus.web.BusTicketBookingSystem.entity.Trip;
import com.ash.bbus.web.BusTicketBookingSystem.repository.BusOperatorRepository;
import com.ash.bbus.web.BusTicketBookingSystem.repository.BusRepository;
import com.ash.bbus.web.BusTicketBookingSystem.service.BusOperatorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusOperatorServiceImpl implements BusOperatorService {

    private final BusOperatorRepository busOperatorRepository;
    private final BusRepository busRepository;

    @Override
    public BusOperator createOperator(BusOperator operator) {
        return busOperatorRepository.save(operator);
    }

    @Override
    public BusOperator getOperatorById(Long id) {
        return busOperatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operator not found with id " + id));
    }

    @Override
    public List<BusOperator> getAllOperators() {
        return busOperatorRepository.findAll();
    }

    @Override
    public BusOperator updateOperator(Long id, BusOperator operator) {
        BusOperator existing = getOperatorById(id);
        existing.setName(operator.getName());
        existing.setContactEmail(operator.getContactEmail());
        existing.setContactPhone(operator.getContactPhone());
        return busOperatorRepository.save(existing);
    }

    @Override
    public BusOperator deactivateOperator(Long id) {
        BusOperator existing = getOperatorById(id);
        existing.setRating(0.0); // Mocking deactivation by setting rating 0, or add status field
        return busOperatorRepository.save(existing);
    }

    @Override
    public List<BusOperator> searchOperators(String name) {
        return busOperatorRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Route> getRoutes(Long busId) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new RuntimeException("Bus not found with id " + busId));
        return bus.getTrips().stream()
                .map(Trip::getRoute)
                .distinct()
                .collect(Collectors.toList());
    }
}
