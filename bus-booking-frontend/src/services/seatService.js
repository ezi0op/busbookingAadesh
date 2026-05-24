import api from "../api/axiosInstance";

// ✅ Get all seats for a trip (seat layout)
export const getSeatsByTrip = (tripId) =>
  api.get(`/trips/${tripId}/seats`);

// ✅ Lock selected seats
export const lockSeats = (tripId, seatIds) =>
  api.post(`/trips/${tripId}/seats/lock`, { seatIds });