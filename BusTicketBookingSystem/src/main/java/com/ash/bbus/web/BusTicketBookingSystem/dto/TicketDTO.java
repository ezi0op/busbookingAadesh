package com.ash.bbus.web.BusTicketBookingSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.ash.bbus.web.BusTicketBookingSystem.entity.enums.PaymentMethod;

@Getter
@Setter
@NoArgsConstructor
public class TicketDTO {

	private Long id;
	private String pnrNumber;

	// Trip info
	private String source;
	private String destination;
	private LocalDate travelDate;
	private LocalTime departureTime;
	private LocalTime arrivalTime;

	// Bus info
	private String busName;
	private String busNumber;

	// Passenger + seat details
	private List<PassengerDTO> passengers;
	private List<TripSeatDTO> seats;

	// Payment info
	private BigDecimal totalAmount;
	private PaymentMethod paymentMethod;

	// Ticket meta
	private LocalDateTime issuedAt;
	private boolean valid;

}
