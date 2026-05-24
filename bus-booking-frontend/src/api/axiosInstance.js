import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

// ✅ Request interceptor — add token
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// ✅ Response interceptor — fixed
axiosInstance.interceptors.response.use(
  (response) => {
    // ✅ Return full response.data (ApiResponse object)
    // So res.data gives you the actual data
    return response.data;
  },
  (error) => {
    console.log("Axios error:", error);

    const message =
      error.response?.data?.message ||
      error.response?.data ||
      error.message ||
      "Something went wrong";

    // ✅ 401 — logout
    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      // Only redirect if not already on login page
      if (!window.location.pathname
            .includes("/login")) {
        window.location.href = "/login";
      }
    }

    return Promise.reject(message);
  }
);

export default axiosInstance;