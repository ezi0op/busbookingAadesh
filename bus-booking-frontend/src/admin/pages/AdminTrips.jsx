import { useState, useEffect } from "react";
import {
  getAllTrips,
  createTrip,
  updateTrip,
  cancelTrip,
} from "../../services/tripService";
import { getAllBuses }  from "../../services/busService";
import { getAllRoutes } from "../../services/routeService";
import "../styles/admin.css";

export default function AdminTrips() {

  const [trips,     setTrips]     = useState([]);
  const [buses,     setBuses]     = useState([]);
  const [routes,    setRoutes]    = useState([]);
  const [loading,   setLoading]   = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editTrip,  setEditTrip]  = useState(null);
  const [error,     setError]     = useState("");
  const [filter,    setFilter]    = useState("ALL");

  const emptyForm = {
    busId:         "",
    routeId:       "",
    travelDate:    "",
    departureTime: "",
    arrivalTime:   "",
    price:         "",
  };
  const [form, setForm] = useState(emptyForm);

  // ✅ Fetch everything on mount
  useEffect(() => {
    fetchAll();
  }, []);

  const fetchAll = async () => {
    setLoading(true);
    try {
      const [tripsRes, busesRes, routesRes] =
        await Promise.all([
          getAllTrips(),
          getAllBuses(),
          getAllRoutes(),
        ]);
      setTrips(tripsRes.data   || []);
      setBuses(busesRes.data   || []);
      setRoutes(routesRes.data || []);
    } catch {
      setError("Failed to load data.");
    } finally {
      setLoading(false);
    }
  };

  const handleOpenAdd = () => {
    setEditTrip(null);
    setForm(emptyForm);
    setError("");
    setShowModal(true);
  };

  const handleOpenEdit = (trip) => {
    setEditTrip(trip);
    setForm({
      busId:         trip.busId,
      routeId:       trip.routeId,
      travelDate:    trip.travelDate,
      departureTime: trip.departureTime,
      arrivalTime:   trip.arrivalTime,
      price:         trip.price,
    });
    setError("");
    setShowModal(true);
  };

  // ✅ Validate form before submit
  const validateForm = () => {
    if (!form.busId) {
      setError("Please select a bus"); return false;
    }
    if (!form.routeId) {
      setError("Please select a route"); return false;
    }
    if (!form.travelDate) {
      setError("Please select travel date"); return false;
    }
    if (!form.departureTime) {
      setError("Please enter departure time"); return false;
    }
    if (!form.arrivalTime) {
      setError("Please enter arrival time"); return false;
    }
    if (!form.price || form.price <= 0) {
      setError("Please enter valid price"); return false;
    }
    if (form.departureTime >= form.arrivalTime) {
      setError(
        "Arrival time must be after departure time"
      );
      return false;
    }
    return true;
  };

  const handleSubmit = async () => {
    setError("");
    if (!validateForm()) return;

    try {
      const payload = {
        ...form,
        busId:   parseInt(form.busId),
        routeId: parseInt(form.routeId),
        price:   parseFloat(form.price),
      };

      if (editTrip) {
        await updateTrip(editTrip.id, payload);
      } else {
        await createTrip(payload);
      }
      setShowModal(false);
      fetchAll();
    } catch (err) {
      setError(
        typeof err === "string"
          ? err
          : "Operation failed. Please try again."
      );
    }
  };

  const handleCancel = async (id) => {
    if (!window.confirm(
      "Cancel this trip? All bookings will be affected."
    )) return;
    try {
      await cancelTrip(id);
      fetchAll();
    } catch {
      setError("Failed to cancel trip.");
    }
  };

  // ✅ Format time for display
  const formatTime = (t) => {
    if (!t) return "-";
    const [h, m] = t.split(":");
    const hour = parseInt(h);
    const ampm = hour >= 12 ? "PM" : "AM";
    return `${hour % 12 || 12}:${m} ${ampm}`;
  };

  // ✅ Get status badge class
  const getStatusClass = (status) => {
    switch (status) {
      case "SCHEDULED":  return "status-scheduled";
      case "CANCELLED":  return "status-cancelled";
      case "COMPLETED":  return "status-confirmed";
      default:           return "";
    }
  };

  // ✅ Filter trips
  const filteredTrips = filter === "ALL"
    ? trips
    : trips.filter(t => t.status === filter);

  // ✅ Today for min date
  const today = new Date().toISOString().split("T")[0];

  return (
    <div>
      {/* ✅ Page header */}
      <div className="admin-page-header">
        <h2>📅 Trip Management</h2>
        <button
          className="btn-add"
          onClick={handleOpenAdd}>
          + Create Trip
        </button>
      </div>

      {/* ✅ Filter tabs */}
      <div style={{
        display: "flex",
        gap: "8px",
        marginBottom: "16px",
        flexWrap: "wrap",
      }}>
        {["ALL", "SCHEDULED", "COMPLETED", "CANCELLED"]
          .map(f => (
            <button
              key={f}
              onClick={() => setFilter(f)}
              style={{
                padding: "7px 16px",
                borderRadius: "8px",
                border: "1.5px solid",
                borderColor:
                  filter === f ? "#d84e55" : "#e0e0e0",
                background:
                  filter === f ? "#d84e55" : "white",
                color: filter === f ? "white" : "#555",
                fontWeight: "600",
                fontSize: "13px",
                cursor: "pointer",
                transition: "all 0.2s",
              }}>
              {f}
              {f !== "ALL" && (
                <span style={{
                  marginLeft: "6px",
                  background: filter === f
                    ? "rgba(255,255,255,0.3)"
                    : "#f0f0f0",
                  padding: "1px 6px",
                  borderRadius: "10px",
                  fontSize: "11px",
                }}>
                  {trips.filter(
                    t => t.status === f
                  ).length}
                </span>
              )}
            </button>
          ))}
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
                <th>Bus</th>
                <th>Route</th>
                <th>Date</th>
                <th>Departure</th>
                <th>Arrival</th>
                <th>Price</th>
                <th>Seats Left</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredTrips.length === 0 ? (
                <tr>
                  <td colSpan={10}>
                    <div className="admin-empty">
                      <div className="admin-empty-icon">
                        📅
                      </div>
                      <p>No trips found.</p>
                    </div>
                  </td>
                </tr>
              ) : (
                filteredTrips.map((trip, i) => (
                  <tr key={trip.id}>
                    <td>{i + 1}</td>

                    {/* Bus info */}
                    <td>
                      <div style={{
                        display: "flex",
                        flexDirection: "column",
                        gap: "2px",
                      }}>
                        <strong>{trip.busName}</strong>
                        <span style={{
                          fontSize: "11px",
                          color: "#888",
                        }}>
                          {trip.busNumber}
                        </span>
                      </div>
                    </td>

                    {/* Route */}
                    <td>
                      <span style={{
                        display: "flex",
                        alignItems: "center",
                        gap: "4px",
                        fontSize: "13px",
                      }}>
                        <strong>{trip.source}</strong>
                        <span style={{ color: "#d84e55" }}>
                          →
                        </span>
                        <strong>{trip.destination}</strong>
                      </span>
                    </td>

                    {/* Date */}
                    <td>
                      {new Date(trip.travelDate)
                        .toLocaleDateString("en-IN", {
                          day: "numeric",
                          month: "short",
                          year: "numeric",
                        })}
                    </td>

                    {/* Times */}
                    <td>{formatTime(trip.departureTime)}</td>
                    <td>{formatTime(trip.arrivalTime)}</td>

                    {/* Price */}
                    <td>
                      <strong style={{ color: "#d84e55" }}>
                        ₹{trip.price}
                      </strong>
                    </td>

                    {/* Available seats */}
                    <td>
                      <span style={{
                        color: trip.availableSeats === 0
                          ? "#c62828"
                          : trip.availableSeats <= 5
                            ? "#e65100"
                            : "#2e7d32",
                        fontWeight: "600",
                        fontSize: "13px",
                      }}>
                        {trip.availableSeats}/
                        {trip.totalSeats}
                      </span>
                    </td>

                    {/* Status */}
                    <td>
                      <span className={
                        getStatusClass(trip.status)
                      }>
                        {trip.status}
                      </span>
                    </td>

                    {/* Actions */}
                    <td>
                      {trip.status === "SCHEDULED" && (
                        <>
                          <button
                            className="btn-edit"
                            onClick={() =>
                              handleOpenEdit(trip)}>
                            ✏️ Edit
                          </button>
                          <button
                            className="btn-cancel-booking"
                            onClick={() =>
                              handleCancel(trip.id)}>
                            🚫 Cancel
                          </button>
                        </>
                      )}
                      {trip.status !== "SCHEDULED" && (
                        <span style={{
                          fontSize: "12px",
                          color: "#aaa",
                        }}>
                          No actions
                        </span>
                      )}
                    </td>

                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}

      {/* ✅ Create / Edit Trip Modal */}
      {showModal && (
        <div className="modal-overlay">
          <div className="modal-box"
            style={{ maxWidth: "560px" }}>

            <div className="modal-header">
              <h3>
                {editTrip
                  ? "✏️ Edit Trip"
                  : "📅 Create New Trip"}
              </h3>
              <button
                className="modal-close"
                onClick={() => setShowModal(false)}>
                ✕
              </button>
            </div>

            {/* ✅ Bus selector */}
            <div className="form-group">
              <label>Select Bus *</label>
              <select
                className="form-select"
                value={form.busId}
                onChange={e => setForm({
                  ...form, busId: e.target.value
                })}>
                <option value="">
                  -- Choose a bus --
                </option>
                {buses.map(bus => (
                  <option
                    key={bus.id}
                    value={bus.id}>
                    {bus.name} ({bus.busNumber}) —
                    {bus.busType} — {bus.totalSeats} seats
                  </option>
                ))}
              </select>
            </div>

            {/* ✅ Route selector */}
            <div className="form-group">
              <label>Select Route *</label>
              <select
                className="form-select"
                value={form.routeId}
                onChange={e => setForm({
                  ...form, routeId: e.target.value
                })}>
                <option value="">
                  -- Choose a route --
                </option>
                {routes.map(route => (
                  <option
                    key={route.id}
                    value={route.id}>
                    {route.source} → {route.destination}
                    ({route.distance} km)
                  </option>
                ))}
              </select>
            </div>

            {/* ✅ Travel date */}
            <div className="form-group">
              <label>Travel Date *</label>
              <input
                className="form-input"
                type="date"
                min={today}
                value={form.travelDate}
                onChange={e => setForm({
                  ...form, travelDate: e.target.value
                })}
              />
            </div>

            {/* ✅ Time fields */}
            <div style={{
              display: "grid",
              gridTemplateColumns: "1fr 1fr",
              gap: "14px",
            }}>
              <div className="form-group">
                <label>Departure Time *</label>
                <input
                  className="form-input"
                  type="time"
                  value={form.departureTime}
                  onChange={e => setForm({
                    ...form,
                    departureTime: e.target.value
                  })}
                />
              </div>

              <div className="form-group">
                <label>Arrival Time *</label>
                <input
                  className="form-input"
                  type="time"
                  value={form.arrivalTime}
                  onChange={e => setForm({
                    ...form,
                    arrivalTime: e.target.value
                  })}
                />
              </div>
            </div>

            {/* ✅ Price */}
            <div className="form-group">
              <label>Ticket Price (₹) *</label>
              <input
                className="form-input"
                type="number"
                min={1}
                step={0.01}
                placeholder="e.g. 499"
                value={form.price}
                onChange={e => setForm({
                  ...form, price: e.target.value
                })}
              />
            </div>

            {/* ✅ Preview selected info */}
            {form.busId && form.routeId && (
              <div style={{
                background: "#f0fff4",
                border: "1px solid #a5d6a7",
                borderRadius: "8px",
                padding: "12px 16px",
                fontSize: "13px",
                color: "#2e7d32",
                marginBottom: "4px",
              }}>
                ✅ {buses.find(
                  b => b.id == form.busId
                )?.name} will run on{" "}
                {routes.find(
                  r => r.id == form.routeId
                )?.source} →{" "}
                {routes.find(
                  r => r.id == form.routeId
                )?.destination}
              </div>
            )}

            {error && (
              <div className="error-box"
                style={{ marginBottom: "4px" }}>
                ⚠️ {error}
              </div>
            )}

            <div className="modal-footer">
              <button
                className="btn-secondary"
                onClick={() => setShowModal(false)}>
                Cancel
              </button>
              <button
                className="btn-add"
                onClick={handleSubmit}>
                {editTrip
                  ? "Update Trip"
                  : "Create Trip"}
              </button>
            </div>

          </div>
        </div>
      )}

    </div>
  );
}