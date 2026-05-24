import { useState, useEffect } from "react";
import { Link } from "react-router-dom"; // ✅ Added
import { getAllBuses } from "../../services/busService";
import { getAllRoutes } from "../../services/routeService";
import { getAllTrips } from "../../services/tripService";
import { getAllBookings } from "../../services/bookingService";
import { getAllUsers } from "../../services/userService";
import "../styles/admin.css";

export default function AdminDashboard() {

  const [stats, setStats] = useState({
    buses: 0,
    routes: 0,
    trips: 0,
    bookings: 0,
    users: 0,
    revenue: 0,
  });

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {

      const [buses, routes, trips, bookings, users] = await Promise.all([
        getAllBuses(),
        getAllRoutes(),
        getAllTrips(),
        getAllBookings(),
        getAllUsers(),
      ]);

      const confirmedBookings = (bookings.data || [])
        .filter(b => b.status === "CONFIRMED");

      const revenue = confirmedBookings.reduce(
        (sum, b) => sum + (b.totalAmount || 0), 0
      );

      setStats({
        buses: (buses.data || []).length,
        routes: (routes.data || []).length,
        trips: (trips.data || []).length,
        bookings: (bookings.data || []).length,
        users: (users.data || []).length,
        revenue: revenue.toFixed(2),
      });

    } catch (err) {
      console.error("Failed to load stats:", err);
    } finally {
      setLoading(false);
    }
  };

  const statItems = [
    {
      icon: "🚌", label: "Total Buses",
      value: stats.buses,
      colorClass: "stat-icon-red"
    },
    {
      icon: "🗺️", label: "Total Routes",
      value: stats.routes,
      colorClass: "stat-icon-blue"
    },
    {
      icon: "📅", label: "Total Trips",
      value: stats.trips,
      colorClass: "stat-icon-green"
    },
    {
      icon: "🎫", label: "Total Bookings",
      value: stats.bookings,
      colorClass: "stat-icon-purple"
    },
    {
      icon: "👥", label: "Total Users",
      value: stats.users,
      colorClass: "stat-icon-blue"
    },
    {
      icon: "💰", label: "Total Revenue",
      value: `₹${stats.revenue}`,
      colorClass: "stat-icon-amber"
    },
  ];

  if (loading) {
    return (
      <div className="admin-loader">
        <div className="spinner" />
      </div>
    );
  }

  return (
    <div>
      <div className="admin-page-header">
        <h2>📊 Dashboard Overview</h2>
      </div>

      {/* Stats Section */}
      <div
        className="stat-cards"
        style={{ gridTemplateColumns: "repeat(3, 1fr)" }}
      >
        {statItems.map((item, i) => (
          <div key={i} className="stat-card">
            <div className={`stat-icon ${item.colorClass}`}>
              {item.icon}
            </div>
            <div className="stat-info">
              <div className="stat-value">{item.value}</div>
              <div className="stat-label">{item.label}</div>
            </div>
          </div>
        ))}
      </div>

      {/* Quick Actions */}
      <div
        style={{
          background: "white",
          borderRadius: "14px",
          padding: "24px",
          border: "1px solid #e0e0e0",
        }}
      >
        <h3
          style={{
            fontSize: "16px",
            fontWeight: "700",
            marginBottom: "16px",
            color: "#333",
          }}
        >
          Quick Actions
        </h3>

        <div
          style={{
            display: "grid",
            gridTemplateColumns: "repeat(4,1fr)",
            gap: "12px",
          }}
        >
          {[
            { label: "Add Bus", icon: "🚌", to: "/admin/buses" },
            { label: "Add Route", icon: "🗺️", to: "/admin/routes" },
            { label: "Create Trip", icon: "📅", to: "/admin/trips" },
            { label: "View Bookings", icon: "🎫", to: "/admin/bookings" },
          ].map((action) => (
            <Link
              key={action.to}
              to={action.to}
              style={{
                background: "#fff5f5",
                border: "1.5px solid #ffd0d0",
                borderRadius: "10px",
                padding: "16px",
                textAlign: "center",
                textDecoration: "none",
                color: "#d84e55",
                fontWeight: "600",
                fontSize: "14px",
                transition: "all 0.2s",
                display: "flex",
                flexDirection: "column",
                gap: "8px",
                alignItems: "center",
              }}
            >
              <span style={{ fontSize: "28px" }}>
                {action.icon}
              </span>
              {action.label}
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
}