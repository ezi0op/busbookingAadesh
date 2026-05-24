import { Navigate } from "react-router-dom";

export default function AdminRoute({ children }) {

  const token = localStorage.getItem("token");
  const userStr = localStorage.getItem("user");

  // ✅ Debug — check what's stored
  console.log("Token:", token);
  console.log("User string:", userStr);

  // ✅ Not logged in
  if (!token) {
    console.log("No token — redirecting to login");
    return <Navigate to="/login" replace />;
  }

  // ✅ Parse user safely
  let user = null;
  try {
    user = JSON.parse(userStr);
  } catch (e) {
    console.log("Invalid user in localStorage");
    return <Navigate to="/login" replace />;
  }

  console.log("User role:", user?.role);

  // ✅ Not admin
  if (!user || user.role !== "ADMIN") {
    console.log("Not admin — redirecting to home");
    return <Navigate to="/" replace />;
  }

  return children;
}