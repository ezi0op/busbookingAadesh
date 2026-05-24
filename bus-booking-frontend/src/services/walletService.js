import api from "../api/axiosInstance";

// ✅ Get wallet balance
export const getWallet = () =>
  api.get("/wallet");

// ✅ Get transaction history
export const getTransactions = () =>
  api.get("/wallet/transactions");