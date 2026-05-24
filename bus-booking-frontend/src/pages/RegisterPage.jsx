import { useState }          from "react";
import { Link, useNavigate } from "react-router-dom";
import { register }          from "../services/authService";

export default function RegisterPage() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: "", email: "",
    password: "", phone: ""
  });
  const [error,   setError]   = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    // ✅ Basic validation
    if (form.password.length < 6) {
      setError("Password must be at least 6 characters");
      setLoading(false);
      return;
    }

    try {
      await register(form);

      // ✅ After register → go to login
      navigate("/login");

    } catch (err) {
      setError(err || "Registration failed.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-container">
      <div className="auth-wrapper">
        <div className="auth-card">

          <div className="auth-header">
            <h1>🚌 BusBooking</h1>
            <h2>Create Account</h2>
            <p>Join us and start booking!</p>
          </div>

          <form onSubmit={handleSubmit}>

            <div className="form-group">
              <label>Full Name</label>
              <input
                className="form-input"
                type="text"
                name="name"
                value={form.name}
                onChange={handleChange}
                placeholder="Enter your full name"
                required
              />
            </div>

            <div className="form-group">
              <label>Email Address</label>
              <input
                className="form-input"
                type="email"
                name="email"
                value={form.email}
                onChange={handleChange}
                placeholder="Enter your email"
                required
              />
            </div>

            <div className="form-group">
              <label>Phone Number</label>
              <input
                className="form-input"
                type="tel"
                name="phone"
                value={form.phone}
                onChange={handleChange}
                placeholder="10-digit mobile number"
                maxLength={10}
              />
            </div>

            <div className="form-group">
              <label>Password</label>
              <input
                className="form-input"
                type="password"
                name="password"
                value={form.password}
                onChange={handleChange}
                placeholder="Minimum 6 characters"
                required
              />
            </div>

            {error && (
              <p className="error-text">⚠️ {error}</p>
            )}

            <button
              type="submit"
              className="btn-primary"
              disabled={loading}
              style={{ marginTop: "8px" }}>
              {loading ? "Registering..." : "Create Account"}
            </button>

          </form>

          <p className="auth-footer">
            Already have an account?{" "}
            <Link to="/login">Login here</Link>
          </p>

        </div>
      </div>

      <style>{`
        .auth-wrapper {
          display: flex;
          justify-content: center;
          align-items: center;
          min-height: calc(100vh - 60px);
          padding: 20px;
        }
        .auth-card {
          background: white;
          border-radius: 16px;
          padding: 40px;
          width: 100%;
          max-width: 420px;
          box-shadow: 0 8px 32px rgba(0,0,0,0.1);
          border: 1px solid #e0e0e0;
        }
        .auth-header {
          text-align: center;
          margin-bottom: 28px;
        }
        .auth-header h1 {
          font-size: 28px;
          color: #d84e55;
          margin-bottom: 8px;
        }
        .auth-header h2 {
          font-size: 20px;
          color: #333;
          margin-bottom: 4px;
        }
        .auth-header p {
          color: #888;
          font-size: 14px;
        }
        .auth-footer {
          text-align: center;
          margin-top: 20px;
          font-size: 14px;
          color: #555;
        }
        .auth-footer a {
          color: #d84e55;
          font-weight: 600;
          text-decoration: none;
        }
        .auth-footer a:hover {
          text-decoration: underline;
        }
      `}</style>

    </div>
  );
}