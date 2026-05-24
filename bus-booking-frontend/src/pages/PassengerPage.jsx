import { useState }     from "react";
import { useNavigate }  from "react-router-dom";
import { useBooking }   from "../context/BookingContext";
import { createBooking } from "../services/bookingService";
import PassengerForm    from "../components/passenger/PassengerForm";
import "../styles/passenger.css";

export default function PassengerPage() {
  const navigate = useNavigate();
  const {
    selectedTrip,
    selectedSeats,
    setPassengers,
    setBooking,
  } = useBooking();

  // ✅ One passenger form per selected seat
  // Initialize with empty passenger for each seat
  const [passengers, setLocalPassengers] = useState(
    selectedSeats.map(seat => ({
      tripSeatId: seat.id,
      name:       "",
      age:        "",
      gender:     "MALE",
    }))
  );

  const [loading, setLoading] = useState(false);
  const [error,   setError]   = useState("");

  // ✅ If no seats selected — redirect back
  if (!selectedSeats || selectedSeats.length === 0) {
    navigate(-1);
    return null;
  }

  // ✅ Update one passenger field
  const handlePassengerChange = (index, field, value) => {
    const updated = [...passengers];
    updated[index] = { ...updated[index], [field]: value };
    setLocalPassengers(updated);
  };

  // ✅ Validate all passengers filled correctly
  const validatePassengers = () => {
    for (let i = 0; i < passengers.length; i++) {
      const p = passengers[i];
      if (!p.name.trim()) {
        setError(`Please enter name for Passenger ${i + 1}`);
        return false;
      }
      if (!p.age || p.age < 1 || p.age > 120) {
        setError(
          `Please enter valid age for Passenger ${i + 1}`
        );
        return false;
      }
      if (!p.gender) {
        setError(
          `Please select gender for Passenger ${i + 1}`
        );
        return false;
      }
    }
    return true;
  };

  // ✅ Submit booking with all passenger details
  const handleSubmit = async () => {
    setError("");
    if (!validatePassengers()) return;

    setLoading(true);
    try {
      const bookingPayload = {
        tripId:      selectedTrip.id,
        tripSeatIds: selectedSeats.map(s => s.id),
        passengers:  passengers,
      };

      const res = await createBooking(bookingPayload);

      // ✅ Save booking in context
      setBooking(res.data);
      setPassengers(passengers);

      // ✅ Go to payment page
      navigate("/payment");

    } catch (err) {
      setError(
        typeof err === "string"
          ? err
          : "Booking failed. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  // ✅ Total price
  const totalPrice = selectedTrip
    ? selectedTrip.price * selectedSeats.length
    : 0;

  return (
    <div className="page-container">

      {/* ✅ Header */}
      <div className="passenger-header">
        <button
          className="btn-back"
          onClick={() => navigate(-1)}>
          ← Back
        </button>
        <div>
          <h2>Passenger Details</h2>
          <p className="passenger-subtitle">
            Fill details for each selected seat
          </p>
        </div>
      </div>

      <div className="passenger-layout">

        {/* ✅ LEFT — Passenger forms */}
        <div className="passenger-forms">
          {selectedSeats.map((seat, index) => (
            <PassengerForm
              key={seat.id}
              index={index}
              seat={seat}
              passenger={passengers[index]}
              onChange={handlePassengerChange}
            />
          ))}

          {error && (
            <div className="error-box">⚠️ {error}</div>
          )}
        </div>

        {/* ✅ RIGHT — Trip + seat summary */}
        <div className="passenger-summary">
          <div className="summary-card">

            <h3 className="summary-title">
              Trip Summary
            </h3>

            {/* Trip info */}
            {selectedTrip && (
              <div className="trip-summary-info">
                <div className="trip-summary-row">
                  <span className="tsrow-label">Bus</span>
                  <span className="tsrow-value">
                    {selectedTrip.busName}
                  </span>
                </div>
                <div className="trip-summary-row">
                  <span className="tsrow-label">Route</span>
                  <span className="tsrow-value">
                    {selectedTrip.source} →{" "}
                    {selectedTrip.destination}
                  </span>
                </div>
                <div className="trip-summary-row">
                  <span className="tsrow-label">Date</span>
                  <span className="tsrow-value">
                    {selectedTrip.travelDate}
                  </span>
                </div>
                <div className="trip-summary-row">
                  <span className="tsrow-label">
                    Departure
                  </span>
                  <span className="tsrow-value">
                    {selectedTrip.departureTime}
                  </span>
                </div>
              </div>
            )}

            <hr className="summary-divider" />

            {/* Seats */}
            <div className="passenger-seat-list">
              <p className="ps-list-title">
                Selected Seats:
              </p>
              {selectedSeats.map((seat, i) => (
                <div key={seat.id} className="ps-seat-row">
                  <span className="ps-seat-num">
                    {seat.seatNumber}
                  </span>
                  <span className="ps-seat-meta">
                    {seat.berthType} | {seat.position}
                  </span>
                  <span className="ps-seat-price">
                    ₹{selectedTrip?.price}
                  </span>
                </div>
              ))}
            </div>

            <hr className="summary-divider" />

            {/* Total */}
            <div className="ps-total">
              <span>Total Amount</span>
              <span className="ps-total-price">
                ₹{totalPrice}
              </span>
            </div>

            {/* Submit button */}
            <button
              className="btn-proceed"
              onClick={handleSubmit}
              disabled={loading}>
              {loading
                ? "Creating Booking..."
                : "Proceed to Payment →"}
            </button>

          </div>
        </div>

      </div>
    </div>
  );
}