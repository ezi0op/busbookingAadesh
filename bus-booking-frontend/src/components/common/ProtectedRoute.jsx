import { Navigate } from "react-router-dom";

// ✅ Wraps routes that require login
// If not logged in → redirect to /login
export default function ProtectedRoute({ children }) {
  const token = localStorage.getItem("token");

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return children;
}