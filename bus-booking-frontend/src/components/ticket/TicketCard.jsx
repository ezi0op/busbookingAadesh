import "../../styles/ticket.css";

export default function TicketCard({ ticket }) {

  const formatTime = (t) => {
    if (!t) return "";
    const [h, m] = t.split(":");
    const hour = parseInt(h);
    const ampm = hour >= 12 ? "PM" : "AM";
    return `${hour % 12 || 12}:${m} ${ampm}`;
  };

  return (
    <div className="ticket-card">

      {/* ✅ TICKET HEADER */}
      <div className="ticket-header">
        <div className="ticket-logo">🚌 BusBooking</div>
        <div className="ticket-pnr">
          <span className="pnr-label">PNR</span>
          <span className="pnr-value">
            {ticket.pnrNumber}
          </span>
        </div>
        <div className={`ticket-status ${
          ticket.valid ? "status-valid" : "status-invalid"
        }`}>
          {ticket.valid ? "✓ CONFIRMED" : "✗ CANCELLED"}
        </div>
      </div>

      {/* ✅ ROUTE SECTION */}
      <div className="ticket-route">
        <div className="ticket-city-block">
          <div className="ticket-time">
            {formatTime(ticket.departureTime)}
          </div>
          <div className="ticket-city">
            {ticket.source}
          </div>
        </div>

        <div className="ticket-journey">
          <div className="journey-line">
            <span className="journey-dot" />
            <span className="journey-track" />
            <span className="journey-bus">🚌</span>
            <span className="journey-track" />
            <span className="journey-dot" />
          </div>
          <div className="journey-date">
            {ticket.travelDate}
          </div>
        </div>

        <div className="ticket-city-block right">
          <div className="ticket-time">
            {formatTime(ticket.arrivalTime)}
          </div>
          <div className="ticket-city">
            {ticket.destination}
          </div>
        </div>
      </div>

      {/* ✅ BUS INFO */}
      <div className="ticket-bus-info">
        <div className="tbi-item">
          <span className="tbi-label">Bus Name</span>
          <span className="tbi-value">
            {ticket.busName}
          </span>
        </div>
        <div className="tbi-item">
          <span className="tbi-label">Bus Number</span>
          <span className="tbi-value">
            {ticket.busNumber}
          </span>
        </div>
        <div className="tbi-item">
          <span className="tbi-label">Total Fare</span>
          <span className="tbi-value tbi-price">
            ₹{ticket.totalAmount}
          </span>
        </div>
        <div className="tbi-item">
          <span className="tbi-label">Payment</span>
          <span className="tbi-value">
            {ticket.paymentMethod || "Online"}
          </span>
        </div>
      </div>

      {/* ✅ DASHED DIVIDER — perforated ticket look */}
      <div className="ticket-perforation">
        <div className="perf-circle left" />
        <div className="perf-line" />
        <div className="perf-circle right" />
      </div>

      {/* ✅ PASSENGER TABLE */}
      <div className="ticket-passengers">
        <h4 className="tp-title">Passenger Details</h4>
        <table className="tp-table">
          <thead>
            <tr>
              <th>Seat</th>
              <th>Name</th>
              <th>Age</th>
              <th>Gender</th>
              <th>Berth</th>
            </tr>
          </thead>
          <tbody>
            {ticket.passengers?.map((p, i) => {
              const seat = ticket.seats?.find(
                s => s.id === p.tripSeatId
              );
              return (
                <tr key={i}>
                  <td className="tp-seat">
                    {seat?.seatNumber || "-"}
                  </td>
                  <td>{p.name}</td>
                  <td>{p.age}</td>
                  <td>{p.gender}</td>
                  <td>
                    {seat?.berthType || "-"}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      {/* ✅ TICKET FOOTER */}
      <div className="ticket-footer">
        <p>
          🎫 Show this ticket at boarding point
        </p>
        <p>
          Issued: {new Date(
            ticket.issuedAt
          ).toLocaleString()}
        </p>
      </div>

    </div>
  );
}