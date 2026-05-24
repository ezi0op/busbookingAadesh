import SeatCell from "./SeatCell";
import "../../styles/seat.css";

export default function SeatLayout({ seats, selectedSeats, onToggle }) {

  // ✅ Split seats into LOWER and UPPER deck
  const lowerSeats = seats.filter(
    s => s.berthType === "LOWER"
  );
  const upperSeats = seats.filter(
    s => s.berthType === "UPPER"
  );

  // ✅ Set of selected seat IDs for quick lookup
  const selectedIds = new Set(
    selectedSeats.map(s => s.id)
  );

  // ✅ Group flat seat array into rows
  // Each row: { left: Seat, rightTop: Seat, rightBottom: Seat }
  // LEFT  = SINGLE berth (1 seat per row)
  // RIGHT = DOUBLE berth (2 seats per row stacked)
  const groupIntoRows = (seatList) => {
    const rowMap = {};

    seatList.forEach(seat => {
      const row = seat.rowNumber;
      if (!rowMap[row]) {
        rowMap[row] = {
          left:        null,
          rightTop:    null,
          rightBottom: null,
        };
      }

      if (seat.position === "LEFT") {
        rowMap[row].left = seat;
      } else if (seat.position === "RIGHT") {
        // First RIGHT seat = top, second = bottom
        if (!rowMap[row].rightTop) {
          rowMap[row].rightTop = seat;
        } else {
          rowMap[row].rightBottom = seat;
        }
      }
    });

    // Return sorted by row number
    return Object.entries(rowMap)
      .sort(([a], [b]) => Number(a) - Number(b))
      .map(([rowNum, seats]) => ({
        rowNumber: Number(rowNum),
        ...seats,
      }));
  };

  const lowerRows = groupIntoRows(lowerSeats);
  const upperRows = groupIntoRows(upperSeats);

  // ✅ Render one deck (lower or upper)
  const renderDeck = (rows, deckLabel) => (
    <div className="deck">

      {/* Deck label */}
      <div className="deck-label">
        {deckLabel}
      </div>

      {/* Bus interior */}
      <div className="bus-interior">

        {/* Driver cabin */}
        <div className="driver-cabin">
          <span className="driver-icon">🚌</span>
          <span className="driver-text">Driver</span>
        </div>

        {/* Aisle label row */}
        <div className="aisle-header">
          <div className="side-label">Left (Single)</div>
          <div className="aisle-gap" />
          <div className="side-label">
            Right (Double)
          </div>
        </div>

        {/* Seat rows */}
        {rows.map(row => (
          <div key={row.rowNumber} className="seat-row">

            {/* ✅ LEFT side — SINGLE berth */}
            <div className="seat-side-left">
              {row.left ? (
                <SeatCell
                  seat={row.left}
                  isSelected={selectedIds.has(row.left.id)}
                  onToggle={onToggle}
                />
              ) : (
                <div className="seat-placeholder" />
              )}
            </div>

            {/* ✅ Aisle divider */}
            <div className="aisle">
              <span className="row-number">
                {row.rowNumber}
              </span>
            </div>

            {/* ✅ RIGHT side — DOUBLE berth (stacked) */}
            <div className="seat-side-right">
              <div className="double-berth">
                {row.rightTop ? (
                  <SeatCell
                    seat={row.rightTop}
                    isSelected={
                      selectedIds.has(row.rightTop.id)
                    }
                    onToggle={onToggle}
                  />
                ) : (
                  <div className="seat-placeholder" />
                )}
                {row.rightBottom ? (
                  <SeatCell
                    seat={row.rightBottom}
                    isSelected={
                      selectedIds.has(row.rightBottom.id)
                    }
                    onToggle={onToggle}
                  />
                ) : (
                  <div className="seat-placeholder" />
                )}
              </div>
            </div>

          </div>
        ))}

      </div>
    </div>
  );

  return (
    <div className="seat-layout">

      {/* ✅ Lower Deck */}
      {lowerRows.length > 0 &&
        renderDeck(lowerRows, "Lower Deck")}

      {/* ✅ Deck separator */}
      {lowerRows.length > 0 &&
       upperRows.length > 0 && (
        <div className="deck-separator">
          <span>▲ Upper Deck ▲</span>
        </div>
      )}

      {/* ✅ Upper Deck */}
      {upperRows.length > 0 &&
        renderDeck(upperRows, "Upper Deck")}

    </div>
  );
}