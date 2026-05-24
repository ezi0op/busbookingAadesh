import { useState, useEffect } from "react";
import {
  getAllRoutes, addRoute,
  updateRoute, deleteRoute,
} from "../../services/routeService";
import "../styles/admin.css";

export default function AdminRoutes() {

  const [routes,    setRoutes]    = useState([]);
  const [loading,   setLoading]   = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editRoute, setEditRoute] = useState(null);
  const [error,     setError]     = useState("");

  const emptyForm = {
    source: "", destination: "",
    distance: "", estimatedDurationMinutes: "",
  };
  const [form, setForm] = useState(emptyForm);

  useEffect(() => { fetchRoutes(); }, []);

  const fetchRoutes = async () => {
    setLoading(true);
    try {
      const res = await getAllRoutes();
      setRoutes(res.data || []);
    } catch { setError("Failed to load routes."); }
    finally { setLoading(false); }
  };

  const handleSubmit = async () => {
    try {
      if (editRoute) {
        await updateRoute(editRoute.id, form);
      } else {
        await addRoute(form);
      }
      setShowModal(false);
      fetchRoutes();
    } catch (err) {
      setError(
        typeof err === "string" ? err : "Operation failed."
      );
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this route?")) return;
    try {
      await deleteRoute(id);
      fetchRoutes();
    } catch { setError("Failed to delete route."); }
  };

  return (
    <div>
      <div className="admin-page-header">
        <h2>🗺️ Route Management</h2>
        <button className="btn-add" onClick={() => {
          setEditRoute(null);
          setForm(emptyForm);
          setShowModal(true);
        }}>
          + Add Route
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
                <th>Source</th>
                <th>Destination</th>
                <th>Distance (km)</th>
                <th>Duration (min)</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {routes.length === 0 ? (
                <tr>
                  <td colSpan={6}>
                    <div className="admin-empty">
                      <div className="admin-empty-icon">
                        🗺️
                      </div>
                      <p>No routes found. Add one!</p>
                    </div>
                  </td>
                </tr>
              ) : (
                routes.map((r, i) => (
                  <tr key={r.id}>
                    <td>{i + 1}</td>
                    <td><strong>{r.source}</strong></td>
                    <td><strong>{r.destination}</strong></td>
                    <td>{r.distance} km</td>
                    <td>{r.estimatedDurationMinutes} min</td>
                    <td>
                      <button className="btn-edit"
                        onClick={() => {
                          setEditRoute(r);
                          setForm({
                            source: r.source,
                            destination: r.destination,
                            distance: r.distance,
                            estimatedDurationMinutes:
                              r.estimatedDurationMinutes,
                          });
                          setShowModal(true);
                        }}>
                        ✏️ Edit
                      </button>
                      <button className="btn-delete"
                        onClick={() => handleDelete(r.id)}>
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

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-box">
            <div className="modal-header">
              <h3>
                {editRoute
                  ? "Edit Route" : "Add New Route"}
              </h3>
              <button className="modal-close"
                onClick={() => setShowModal(false)}>
                ✕
              </button>
            </div>

            {[
              { label: "Source City *",
                field: "source",
                placeholder: "e.g. Mumbai" },
              { label: "Destination City *",
                field: "destination",
                placeholder: "e.g. Pune" },
              { label: "Distance (km) *",
                field: "distance",
                placeholder: "e.g. 150",
                type: "number" },
              { label: "Duration (minutes) *",
                field: "estimatedDurationMinutes",
                placeholder: "e.g. 300",
                type: "number" },
            ].map(f => (
              <div key={f.field} className="form-group">
                <label>{f.label}</label>
                <input
                  className="form-input"
                  type={f.type || "text"}
                  placeholder={f.placeholder}
                  value={form[f.field]}
                  onChange={e => setForm({
                    ...form, [f.field]: e.target.value
                  })}
                />
              </div>
            ))}

            <div className="modal-footer">
              <button className="btn-secondary"
                onClick={() => setShowModal(false)}>
                Cancel
              </button>
              <button className="btn-add"
                onClick={handleSubmit}>
                {editRoute
                  ? "Update Route" : "Add Route"}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}