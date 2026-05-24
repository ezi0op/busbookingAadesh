import "../../styles/trips.css";

export default function TripCard({ trip, onSelect }) {

  // ✅ Format time — "14:30:00" → "2:30 PM"
  const formatTime = (timeStr) => {
    if (!timeStr) return "";
    const [h, m] = timeStr.split(":");
    const hour = parseInt(h);
    const ampm = hour >= 12 ? "PM" : "AM";
    const hour12 = hour % 12 || 12;
    return `${hour12}:${m} ${ampm}`;
  };

  // ✅ Calculate duration from departure and arrival
  const getDuration = (dep, arr) => {
    if (!dep || !arr) return "";
    const [dh, dm] = dep.split(":").map(Number);
    const [ah, am] = arr.split(":").map(Number);
    let mins = (ah * 60 + am) - (dh * 60 + dm);
    if (mins < 0) mins += 24 * 60;
    const hours = Math.floor(mins / 60);
    const minutes = mins % 60;
    return `${hours}h ${minutes}m`;
  };

  // ✅ Seat availability color
  const getSeatColor = (available) => {
    if (available === 0)  return "#e53935"; // red
    if (available <= 5)   return "#fb8c00"; // orange
    return "#43a047";                        // green
  };

  return (
    <div className="trip-card">

      {/* ✅ TOP ROW — bus name + type badge */}
      <div className="trip-card-top">
        <div className="trip-bus-info">
          <h3 className="trip-bus-name">
            🚌 {trip.busName}
          </h3>
          <span className="trip-bus-number">
            {trip.busNumber}
          </span>
        </div>
        <div className="trip-badges">
          {trip.busType && (
            <span className="badge-type">
              {trip.busType.replace("_", " ")}
            </span>
          )}
          {trip.seatType && (
            <span className="badge-seat">
              {trip.seatType}
            </span>
          )}
        </div>
      </div>

      {/* ✅ MIDDLE ROW — time + duration + route */}
      <div className="trip-card-middle">

        {/* Departure */}
        <div className="trip-time-block">
          <div className="trip-time">
            {formatTime(trip.departureTime)}
          </div>
          <div className="trip-city">
            {trip.source}
          </div>
        </div>

        {/* Duration arrow */}
        <div className="trip-duration">
          <div className="duration-line">
            <span className="duration-dot left" />
            <span className="duration-arrow" />
            <span className="duration-dot right" />
          </div>
          <div className="duration-text">
            {getDuration(trip.departureTime, trip.arrivalTime)}
          </div>
        </div>

        {/* Arrival */}
        <div className="trip-time-block right">
          <div className="trip-time">
            {formatTime(trip.arrivalTime)}
          </div>
          <div className="trip-city">
            {trip.destination}
          </div>
        </div>

      </div>

      {/* ✅ BOTTOM ROW — price + seats + button */}
      <div className="trip-card-bottom">

        <div className="trip-bottom-left">
          {/* Price */}
          <div className="trip-price">
            ₹{trip.price}
            <span className="trip-price-label">
              per seat
            </span>
          </div>

          {/* Available seats */}
          <div
            className="trip-seats"
            style={{ color: getSeatColor(trip.availableSeats) }}>
            {trip.availableSeats === 0
              ? "❌ Sold Out"
              : `✅ ${trip.availableSeats} seats available`}
          </div>
        </div>

        {/* Select seats button */}
        <button
          className="btn-select-seats"
          disabled={trip.availableSeats === 0}
          onClick={() => onSelect(trip)}>
          {trip.availableSeats === 0
            ? "Sold Out"
            : "Select Seats →"}
        </button>

      </div>

    </div>
  );
}