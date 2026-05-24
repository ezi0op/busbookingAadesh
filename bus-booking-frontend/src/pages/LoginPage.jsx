import { useState }      from "react";
import { Link, useNavigate } from "react-router-dom";
import { login }         from "../services/authService";

export default function LoginPage() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    email: "", password: ""
  });
  const [error,   setError]   = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
  };

  // ✅ After successful login — redirect based on role
const handleSubmit = async (e) => {
  e.preventDefault();
  setLoading(true);
  setError("");

  try {
    const res = await login(form);

    // ✅ Check response structure
    console.log("Full response:", res);

    // ✅ Handle both response structures
    // Some interceptors return res directly
    // Some return res.data
    const data = res?.data || res;
    console.log("Data:", data);

    const token = data?.token;
    const userId = data?.userId;
    const name  = data?.name;
    const email = data?.email;
    const role  = data?.role;

    console.log("Token:", token);
    console.log("Role:", role);

    if (!token) {
      setError("Login failed — no token received");
      return;
    }

    // ✅ Save to localStorage
    localStorage.setItem("token", token);
    localStorage.setItem("user", JSON.stringify({
      id: userId, name, email, role
    }));

    // ✅ Redirect based on role
    if (role === "ADMIN") {
      navigate("/admin");
    } else {
      navigate("/");
    }

  } catch (err) {
    console.log("Login error details:", err);
    console.log("Error type:", typeof err);

    // ✅ Handle different error formats
    if (typeof err === "string") {
      setError(err);
    } else if (err?.message) {
      setError(err.message);
    } else {
      setError("Login failed. Check credentials.");
    }
  } finally {
    setLoading(false);
  }
};

  return (
    <div className="page-container">
      <div className="auth-wrapper">
        <div className="auth-card">

          {/* Header */}
          <div className="auth-header">
            <h1>🚌 BusBooking</h1>
            <h2>Welcome Back!</h2>
            <p>Login to your account</p>
          </div>

          {/* Form */}
          <form onSubmit={handleSubmit}>

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
              <label>Password</label>
              <input
                className="form-input"
                type="password"
                name="password"
                value={form.password}
                onChange={handleChange}
                placeholder="Enter your password"
                required
              />
            </div>

            {/* Error */}
            {error && (
              <p className="error-text">⚠️ {error}</p>
            )}

            {/* Submit */}
            <button
              type="submit"
              className="btn-primary"
              disabled={loading}
              style={{ marginTop: "8px" }}>
              {loading ? "Logging in..." : "Login"}
            </button>

          </form>

          {/* Register link */}
          <p className="auth-footer">
            Don't have an account?{" "}
            <Link to="/register">Register here</Link>
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