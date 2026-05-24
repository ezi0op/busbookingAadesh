import api from "../api/axiosInstance";

// ✅ Process payment
export const processPayment = (bookingId, data) =>
  api.post(`/payments/${bookingId}`, data);

// ✅ Get payment details
export const getPayment = (bookingId) =>
  api.get(`/payments/${bookingId}`);