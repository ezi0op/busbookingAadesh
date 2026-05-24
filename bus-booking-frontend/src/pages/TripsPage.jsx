import { useState, useEffect } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { searchTrips } from "../services/tripService";
import { useBooking } from "../context/BookingContext";
import TripCard from "../components/trips/TripCard";
import SearchForm from "../components/search/SearchForm";
import Loader from "../components/common/Loader";
import "../styles/trips.css";

export default function TripsPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { setSelectedTrip, setSearchParams: setCtxParams } = useBooking();

  const [trips,   setTrips]   = useState([]);
  const [loading, setLoading] = useState(false);
  const [error,   setError]   = useState("");

  // ✅ Filters state
  const [sortBy,       setSortBy]       = useState("price");
  const [filterBus,    setFilterBus]    = useState("ALL");
  const [filterBerth,  setFilterBerth]  = useState("ALL");

  // ✅ Read URL params
  const source      = searchParams.get("source")      || "";
  const destination = searchParams.get("destination") || "";
  const date        = searchParams.get("date")        || "";

  // ✅ Fetch trips when URL params change
  useEffect(() => {
    if (source && destination && date) {
      fetchTrips(source, destination, date);
    }
  }, [source, destination, date]);

  const fetchTrips = async (src, dest, dt) => {
    setLoading(true);
    setError("");
    try {
      const res = await searchTrips(src, dest, dt);
      setTrips(res.data || []);
    } catch (err) {
      setError("Failed to fetch trips. Please try again.");
      setTrips([]);
    } finally {
      setLoading(false);
    }
  };

  // ✅ New search from this page
  const handleSearch = (params) => {
    navigate(
      `/trips?source=${params.source}` +
      `&destination=${params.destination}` +
      `&date=${params.date}`
    );
  };

  // ✅ When user clicks "Select Seats" on a trip
  const handleSelectTrip = (trip) => {
    // Save selected trip in context
    setSelectedTrip(trip);
    // Save search params in context
    setCtxParams({ source, destination, date });
    // Navigate to seat selection
    navigate(`/seats/${trip.id}`);
  };

  // ✅ Apply filters and sorting
  const getFilteredTrips = () => {
    let filtered = [...trips];

    // Filter by bus type
    if (filterBus !== "ALL") {
      filtered = filtered.filter(
        t => t.busType === filterBus
      );
    }

    // Filter by berth type
    if (filterBerth !== "ALL") {
      filtered = filtered.filter(
        t => t.seatType === filterBerth
      );
    }

    // Sort
    if (sortBy === "price") {
      filtered.sort((a, b) => a.price - b.price);
    } else if (sortBy === "departure") {
      filtered.sort((a, b) =>
        a.departureTime.localeCompare(b.departureTime)
      );
    } else if (sortBy === "seats") {
      filtered.sort((a, b) =>
        b.availableSeats - a.availableSeats
      );
    }

    return filtered;
  };

  const filteredTrips = getFilteredTrips();

  return (
    <div>
      {/* ✅ Search bar at top */}
      <div className="trips-search-bar">
        <SearchForm onSearch={handleSearch} />
      </div>

      <div className="page-container">

        {/* ✅ Route + date heading */}
        {source && destination && (
          <div className="trips-heading">
            <h2>
              {source} → {destination}
            </h2>
            <span className="trips-date">
              📅 {new Date(date).toDateString()}
            </span>
          </div>
        )}

        <div className="trips-layout">

          {/* ✅ LEFT — Filters sidebar */}
          <aside className="filters-panel">
            <h3 className="filter-title">🔧 Filters</h3>

            {/* Sort */}
            <div className="filter-section">
              <label className="filter-label">Sort By</label>
              <select
                className="filter-select"
                value={sortBy}
                onChange={e => setSortBy(e.target.value)}>
                <option value="price">Price: Low to High</option>
                <option value="departure">Departure Time</option>
                <option value="seats">Available Seats</option>
              </select>
            </div>

            {/* Bus type filter */}
            <div className="filter-section">
              <label className="filter-label">Bus Type</label>
              {["ALL","AC","NON_AC","SLEEPER","SEMI_SLEEPER"].map(type => (
                <label key={type} className="filter-radio">
                  <input
                    type="radio"
                    name="busType"
                    value={type}
                    checked={filterBus === type}
                    onChange={e => setFilterBus(e.target.value)}
                  />
                  {type === "ALL" ? "All Types" : type.replace("_"," ")}
                </label>
              ))}
            </div>

            {/* Seat type filter */}
            <div className="filter-section">
              <label className="filter-label">Seat Type</label>
              {["ALL","SLEEPER","SEATER"].map(type => (
                <label key={type} className="filter-radio">
                  <input
                    type="radio"
                    name="seatType"
                    value={type}
                    checked={filterBerth === type}
                    onChange={e => setFilterBerth(e.target.value)}
                  />
                  {type === "ALL" ? "All" : type}
                </label>
              ))}
            </div>

            {/* Reset filters */}
            <button
              className="btn-reset"
              onClick={() => {
                setSortBy("price");
                setFilterBus("ALL");
                setFilterBerth("ALL");
              }}>
              Reset Filters
            </button>

          </aside>

          {/* ✅ RIGHT — Trip results */}
          <main className="trips-results">

            {loading && <Loader />}

            {!loading && error && (
              <div className="trips-empty">
                <p>❌ {error}</p>
              </div>
            )}

            {!loading && !error &&
             filteredTrips.length === 0 && (
              <div className="trips-empty">
                <div className="empty-icon">🚌</div>
                <h3>No buses found</h3>
                <p>
                  Try different dates or cities.
                </p>
              </div>
            )}

            {!loading && filteredTrips.map(trip => (
              <TripCard
                key={trip.id}
                trip={trip}
                onSelect={handleSelectTrip}
              />
            ))}

            {/* ✅ Result count */}
            {!loading && filteredTrips.length > 0 && (
              <p className="results-count">
                Showing {filteredTrips.length} bus(es)
              </p>
            )}

          </main>

        </div>
      </div>
    </div>
  );
}