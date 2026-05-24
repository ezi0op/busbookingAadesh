import api from "../api/axiosInstance";

export const getAllUsers  = ()      =>
  api.get("/users");

export const getUserById  = (id)    =>
  api.get(`/users/${id}`);

export const deleteUser   = (id)    =>
  api.delete(`/users/${id}`);

export const updateUser   = (id, d) =>
  api.put(`/users/${id}`, d);