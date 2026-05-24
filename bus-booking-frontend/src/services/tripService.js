import api from "../api/axiosInstance";

export const searchTrips = (source, destination, date) =>
  api.get("/trips/search", {
    params: { source, destination, date },
  });

export const getTripById = (id) =>
  api.get(`/trips/${id}`);

export const getAllTrips = () =>
  api.get("/trips");

export const createTrip = (data) =>
  api.post("/trips", data);

export const updateTrip = (id, data) =>
  api.put(`/trips/${id}`, data);

export const cancelTrip = (id) =>
  api.put(`/trips/${id}/cancel`);