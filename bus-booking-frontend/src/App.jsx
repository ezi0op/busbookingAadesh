import {
  BrowserRouter,
  Routes,
  Route,
  Navigate,
  useLocation,
} from "react-router-dom";
import { BookingProvider } from "./context/BookingContext";

// User pages
import HomePage      from "./pages/HomePage";
import LoginPage     from "./pages/LoginPage";
import RegisterPage  from "./pages/RegisterPage";
import TripsPage     from "./pages/TripsPage";
import SeatPage      from "./pages/SeatPage";
import PassengerPage from "./pages/PassengerPage";
import PaymentPage   from "./pages/PaymentPage";
import TicketPage    from "./pages/TicketPage";

// Common
import Header         from "./components/common/Header";
import Footer         from "./components/common/Footer";
import ProtectedRoute from "./components/common/ProtectedRoute";

// Admin
import AdminLayout    from "./admin/AdminLayout";
import AdminRoute     from "./admin/AdminRoute";
import AdminDashboard from "./admin/pages/AdminDashboard";
import AdminBuses     from "./admin/pages/AdminBuses";
import AdminRoutes    from "./admin/pages/AdminRoutes";
import AdminTrips     from "./admin/pages/AdminTrips";
import AdminBookings  from "./admin/pages/AdminBookings";
import AdminUsers     from "./admin/pages/AdminUsers";

// ✅ Wrapper that shows Header+Footer only on user pages
function UserLayout() {
  return (
    <>
      <Header />
      <Routes>
        <Route path="/"         element={<HomePage />} />
        <Route path="/login"    element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/trips"    element={<TripsPage />} />

        <Route path="/seats/:tripId" element={
          <ProtectedRoute>
            <SeatPage />
          </ProtectedRoute>
        }/>

        <Route path="/passengers" element={
          <ProtectedRoute>
            <PassengerPage />
          </ProtectedRoute>
        }/>

        <Route path="/payment" element={
          <ProtectedRoute>
            <PaymentPage />
          </ProtectedRoute>
        }/>

        <Route path="/ticket" element={
          <ProtectedRoute>
            <TicketPage />
          </ProtectedRoute>
        }/>

        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
      <Footer />
    </>
  );
}

export default function App() {
  return (
    <BookingProvider>
      <BrowserRouter>
        <Routes>

          {/* ✅ ADMIN routes — no Header/Footer */}
          <Route path="/admin" element={
            <AdminRoute>
              <AdminLayout />
            </AdminRoute>
          }>
            <Route index
              element={<AdminDashboard />} />
            <Route path="buses"
              element={<AdminBuses />} />
            <Route path="routes"
              element={<AdminRoutes />} />
            <Route path="trips"
              element={<AdminTrips />} />
            <Route path="bookings"
              element={<AdminBookings />} />
            <Route path="users"
              element={<AdminUsers />} />
          </Route>

          {/* ✅ USER routes — Header + Footer via UserLayout */}
          <Route path="/*" element={<UserLayout />} />

        </Routes>
      </BrowserRouter>
    </BookingProvider>
  );
}