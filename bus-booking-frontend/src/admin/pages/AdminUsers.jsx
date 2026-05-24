import { useState, useEffect } from "react";
import {
  getAllUsers, deleteUser
} from "../../services/userService";
import "../styles/admin.css";

export default function AdminUsers() {

  const [users,   setUsers]   = useState([]);
  const [loading, setLoading] = useState(true);
  const [error,   setError]   = useState("");

  useEffect(() => { fetchUsers(); }, []);

  const fetchUsers = async () => {
    setLoading(true);
    try {
      const res = await getAllUsers();
      setUsers(res.data || []);
    } catch { setError("Failed to load users."); }
    finally { setLoading(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm(
      "Delete this user? This cannot be undone."
    )) return;
    try {
      await deleteUser(id);
      fetchUsers();
    } catch { setError("Failed to delete user."); }
  };

  return (
    <div>
      <div className="admin-page-header">
        <h2>👥 User Management</h2>
        <span style={{ color: "#888", fontSize: "14px" }}>
          Total: {users.length} users
        </span>
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
                <th>Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Role</th>
                <th>Registered</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.length === 0 ? (
                <tr>
                  <td colSpan={7}>
                    <div className="admin-empty">
                      <div className="admin-empty-icon">
                        👥
                      </div>
                      <p>No users found.</p>
                    </div>
                  </td>
                </tr>
              ) : (
                users.map((u, i) => (
                  <tr key={u.id}>
                    <td>{i + 1}</td>
                    <td>
                      <div style={{
                        display: "flex",
                        alignItems: "center",
                        gap: "10px"
                      }}>
                        <div style={{
                          width: "32px",
                          height: "32px",
                          background: "#d84e55",
                          borderRadius: "50%",
                          display: "flex",
                          alignItems: "center",
                          justifyContent: "center",
                          color: "white",
                          fontSize: "13px",
                          fontWeight: "700",
                          flexShrink: 0,
                        }}>
                          {u.name?.charAt(0)
                            .toUpperCase()}
                        </div>
                        <strong>{u.name}</strong>
                      </div>
                    </td>
                    <td>{u.email}</td>
                    <td>{u.phone || "-"}</td>
                    <td>
                      <span style={{
                        background: u.role === "ADMIN"
                          ? "#ffebee" : "#e3f2fd",
                        color: u.role === "ADMIN"
                          ? "#c62828" : "#1565c0",
                        padding: "4px 10px",
                        borderRadius: "12px",
                        fontSize: "12px",
                        fontWeight: "600",
                      }}>
                        {u.role}
                      </span>
                    </td>
                    <td>
                      {u.createdAt
                        ? new Date(u.createdAt)
                            .toLocaleDateString()
                        : "-"}
                    </td>
                    <td>
                      {u.role !== "ADMIN" && (
                        <button
                          className="btn-delete"
                          onClick={() =>
                            handleDelete(u.id)}>
                          🗑️ Delete
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