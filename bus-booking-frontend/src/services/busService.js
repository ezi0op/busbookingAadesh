import api from "../api/axiosInstance";

export const getAllBuses  = ()       =>
  api.get("/buses");

export const getBusById  = (id)     =>
  api.get(`/buses/${id}`);

export const addBus      = (data)   =>
  api.post("/buses", data);

export const updateBus   = (id, d)  =>
  api.put(`/buses/${id}`, d);

export const deleteBus   = (id)     =>
  api.delete(`/buses/${id}`);