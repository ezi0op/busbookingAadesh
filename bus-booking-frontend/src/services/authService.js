import api from "../api/axiosInstance";

// ✅ Register new user
export const register = (data) =>
  api.post("/users/register", data);

// ✅ Login
export const login = (data) =>
  api.post("/users/login", data);

// ✅ Get user profile
export const getProfile = (id) =>
  api.get(`/users/${id}`);

// ✅ Update profile
export const updateProfile = (id, data) =>
  api.put(`/users/${id}`, data);