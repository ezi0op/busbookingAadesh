import "../../styles/seat.css";

export default function SeatCell({ seat, isSelected, onToggle }) {

  // ✅ Determine seat state
  const isBooked  = seat.status === "BOOKED";
  const isLocked  = seat.status === "LOCKED";
  const isUpper   = seat.berthType === "UPPER";
  const isSingle  = seat.seatGroup === "SINGLE";

  // ✅ Build CSS class based on state
  const getClass = () => {
    let cls = "seat-cell";
    if (isBooked)      cls += " seat-booked";
    else if (isLocked) cls += " seat-locked";
    else if (isSelected) cls += " seat-selected";
    else               cls += " seat-available";
    if (isUpper)       cls += " seat-upper";
    if (isSingle)      cls += " seat-single";
    return cls;
  };

  // ✅ Tooltip shown on hover
  const getTooltip = () => {
    if (isBooked)  return "Already booked";
    if (isLocked)  return "Temporarily locked";
    if (isSelected) return "Click to deselect";
    return `${seat.seatNumber} | ${seat.berthType} | ${seat.position}`;
  };

  return (
    <div
      className={getClass()}
      title={getTooltip()}
      onClick={() => !isBooked && !isLocked &&
        onToggle(seat)}>

      {/* Seat number */}
      <span className="seat-number">
        {seat.seatNumber}
      </span>

      {/* Small berth indicator dot */}
      <span className="seat-berth-dot" />

    </div>
  );
}