import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getSeatsByTrip } from "../services/seatService";
import { useBooking } from "../context/BookingContext";
import SeatLayout from "../components/seat/SeatLayout";
import SeatLegend from "../components/seat/SeatLegend";
import Loader from "../components/common/Loader";
import "../styles/seat.css";

export default function SeatPage() {
  const { tripId }  = useParams();
  const navigate    = useNavigate();
  const {
    selectedTrip,
    selectedSeats,
    setSelectedSeats,
  } = useBooking();

  const [seats,   setSeats]   = useState([]);
  const [loading, setLoading] = useState(true);
  const [error,   setError]   = useState("");

  // ✅ Fetch all seats for this trip
  useEffect(() => {
    const fetchSeats = async () => {
      setLoading(true);
      try {
        const res = await getSeatsByTrip(tripId);
        setSeats(res.data || []);
      } catch (err) {
        setError("Failed to load seats. Please try again.");
      } finally {
        setLoading(false);
      }
    };
    fetchSeats();

    // ✅ Clear previously selected seats
    // when user comes to this page fresh
    setSelectedSeats([]);
  }, [tripId]);

  // ✅ Toggle seat selection
  const handleSeatToggle = (seat) => {
    // Cannot select booked or locked seats
    if (seat.status === "BOOKED" ||
        seat.status === "LOCKED") return;

    const alreadySelected = selectedSeats
      .find(s => s.id === seat.id);

    if (alreadySelected) {
      // Deselect
      setSelectedSeats(
        selectedSeats.filter(s => s.id !== seat.id)
      );
    } else {
      // Select — max 6 seats at once
      if (selectedSeats.length >= 6) {
        alert("You can select maximum 6 seats at a time.");
        return;
      }
      setSelectedSeats([...selectedSeats, seat]);
    }
  };

  // ✅ Proceed to passenger details
  const handleProceed = () => {
    if (selectedSeats.length === 0) {
      alert("Please select at least one seat.");
      return;
    }
    navigate("/passengers");
  };

  // ✅ Calculate total price
  const totalPrice = selectedTrip
    ? selectedTrip.price * selectedSeats.length
    : 0;

  return (
    <div className="page-container">

      {/* ✅ Page heading */}
      <div className="seat-page-header">
        <button
          className="btn-back"
          onClick={() => navigate(-1)}>
          ← Back
        </button>
        <div className="seat-page-title">
          <h2>Select Your Seats</h2>
          {selectedTrip && (
            <p className="seat-route">
              🚌 {selectedTrip.busName} &nbsp;|&nbsp;
              {selectedTrip.source} → {selectedTrip.destination}
              &nbsp;|&nbsp; ₹{selectedTrip.price} per seat
            </p>
          )}
        </div>
      </div>

      {loading && <Loader />}

      {error && (
        <div className="error-box">❌ {error}</div>
      )}

      {!loading && !error && (
        <div className="seat-page-layout">

          {/* ✅ LEFT — Bus seat layout */}
          <div className="seat-layout-section">
            <SeatLegend />
            <SeatLayout
              seats={seats}
              selectedSeats={selectedSeats}
              onToggle={handleSeatToggle}
            />
          </div>

          {/* ✅ RIGHT — Booking summary */}
          <div className="seat-summary">
            <div className="summary-card">
              <h3 className="summary-title">
                Booking Summary
              </h3>

              {selectedSeats.length === 0 ? (
                <p className="summary-empty">
                  No seats selected yet.
                  <br />
                  Click on available seats.
                </p>
              ) : (
                <>
                  {/* Selected seat list */}
                  <div className="summary-seats">
                    {selectedSeats.map(seat => (
                      <div
                        key={seat.id}
                        className="summary-seat-row">
                        <div className="summary-seat-info">
                          <span className="summary-seat-num">
                            {seat.seatNumber}
                          </span>
                          <span className="summary-seat-meta">
                            {seat.berthType} |{" "}
                            {seat.position} |{" "}
                            {seat.seatGroup}
                          </span>
                        </div>
                        <div className="summary-seat-price">
                          ₹{selectedTrip?.price}
                        </div>
                        {/* Remove seat */}
                        <button
                          className="btn-remove-seat"
                          onClick={() =>
                            handleSeatToggle(seat)}>
                          ✕
                        </button>
                      </div>
                    ))}
                  </div>

                  <hr className="summary-divider" />

                  {/* Total */}
                  <div className="summary-total">
                    <span>
                      {selectedSeats.length} Seat(s) × ₹
                      {selectedTrip?.price}
                    </span>
                    <span className="summary-total-price">
                      ₹{totalPrice}
                    </span>
                  </div>

                  {/* Proceed button */}
                  <button
                    className="btn-proceed"
                    onClick={handleProceed}>
                    Proceed to Passengers →
                  </button>
                </>
              )}
            </div>
          </div>

        </div>
      )}
    </div>
  );
}