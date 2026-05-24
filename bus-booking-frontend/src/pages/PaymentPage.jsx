import { useState }    from "react";
import { useNavigate } from "react-router-dom";
import { useBooking }  from "../context/BookingContext";
import { processPayment } from "../services/paymentService";
import "../styles/payment.css";

export default function PaymentPage() {
  const navigate = useNavigate();
  const { booking, selectedTrip, selectedSeats } = useBooking();

  const [method,  setMethod]  = useState("ONLINE");
  const [loading, setLoading] = useState(false);
  const [error,   setError]   = useState("");

  // ✅ If no booking — redirect back
  if (!booking) {
    navigate("/");
    return null;
  }

  const handlePayment = async () => {
    setLoading(true);
    setError("");
    try {
      await processPayment(booking.id, { method });
      // ✅ Payment done — go to ticket
      navigate("/ticket");
    } catch (err) {
      setError(
        typeof err === "string"
          ? err
          : "Payment failed. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-container">

      <div className="payment-header">
        <button
          className="btn-back"
          onClick={() => navigate(-1)}>
          ← Back
        </button>
        <div>
          <h2>Complete Payment</h2>
          <p className="payment-subtitle">
            Review your booking and pay
          </p>
        </div>
      </div>

      <div className="payment-layout">

        {/* ✅ LEFT — Payment method */}
        <div className="payment-methods-section">

          <div className="payment-card">
            <h3 className="payment-section-title">
              Select Payment Method
            </h3>

            {/* Payment options */}
            {[
              {
                value: "ONLINE",
                label: "💳 Online Payment",
                desc:  "Credit / Debit Card / Net Banking"
              },
              {
                value: "UPI",
                label: "📱 UPI",
                desc:  "Google Pay, PhonePe, Paytm"
              },
              {
                value: "WALLET",
                label: "👜 Wallet",
                desc:  "Pay from your BusBooking wallet"
              },
              {
                value: "CASH",
                label: "💵 Cash",
                desc:  "Pay cash at the bus stop"
              },
            ].map(opt => (
              <label
                key={opt.value}
                className={`payment-option ${
                  method === opt.value ? "selected" : ""
                }`}>
                <input
                  type="radio"
                  name="paymentMethod"
                  value={opt.value}
                  checked={method === opt.value}
                  onChange={() => setMethod(opt.value)}
                />
                <div className="payment-option-content">
                  <span className="payment-option-label">
                    {opt.label}
                  </span>
                  <span className="payment-option-desc">
                    {opt.desc}
                  </span>
                </div>
                {method === opt.value && (
                  <span className="payment-check">✓</span>
                )}
              </label>
            ))}
          </div>

          {/* ✅ Security note */}
          <div className="payment-security">
            🔒 Your payment is 100% secure and encrypted
          </div>

        </div>

        {/* ✅ RIGHT — Order summary */}
        <div className="payment-summary-section">
          <div className="summary-card">

            <h3 className="summary-title">
              Order Summary
            </h3>

            {/* Trip details */}
            {selectedTrip && (
              <div className="payment-trip-info">
                <div className="pti-row">
                  <span className="pti-label">Bus</span>
                  <span className="pti-value">
                    {selectedTrip.busName}
                  </span>
                </div>
                <div className="pti-row">
                  <span className="pti-label">Route</span>
                  <span className="pti-value">
                    {selectedTrip.source} →{" "}
                    {selectedTrip.destination}
                  </span>
                </div>
                <div className="pti-row">
                  <span className="pti-label">Date</span>
                  <span className="pti-value">
                    {selectedTrip.travelDate}
                  </span>
                </div>
                <div className="pti-row">
                  <span className="pti-label">Seats</span>
                  <span className="pti-value">
                    {selectedSeats.map(
                      s => s.seatNumber
                    ).join(", ")}
                  </span>
                </div>
              </div>
            )}

            <hr className="summary-divider" />

            {/* Price breakdown */}
            <div className="price-breakdown">
              <div className="pb-row">
                <span>
                  {selectedSeats.length} seat(s) ×
                  ₹{selectedTrip?.price}
                </span>
                <span>₹{booking.totalAmount}</span>
              </div>
              <div className="pb-row">
                <span style={{ color: "#43a047" }}>
                  Discount
                </span>
                <span style={{ color: "#43a047" }}>
                  ₹0
                </span>
              </div>
            </div>

            <hr className="summary-divider" />

            {/* Grand total */}
            <div className="grand-total">
              <span>Total Payable</span>
              <span className="grand-total-price">
                ₹{booking.totalAmount}
              </span>
            </div>

            {error && (
              <div className="error-box"
                style={{ marginBottom: "12px" }}>
                ⚠️ {error}
              </div>
            )}

            {/* Pay button */}
            <button
              className="btn-pay"
              onClick={handlePayment}
              disabled={loading}>
              {loading
                ? "Processing..."
                : `Pay ₹${booking.totalAmount} →`}
            </button>

            {/* Cancel note */}
            <p className="cancel-note">
              ✅ Free cancellation | 50% refund to wallet
            </p>

          </div>
        </div>

      </div>
    </div>
  );
}