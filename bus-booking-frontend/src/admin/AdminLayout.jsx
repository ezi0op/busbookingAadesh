import { NavLink, useNavigate, Outlet }
  from "react-router-dom";
import "./styles/admin.css";

export default function AdminLayout() {
  const navigate = useNavigate();

  const user = JSON.parse(
    localStorage.getItem("user") || "null"
  );

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    navigate("/login");
  };

  const navItems = [
    { to: "/admin",          icon: "📊",
      label: "Dashboard"  },
    { to: "/admin/buses",    icon: "🚌",
      label: "Buses"      },
    { to: "/admin/routes",   icon: "🗺️",
      label: "Routes"     },
    { to: "/admin/trips",    icon: "📅",
      label: "Trips"      },
    { to: "/admin/bookings", icon: "🎫",
      label: "Bookings"   },
    { to: "/admin/users",    icon: "👥",
      label: "Users"      },
  ];

  return (
    <div className="admin-wrapper">

      {/* ✅ SIDEBAR */}
      <aside className="admin-sidebar">

        {/* Logo */}
        <div className="sidebar-logo">
          <span className="logo-icon">🚌</span>
          <span className="logo-text">Admin Panel</span>
        </div>

        {/* Admin user info */}
        <div className="sidebar-user">
          <div className="sidebar-avatar">
            {user?.name?.charAt(0).toUpperCase() || "A"}
          </div>
          <div className="sidebar-user-info">
            <span className="sidebar-user-name">
              {user?.name || "Admin"}
            </span>
            <span className="sidebar-user-role">
              Administrator
            </span>
          </div>
        </div>

        {/* Nav links */}
        <nav className="sidebar-nav">
          {navItems.map(item => (
            <NavLink
              key={item.to}
              to={item.to}
              end={item.to === "/admin"}
              className={({ isActive }) =>
                `sidebar-link ${isActive ? "active" : ""}`
              }>
              <span className="sidebar-icon">
                {item.icon}
              </span>
              <span className="sidebar-label">
                {item.label}
              </span>
            </NavLink>
          ))}
        </nav>

        {/* Bottom actions */}
        <div className="sidebar-bottom">
          <button
            className="sidebar-link"
            style={{ width: "100%", textAlign: "left" }}
            onClick={() => navigate("/")}>
            🏠 View Site
          </button>
          <button
            className="sidebar-link sidebar-logout"
            style={{ width: "100%", textAlign: "left" }}
            onClick={handleLogout}>
            🚪 Logout
          </button>
        </div>

      </aside>

      {/* ✅ MAIN CONTENT */}
      <main className="admin-main">

        {/* Top bar */}
        <div className="admin-topbar">
          <h1 className="topbar-title">
            🚌 Bus Booking Admin
          </h1>
          <div className="topbar-right">
            <span className="topbar-welcome">
              Welcome, {user?.name || "Admin"} 👋
            </span>
          </div>
        </div>

        {/* ✅ Child pages render here */}
        <div className="admin-content">
          <Outlet />
        </div>

      </main>

    </div>
  );
}