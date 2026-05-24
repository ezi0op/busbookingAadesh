import { useState, useEffect } from "react";
import { useNavigate }         from "react-router-dom";
import { useBooking }          from "../context/BookingContext";
import { getTicketByBooking,
         downloadTicket }      from "../services/ticketService";
import TicketCard              from "../components/ticket/TicketCard";
import Loader                  from "../components/common/Loader";
import "../styles/ticket.css";

export default function TicketPage() {
  const navigate = useNavigate();
  const { booking, clearBooking } = useBooking();

  const [ticket,   setTicket]   = useState(null);
  const [loading,  setLoading]  = useState(true);
  const [error,    setError]    = useState("");
  const [downloading, setDownloading] = useState(false);

  useEffect(() => {
    if (!booking) {
      navigate("/");
      return;
    }
    fetchTicket();
  }, [booking]);

  const fetchTicket = async () => {
    setLoading(true);
    try {
      const res = await getTicketByBooking(booking.id);
      setTicket(res.data);
    } catch (err) {
      setError("Failed to load ticket.");
    } finally {
      setLoading(false);
    }
  };

  // ✅ Download PDF ticket
  const handleDownload = async () => {
    setDownloading(true);
    try {
      const blob = await downloadTicket(booking.id);
      const url  = window.URL.createObjectURL(
        new Blob([blob])
      );
      const link = document.createElement("a");
      link.href  = url;
      link.setAttribute(
        "download",
        `ticket-${ticket.pnrNumber}.pdf`
      );
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      alert("Download failed. Please try again.");
    } finally {
      setDownloading(false);
    }
  };

  // ✅ Go home and clear booking state
  const handleGoHome = () => {
    clearBooking();
    navigate("/");
  };

  return (
    <div className="page-container">

      {loading && <Loader />}

      {error && (
        <div className="error-box">❌ {error}</div>
      )}

      {!loading && ticket && (
        <>
          {/* ✅ Success banner */}
          <div className="ticket-success-banner">
            <div className="success-icon">🎉</div>
            <div className="success-content">
              <h2>Booking Confirmed!</h2>
              <p>
                Your ticket has been booked successfully.
                Check your email for details.
              </p>
            </div>
          </div>

          {/* ✅ Ticket card */}
          <TicketCard ticket={ticket} />

          {/* ✅ Action buttons */}
          <div className="ticket-actions">
            <button
              className="btn-download"
              onClick={handleDownload}
              disabled={downloading}>
              {downloading
                ? "Downloading..."
                : "⬇ Download PDF Ticket"}
            </button>

            <button
              className="btn-home"
              onClick={handleGoHome}>
              🏠 Back to Home
            </button>
          </div>
        </>
      )}
    </div>
  );
}