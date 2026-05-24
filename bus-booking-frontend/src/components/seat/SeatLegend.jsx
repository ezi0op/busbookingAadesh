import "../../styles/seat.css";

export default function SeatLegend() {
  return (
    <div className="seat-legend">

      <div className="legend-item">
        <div className="legend-box legend-available" />
        <span>Available</span>
      </div>

      <div className="legend-item">
        <div className="legend-box legend-selected" />
        <span>Selected</span>
      </div>

      <div className="legend-item">
        <div className="legend-box legend-booked" />
        <span>Booked</span>
      </div>

      <div className="legend-item">
        <div className="legend-box legend-locked" />
        <span>Locked</span>
      </div>

      <div className="legend-item">
        <div className="legend-box legend-upper" />
        <span>Upper Berth</span>
      </div>

    </div>
  );
}