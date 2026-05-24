import { Link } from "react-router-dom";

export default function Footer() {
  return (
    <footer style={{
      background: "#222",
      color: "#aaa",
      marginTop: "60px",
      padding: "40px 16px 20px",
    }}>
      <div style={{
        maxWidth: "1100px",
        margin: "0 auto",
      }}>

        {/* Top grid */}
        <div style={{
          display: "grid",
          gridTemplateColumns:
            "repeat(auto-fit, minmax(200px, 1fr))",
          gap: "32px",
          marginBottom: "32px",
        }}>

          {/* Brand */}
          <div>
            <h3 style={{
              color: "#d84e55",
              fontSize: "20px",
              marginBottom: "12px",
            }}>
              🚌 BusBooking
            </h3>
            <p style={{
              fontSize: "13px",
              lineHeight: "1.7",
            }}>
              India's trusted bus ticket booking platform.
              Book tickets for 5000+ routes across India.
            </p>
          </div>

          {/* Quick links */}
          <div>
            <h4 style={{
              color: "white",
              marginBottom: "12px",
              fontSize: "14px",
            }}>
              Quick Links
            </h4>
            {[
              { to: "/", label: "Home" },
              { to: "/trips", label: "Search Buses" },
              { to: "/login", label: "Login" },
              { to: "/register", label: "Register" },
            ].map(link => (
              <div key={link.to}
                style={{ marginBottom: "8px" }}>
                <Link to={link.to} style={{
                  color: "#aaa",
                  textDecoration: "none",
                  fontSize: "13px",
                  transition: "color 0.2s",
                }}>
                  {link.label}
                </Link>
              </div>
            ))}
          </div>

          {/* Support */}
          <div>
            <h4 style={{
              color: "white",
              marginBottom: "12px",
              fontSize: "14px",
            }}>
              Support
            </h4>
            {[
              "Help Center",
              "Cancellation Policy",
              "Refund Policy",
              "Contact Us",
            ].map(item => (
              <div key={item}
                style={{ marginBottom: "8px" }}>
                <span style={{
                  fontSize: "13px",
                  cursor: "pointer",
                }}>
                  {item}
                </span>
              </div>
            ))}
          </div>

          {/* Contact */}
          <div>
            <h4 style={{
              color: "white",
              marginBottom: "12px",
              fontSize: "14px",
            }}>
              Contact
            </h4>
            <p style={{ fontSize: "13px", marginBottom: "8px" }}>
              📧 support@busbooking.com
            </p>
            <p style={{ fontSize: "13px", marginBottom: "8px" }}>
              📞 1800-XXX-XXXX
            </p>
            <p style={{ fontSize: "13px" }}>
              🕐 24/7 Customer Support
            </p>
          </div>

        </div>

        {/* Bottom bar */}
        <div style={{
          borderTop: "1px solid #333",
          paddingTop: "16px",
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          flexWrap: "wrap",
          gap: "8px",
          fontSize: "12px",
          color: "#666",
        }}>
          <span>
            © {new Date().getFullYear()} BusBooking.
            All rights reserved.
          </span>
          <span>
            Made with ❤️ for India
          </span>
        </div>

      </div>
    </footer>
  );
}