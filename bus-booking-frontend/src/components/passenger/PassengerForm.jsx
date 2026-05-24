import "../../styles/passenger.css";

export default function PassengerForm({
  index,
  seat,
  passenger,
  onChange,
}) {
  return (
    <div className="passenger-form-card">

      {/* Card header */}
      <div className="pform-header">
        <div className="pform-badge">
          Passenger {index + 1}
        </div>
        <div className="pform-seat-info">
          <span className="pform-seat-num">
            Seat: {seat.seatNumber}
          </span>
          <span className="pform-seat-meta">
            {seat.berthType} | {seat.position} |{" "}
            {seat.seatGroup}
          </span>
        </div>
      </div>

      {/* Form fields */}
      <div className="pform-body">

        {/* Name */}
        <div className="form-group">
          <label>Full Name *</label>
          <input
            className="form-input"
            type="text"
            placeholder="Enter passenger name"
            value={passenger.name}
            onChange={e =>
              onChange(index, "name", e.target.value)
            }
          />
        </div>

        <div className="pform-row">

          {/* Age */}
          <div className="form-group">
            <label>Age *</label>
            <input
              className="form-input"
              type="number"
              placeholder="Age"
              min={1}
              max={120}
              value={passenger.age}
              onChange={e =>
                onChange(index, "age",
                  parseInt(e.target.value) || "")
              }
            />
          </div>

          {/* Gender */}
          <div className="form-group">
            <label>Gender *</label>
            <select
              className="form-select"
              value={passenger.gender}
              onChange={e =>
                onChange(index, "gender", e.target.value)
              }>
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
              <option value="OTHER">Other</option>
            </select>
          </div>

        </div>

      </div>

    </div>
  );
}