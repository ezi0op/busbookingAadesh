package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.exception.ResourceNotFoundException;
import com.ash.bbus.web.BusTicketBookingSystem.repository.BookingRepository;
import com.ash.bbus.web.BusTicketBookingSystem.service.QRCodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QRCodeServiceImpl implements QRCodeService {

    private final BookingRepository bookingRepository;

    public QRCodeServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public byte[] generateQRCodeForBooking(Long bookingId) {
        bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Booking not found: " + bookingId
            ));

        String qrData = "BOOKING_ID:" + bookingId;

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                qrData,
                BarcodeFormat.QR_CODE,
                300,
                300
            );
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (WriterException | IOException ex) {
            throw new IllegalStateException(
                "Failed to generate QR code for booking: " + bookingId,
                ex
            );
        }
    }
}
