package com.ash.bbus.web.BusTicketBookingSystem.util;

import com.ash.bbus.web.BusTicketBookingSystem.dto.PassengerDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.TicketDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.TripSeatDTO;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class PdfUtil {

    // ✅ Generates PDF ticket and returns as byte[]
    public byte[] generateTicketPdf(TicketDTO ticket) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // ✅ Title
            document.add(
                new Paragraph("🚌 Bus Ticket")
                    .setFontSize(22)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.DARK_GRAY)
            );

            document.add(new Paragraph("\n"));

            // ✅ PNR box
            document.add(
                new Paragraph("PNR: " + ticket.getPnrNumber())
                    .setFontSize(16)
                    .setBold()
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(8)
            );

            document.add(new Paragraph("\n"));

            // ✅ Trip Info table
            Table tripTable = new Table(
                UnitValue.createPercentArray(new float[]{1, 2})
            ).useAllAvailableWidth();

            addRow(tripTable, "From", ticket.getSource());
            addRow(tripTable, "To", ticket.getDestination());
            addRow(tripTable, "Date",
                ticket.getTravelDate().toString());
            addRow(tripTable, "Departure",
                ticket.getDepartureTime().toString());
            addRow(tripTable, "Arrival",
                ticket.getArrivalTime().toString());
            addRow(tripTable, "Bus", ticket.getBusName()
                + " (" + ticket.getBusNumber() + ")");
            addRow(tripTable, "Total Amount",
                "₹" + ticket.getTotalAmount());

            document.add(tripTable);
            document.add(new Paragraph("\n"));

            // ✅ Passenger details table
            document.add(
                new Paragraph("Passengers")
                    .setFontSize(14)
                    .setBold()
            );

            Table passengerTable = new Table(
                UnitValue.createPercentArray(
                    new float[]{1, 2, 1, 1, 1}
                )
            ).useAllAvailableWidth();

            // Table header
            passengerTable.addHeaderCell(
                headerCell("Seat No."));
            passengerTable.addHeaderCell(
                headerCell("Name"));
            passengerTable.addHeaderCell(
                headerCell("Age"));
            passengerTable.addHeaderCell(
                headerCell("Gender"));
            passengerTable.addHeaderCell(
                headerCell("Berth"));

            // Table rows — match passenger to seat
            for (int i = 0; i < ticket.getPassengers().size(); i++) {
                PassengerDTO p = ticket.getPassengers().get(i);

                // ✅ Find matching seat by tripSeatId
                TripSeatDTO seat = ticket.getSeats().stream()
                    .filter(s -> s.getId()
                        .equals(p.getTripSeatId()))
                    .findFirst()
                    .orElse(null);

                passengerTable.addCell(
                    seat != null ? seat.getSeatNumber() : "-"
                );
                passengerTable.addCell(p.getName());
                passengerTable.addCell(
                    String.valueOf(p.getAge()));
                passengerTable.addCell(
                    p.getGender().name());
                passengerTable.addCell(
                    seat != null
                        ? seat.getBerthType().name()
                        : "-"
                );
            }

            document.add(passengerTable);
            document.add(new Paragraph("\n"));

            // ✅ Footer
            document.add(
                new Paragraph(
                    "Thank you for booking with BusBooking!"
                )
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10)
                .setFontColor(ColorConstants.GRAY)
            );

            document.close();

        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to generate PDF ticket: " + e.getMessage()
            );
        }

        return out.toByteArray();
    }

    // ✅ Helper — add key-value row to table
    private void addRow(Table table, String key, String value) {
        table.addCell(
            new Cell()
                .add(new Paragraph(key).setBold())
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
        );
        table.addCell(
            new Cell().add(new Paragraph(value))
        );
    }

    // ✅ Helper — styled header cell
    private Cell headerCell(String text) {
        return new Cell()
            .add(new Paragraph(text).setBold()
                .setFontColor(ColorConstants.WHITE))
            .setBackgroundColor(ColorConstants.DARK_GRAY)
            .setTextAlignment(TextAlignment.CENTER);
    }
}