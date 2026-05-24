package com.ash.bbus.web.BusTicketBookingSystem.service;

import java.util.List;

import com.ash.bbus.web.BusTicketBookingSystem.dto.BusDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.SeatDTO;

public interface BusService {

	BusDTO addBus(BusDTO dto);

	BusDTO getBusById(Long busId);

	List<BusDTO> getAllBuses();

	BusDTO updateBus(Long busId, BusDTO dto);

	void deleteBus(Long busId);

	void addSeatsToBus(Long busId, List<SeatDTO> seats);

	List<SeatDTO> getSeatsByBus(Long busId);
}