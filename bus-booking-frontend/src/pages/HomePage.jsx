import { useNavigate } from "react-router-dom";
import { useState }    from "react";
import SearchForm      from "../components/search/SearchForm";
import "../styles/global.css";

export default function HomePage() {
  const navigate = useNavigate();

  // ✅ Called when user submits search form
  const handleSearch = (params) => {
    // Navigate to trips page with search params in URL
    navigate(
      `/trips?source=${params.source}` +
      `&destination=${params.destination}` +
      `&date=${params.date}`
    );
  };

  return (
    <div>

      {/* ✅ Hero Section */}
      <div className="hero">
        <div className="hero-content">
          <h1 className="hero-title">
            Book Bus Tickets Online
          </h1>
          <p className="hero-subtitle">
            India's largest bus ticketing platform.
            Safe, fast and reliable.
          </p>

          {/* ✅ Search Form inside hero */}
          <SearchForm onSearch={handleSearch} />
        </div>
      </div>

      {/* ✅ Features Section */}
      <div className="page-container">
        <h2 className="section-title">Why Choose Us?</h2>
        <div className="features-grid">

          <div className="feature-card">
            <div className="feature-icon">🚌</div>
            <h3>5000+ Bus Routes</h3>
            <p>Covering all major cities and towns across India</p>
          </div>

          <div className="feature-card">
            <div className="feature-icon">🔒</div>
            <h3>Safe & Secure</h3>
            <p>100% secure payments with instant confirmation</p>
          </div>

          <div className="feature-card">
            <div className="feature-icon">💺</div>
            <h3>Choose Your Seat</h3>
            <p>Pick your preferred seat from the interactive layout</p>
          </div>

          <div className="feature-card">
            <div className="feature-icon">📱</div>
            <h3>Easy Cancellation</h3>
            <p>Cancel anytime and get 50% refund to your wallet</p>
          </div>

        </div>
      </div>

    </div>
  );
}