import { useState, useEffect } from "react";
import {
  getAllBuses,
  addBus,
  updateBus,
  deleteBus,
} from "../../services/busService";
import "../styles/admin.css";

export default function AdminBuses() {

  const [buses,   setBuses]   = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editBus,   setEditBus]   = useState(null);
  const [error,     setError]     = useState("");

  const emptyForm = {
    name: "", busNumber: "",
    busType: "AC", seatType: "SLEEPER",
    totalSeats: 40,
  };
  const [form, setForm] = useState(emptyForm);

  useEffect(() => { fetchBuses(); }, []);

  const fetchBuses = async () => {
    setLoading(true);
    try {
      const res = await getAllBuses();
      setBuses(res.data || []);
    } catch {
      setError("Failed to load buses.");
    } finally {
      setLoading(false);
    }
  };

  const handleOpenAdd = () => {
    setEditBus(null);
    setForm(emptyForm);
    setShowModal(true);
  };

  const handleOpenEdit = (bus) => {
    setEditBus(bus);
    setForm({
      name:       bus.name,
      busNumber:  bus.busNumber,
      busType:    bus.busType,
      seatType:   bus.seatType,
      totalSeats: bus.totalSeats,
    });
    setShowModal(true);
  };

  const handleSubmit = async () => {
    try {
      if (editBus) {
        await updateBus(editBus.id, form);
      } else {
        await addBus(form);
      }
      setShowModal(false);
      fetchBuses();
    } catch (err) {
      setError(
        typeof err === "string" ? err : "Operation failed."
      );
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm(
      "Are you sure you want to delete this bus?"
    )) return;
    try {
      await deleteBus(id);
      fetchBuses();
    } catch {
      setError("Failed to delete bus.");
    }
  };

  return (
    <div>
      <div className="admin-page-header">
        <h2>🚌 Bus Management</h2>
        <button
          className="btn-add"
          onClick={handleOpenAdd}>
          + Add Bus
        </button>
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
                <th>Bus Name</th>
                <th>Bus Number</th>
                <th>Type</th>
                <th>Seat Type</th>
                <th>Total Seats</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {buses.length === 0 ? (
                <tr>
                  <td colSpan={7}>
                    <div className="admin-empty">
                      <div className="admin-empty-icon">
                        🚌
                      </div>
                      <p>No buses found. Add one!</p>
                    </div>
                  </td>
                </tr>
              ) : (
                buses.map((bus, i) => (
                  <tr key={bus.id}>
                    <td>{i + 1}</td>
                    <td>
                      <strong>{bus.name}</strong>
                    </td>
                    <td>{bus.busNumber}</td>
                    <td>{bus.busType}</td>
                    <td>{bus.seatType}</td>
                    <td>{bus.totalSeats}</td>
                    <td>
                      <button
                        className="btn-edit"
                        onClick={() =>
                          handleOpenEdit(bus)}>
                        ✏️ Edit
                      </button>
                      <button
                        className="btn-delete"
                        onClick={() =>
                          handleDelete(bus.id)}>
                        🗑️ Delete
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}

      {/* ✅ Add / Edit Modal */}
      {showModal && (
        <div className="modal-overlay">
          <div className="modal-box">
            <div className="modal-header">
              <h3>
                {editBus ? "Edit Bus" : "Add New Bus"}
              </h3>
              <button
                className="modal-close"
                onClick={() => setShowModal(false)}>
                ✕
              </button>
            </div>

            <div className="form-group">
              <label>Bus Name *</label>
              <input
                className="form-input"
                value={form.name}
                onChange={e => setForm({
                  ...form, name: e.target.value
                })}
                placeholder="e.g. Shivneri Express"
              />
            </div>

            <div className="form-group">
              <label>Bus Number *</label>
              <input
                className="form-input"
                value={form.busNumber}
                onChange={e => setForm({
                  ...form, busNumber: e.target.value
                })}
                placeholder="e.g. MH12AB1234"
              />
            </div>

            <div style={{
              display: "grid",
              gridTemplateColumns: "1fr 1fr",
              gap: "14px"
            }}>
              <div className="form-group">
                <label>Bus Type</label>
                <select
                  className="form-select"
                  value={form.busType}
                  onChange={e => setForm({
                    ...form, busType: e.target.value
                  })}>
                  <option value="AC">AC</option>
                  <option value="NON_AC">Non AC</option>
                  <option value="SLEEPER">Sleeper</option>
                  <option value="SEMI_SLEEPER">
                    Semi Sleeper
                  </option>
                </select>
              </div>

              <div className="form-group">
                <label>Seat Type</label>
                <select
                  className="form-select"
                  value={form.seatType}
                  onChange={e => setForm({
                    ...form, seatType: e.target.value
                  })}>
                  <option value="SLEEPER">Sleeper</option>
                  <option value="SEATER">Seater</option>
                </select>
              </div>
            </div>

            <div className="form-group">
              <label>Total Seats *</label>
              <input
                className="form-input"
                type="number"
                min={1}
                max={60}
                value={form.totalSeats}
                onChange={e => setForm({
                  ...form,
                  totalSeats: parseInt(e.target.value)
                })}
              />
            </div>

            <div className="modal-footer">
              <button
                className="btn-secondary"
                onClick={() => setShowModal(false)}>
                Cancel
              </button>
              <button
                className="btn-add"
                onClick={handleSubmit}>
                {editBus ? "Update Bus" : "Add Bus"}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}