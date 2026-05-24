import { useState } from "react";
import "../../styles/search.css";

export default function SearchForm({ onSearch }) {

  const [form, setForm] = useState({
    source:      "",
    destination: "",
    date:        "",
  });

  const [error, setError] = useState("");

  // ✅ Get today's date in YYYY-MM-DD format
  // This is used as min date — can't book past trips
  const today = new Date().toISOString().split("T")[0];

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // ✅ Validate all fields filled
    if (!form.source.trim()) {
      setError("Please enter source city"); return;
    }
    if (!form.destination.trim()) {
      setError("Please enter destination city"); return;
    }
    if (!form.date) {
      setError("Please select travel date"); return;
    }
    if (form.source.toLowerCase() ===
        form.destination.toLowerCase()) {
      setError("Source and destination cannot be same"); return;
    }

    // ✅ Pass to parent (HomePage or SearchPage)
    onSearch(form);
  };

  return (
    <div className="search-box">
      <h2>🔍 Search Buses</h2>

      <form onSubmit={handleSubmit}>
        <div className="search-grid">

          {/* Source */}
          <div className="search-field">
            <label>From</label>
            <input
              type="text"
              name="source"
              value={form.source}
              onChange={handleChange}
              placeholder="e.g. Mumbai"
              autoComplete="off"
            />
          </div>

          {/* Destination */}
          <div className="search-field">
            <label>To</label>
            <input
              type="text"
              name="destination"
              value={form.destination}
              onChange={handleChange}
              placeholder="e.g. Pune"
              autoComplete="off"
            />
          </div>

          {/* Date */}
          <div className="search-field">
            <label>Date</label>
            <input
              type="date"
              name="date"
              value={form.date}
              onChange={handleChange}
              min={today}
            />
          </div>

          {/* Search Button */}
          <button type="submit" className="search-btn">
            Search 🔍
          </button>

        </div>

        {/* Error message */}
        {error && (
          <p className="error-text" style={{ marginTop: "12px" }}>
            ⚠️ {error}
          </p>
        )}
      </form>
    </div>
  );
}