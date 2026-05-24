import api from "../api/axiosInstance";

// ✅ User booking actions
export const createBooking = (data) =>
  api.post("/bookings", data);

export const getBookingById = (id) =>
  api.get(`/bookings/${id}`);

export const getMyBookings = () =>
  api.get("/bookings/my");

export const cancelBooking = (id) =>
  api.put(`/bookings/${id}/cancel`);

// ✅ FIX: This was missing — needed by AdminDashboard + AdminBookings
export const getAllBookings = () =>
  api.get("/bookings");