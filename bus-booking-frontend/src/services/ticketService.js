import api from "../api/axiosInstance";

// ✅ Get ticket by booking ID
export const getTicketByBooking = (bookingId) =>
  api.get(`/tickets/booking/${bookingId}`);

// ✅ Get ticket by PNR
export const getTicketByPnr = (pnr) =>
  api.get(`/tickets/pnr/${pnr}`);

// ✅ Download PDF ticket
export const downloadTicket = (bookingId) =>
  api.get(`/tickets/booking/${bookingId}/download`, {
    responseType: "blob",  // important for PDF download
  });