import { useState, useEffect } from "react";
import {
  getAllBookings,
  cancelBooking,
} from "../../services/bookingService";
import "../styles/admin.css";

export default function AdminBookings() {

  const [bookings, setBookings] = useState([]);
  const [loading,  setLoading]  = useState(true);
  const [error,    setError]    = useState("");
  const [filter,   setFilter]   = useState("ALL");

  useEffect(() => { fetchBookings(); }, []);

  const fetchBookings = async () => {
    setLoading(true);
    try {
      const res = await getAllBookings();
      setBookings(res.data || []);
    } catch { setError("Failed to load bookings."); }
    finally { setLoading(false); }
  };

  const handleCancel = async (id) => {
    if (!window.confirm(
      "Cancel this booking? 50% refund will be issued."
    )) return;
    try {
      await cancelBooking(id);
      fetchBookings();
    } catch { setError("Failed to cancel booking."); }
  };

  const getStatusClass = (status) => {
    switch (status) {
      case "CONFIRMED": return "status-confirmed";
      case "PENDING":   return "status-pending";
      case "CANCELLED": return "status-cancelled";
      default:          return "";
    }
  };

  const filtered = filter === "ALL"
    ? bookings
    : bookings.filter(b => b.status === filter);

  return (
    <div>
      <div className="admin-page-header">
        <h2>🎫 Booking Management</h2>

        {/* Filter tabs */}
        <div style={{ display: "flex", gap: "8px" }}>
          {["ALL","CONFIRMED","PENDING","CANCELLED"]
            .map(f => (
              <button
                key={f}
                onClick={() => setFilter(f)}
                style={{
                  padding: "7px 14px",
                  borderRadius: "8px",
                  border: "1.5px solid",
                  borderColor:
                    filter === f ? "#d84e55" : "#e0e0e0",
                  background:
                    filter === f ? "#d84e55" : "white",
                  color:
                    filter === f ? "white" : "#555",
                  fontWeight: "600",
                  fontSize: "13px",
                  cursor: "pointer",
                }}>
                {f}
              </button>
            ))}
        </div>
      </div>

      {error && (
        <div className="error-box"
          style={{ marginBottom: "16px" }}>
          ⚠️ {error}
        </div>
      )}

      {loading ? (
        <div className="admin-loader">
          <div className="spinner" />
        </div>
      ) : (
        <div className="admin-table-wrap">
          <table className="admin-table">
            <thead>
              <tr>
                <th>#</th>
                <th>Booking ID</th>
                <th>User</th>
                <th>Route</th>
                <th>Amount</th>
                <th>Status</th>
                <th>Booked At</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.length === 0 ? (
                <tr>
                  <td colSpan={8}>
                    <div className="admin-empty">
                      <div className="admin-empty-icon">
                        🎫
                      </div>
                      <p>No bookings found.</p>
                    </div>
                  </td>
                </tr>
              ) : (
                filtered.map((b, i) => (
                  <tr key={b.id}>
                    <td>{i + 1}</td>
                    <td>
                      <strong>#{b.id}</strong>
                    </td>
                    <td>{b.userName}</td>
                    <td>
                      {b.source} → {b.destination}
                    </td>
                    <td>₹{b.totalAmount}</td>
                    <td>
                      <span className={
                        getStatusClass(b.status)
                      }>
                        {b.status}
                      </span>
                    </td>
                    <td>
                      {b.bookingTime
                        ? new Date(b.bookingTime)
                            .toLocaleDateString()
                        : "-"}
                    </td>
                    <td>
                      {b.status === "CONFIRMED" && (
                        <button
                          className="btn-cancel-booking"
                          onClick={() =>
                            handleCancel(b.id)}>
                          Cancel
                        </button>
                      )}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}