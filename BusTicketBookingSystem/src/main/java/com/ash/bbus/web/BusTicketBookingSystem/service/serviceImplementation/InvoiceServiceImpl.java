package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.entity.Booking;
import com.ash.bbus.web.BusTicketBookingSystem.entity.Passenger;
import com.ash.bbus.web.BusTicketBookingSystem.entity.Payment;
import com.ash.bbus.web.BusTicketBookingSystem.entity.Route;
import com.ash.bbus.web.BusTicketBookingSystem.entity.TripSeat;
import com.ash.bbus.web.BusTicketBookingSystem.exception.ResourceNotFoundException;
import com.ash.bbus.web.BusTicketBookingSystem.repository.BookingRepository;
import com.ash.bbus.web.BusTicketBookingSystem.repository.PassengerRepository;
import com.ash.bbus.web.BusTicketBookingSystem.repository.TripSeatRepository;
import com.ash.bbus.web.BusTicketBookingSystem.service.InvoiceService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final TripSeatRepository tripSeatRepository;

    public InvoiceServiceImpl(BookingRepository bookingRepository,
                              PassengerRepository passengerRepository,
                              TripSeatRepository tripSeatRepository) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.tripSeatRepository = tripSeatRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generateInvoice(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Booking not found: " + bookingId
            ));

        List<Passenger> passengers = passengerRepository.findByBookingId(bookingId);
        List<TripSeat> seats = tripSeatRepository.findByBookingId(bookingId);
        Payment payment = booking.getPayment();
        Route route = booking.getTrip().getRoute();

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                float y = 760;
                y = addLine(content, "Bus Ticket Invoice", 20, true, y);
                y = addLine(content, "Booking ID: " + booking.getId(), 12, false, y - 16);
                y = addLine(content, "Route: " + route.getSource() + " to " + route.getDestination(), 12, false, y);
                y = addLine(content, "Travel Date: " + booking.getTrip().getTravelDate(), 12, false, y);
                y = addLine(content, "Seats: " + formatSeats(seats), 12, false, y);
                y = addLine(content, "Amount: " + booking.getTotalAmount(), 12, false, y);
                y = addLine(content, "Payment Method: " + valueOrPending(payment == null ? null : payment.getMethod()), 12, false, y);
                y = addLine(content, "Transaction ID: " + valueOrPending(payment == null ? null : payment.getTransactionId()), 12, false, y - 10);

                y = addLine(content, "Passenger Details", 14, true, y - 16);
                for (Passenger passenger : passengers) {
                    String passengerLine = passenger.getName()
                        + ", Age " + passenger.getAge()
                        + ", " + passenger.getGender()
                        + ", Seat " + passenger.getTripSeat().getSeat().getSeatNumber();
                    y = addLine(content, passengerLine, 12, false, y);
                }
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException(
                "Failed to generate invoice for booking: " + bookingId,
                ex
            );
        }
    }

    private float addLine(PDPageContentStream content,
                          String text,
                          int fontSize,
                          boolean bold,
                          float y) throws IOException {
        content.beginText();
        content.setFont(bold ? PDType1Font.HELVETICA_BOLD : PDType1Font.HELVETICA, fontSize);
        content.newLineAtOffset(50, y);
        content.showText(text == null ? "" : text);
        content.endText();
        return y - 22;
    }

    private String formatSeats(List<TripSeat> seats) {
        if (seats == null || seats.isEmpty()) {
            return "N/A";
        }

        return seats.stream()
            .map(seat -> seat.getSeat().getSeatNumber())
            .collect(Collectors.joining(", "));
    }

    private String valueOrPending(Object value) {
        return value == null ? "PENDING" : value.toString();
    }
}
