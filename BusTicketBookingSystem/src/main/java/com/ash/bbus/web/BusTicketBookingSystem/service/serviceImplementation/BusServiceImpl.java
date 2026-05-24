package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.dto.BusDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.SeatDTO;
import com.ash.bbus.web.BusTicketBookingSystem.entity.Bus;
import com.ash.bbus.web.BusTicketBookingSystem.exception.ResourceNotFoundException;
import com.ash.bbus.web.BusTicketBookingSystem.repository.BusRepository;
import com.ash.bbus.web.BusTicketBookingSystem.service.BusService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;

    public BusServiceImpl(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    @Override
    @Transactional
    public BusDTO addBus(BusDTO dto) {

        // ✅ Check duplicate bus number
        if (busRepository.existsByBusNumber(dto.getBusNumber())) {
            throw new IllegalArgumentException(
                "Bus number already exists: " + dto.getBusNumber()
            );
        }

        Bus bus = new Bus();
        bus.setName(dto.getName());
        bus.setBusNumber(dto.getBusNumber());
        bus.setBusType(dto.getBusType());
        bus.setSeatType(dto.getSeatType());
        bus.setTotalSeats(dto.getTotalSeats());

        return mapToDTO(busRepository.save(bus));
    }

    @Override
    public BusDTO getBusById(Long id) {
        return mapToDTO(findBus(id));
    }

    @Override
    public List<BusDTO> getAllBuses() {
        return busRepository.findAll()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BusDTO updateBus(Long id, BusDTO dto) {
        Bus bus = findBus(id);

        bus.setName(dto.getName());
        bus.setBusType(dto.getBusType());
        bus.setSeatType(dto.getSeatType());
        bus.setTotalSeats(dto.getTotalSeats());

        return mapToDTO(busRepository.save(bus));
    }

    @Override
    @Transactional
    public void deleteBus(Long id) {
        if (!busRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bus not found: " + id);
        }
        busRepository.deleteById(id);
    }

    private Bus findBus(Long id) {
        return busRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Bus not found: " + id
            ));
    }

    private BusDTO mapToDTO(Bus bus) {
        BusDTO dto = new BusDTO();
        dto.setId(bus.getId());
        dto.setName(bus.getName());
        dto.setBusNumber(bus.getBusNumber());
        dto.setBusType(bus.getBusType());
        dto.setSeatType(bus.getSeatType());
        dto.setTotalSeats(bus.getTotalSeats());
        return dto;
    }

	@Override
	public void addSeatsToBus(Long busId, List<SeatDTO> seats) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<SeatDTO> getSeatsByBus(Long busId) {
		// TODO Auto-generated method stub
		return null;
	}
}