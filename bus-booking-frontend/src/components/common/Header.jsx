import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../../styles/header.css";

export default function Header() {
  const navigate = useNavigate();

  // ✅ Get logged in user from localStorage
  const user = JSON.parse(localStorage.getItem("user") || "null");
  const [menuOpen, setMenuOpen] = useState(false);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    navigate("/login");
  };

  return (
    <header className="header">
      <div className="header-inner">

        {/* ✅ Logo */}
        <Link to="/" className="header-logo">
          🚌 <span>BusBooking</span>
        </Link>

        {/* ✅ Nav Links */}
        <nav className={`header-nav ${menuOpen ? "open" : ""}`}>
          <Link to="/"      className="nav-link">Home</Link>
          <Link to="/trips" className="nav-link">Trips</Link>

          {user ? (
            <>
              {/* ✅ Logged in — show name + logout */}
              <span className="nav-user">
                👤 {user.name}
              </span>
              <button
                className="btn-logout"
                onClick={handleLogout}>
                Logout
              </button>
            </>
          ) : (
            <>
              {/* ✅ Not logged in — show login/register */}
              <Link to="/login"    className="nav-link">Login</Link>
              <Link to="/register" className="btn-register">
                Register
              </Link>
            </>
          )}
        </nav>

        {/* ✅ Mobile hamburger menu */}
        <button
          className="hamburger"
          onClick={() => setMenuOpen(!menuOpen)}>
          {menuOpen ? "✕" : "☰"}
        </button>

      </div>
    </header>
  );
}