import api from "../api/axiosInstance";

export const getAllRoutes  = ()      =>
  api.get("/routes");

export const getRouteById = (id)    =>
  api.get(`/routes/${id}`);

export const addRoute     = (data)  =>
  api.post("/routes", data);

export const updateRoute  = (id, d) =>
  api.put(`/routes/${id}`, d);

export const deleteRoute  = (id)    =>
  api.delete(`/routes/${id}`);