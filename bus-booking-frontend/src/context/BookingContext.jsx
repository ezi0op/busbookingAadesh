import { createContext, useContext, useState } from "react";

// ✅ Create context
const BookingContext = createContext();

// ✅ Provider — wraps entire app
export function BookingProvider({ children }) {

  // Search state
  const [searchParams, setSearchParams] = useState({
    source: "",
    destination: "",
    date: "",
  });

  // Trip selection
  const [selectedTrip, setSelectedTrip] = useState(null);

  // Seat selection
  const [selectedSeats, setSelectedSeats] = useState([]);

  // Passenger details — one per seat
  const [passengers, setPassengers] = useState([]);

  // Final booking result
  const [booking, setBooking] = useState(null);

  // Clear all booking state (after ticket shown)
  const clearBooking = () => {
    setSelectedTrip(null);
    setSelectedSeats([]);
    setPassengers([]);
    setBooking(null);
  };

  return (
    <BookingContext.Provider value={{
      searchParams,   setSearchParams,
      selectedTrip,   setSelectedTrip,
      selectedSeats,  setSelectedSeats,
      passengers,     setPassengers,
      booking,        setBooking,
      clearBooking,
    }}>
      {children}
    </BookingContext.Provider>
  );
}

// ✅ Custom hook — use this in any component
export const useBooking = () => useContext(BookingContext);