package com.ash.bbus.web.BusTicketBookingSystem.dao.daoImplementation;

import com.ash.bbus.web.BusTicketBookingSystem.dao.TripDao;
import com.ash.bbus.web.BusTicketBookingSystem.entity.Trip;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;

@Repository
public class TripDaoImpl implements TripDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Trip> searchTrips(String source, String destination, LocalDate date) {
		TypedQuery<Trip> query = entityManager.createQuery(
				"SELECT t FROM Trip t " + "JOIN FETCH t.route r " + "JOIN FETCH t.bus b "
						+ "WHERE LOWER(r.source) = LOWER(:source) " + "AND LOWER(r.destination) = LOWER(:destination) "
						+ "AND t.travelDate = :date " + "AND t.status = 'SCHEDULED' " + "AND t.availableSeats > 0",
				Trip.class);
		query.setParameter("source", source);
		query.setParameter("destination", destination);
		query.setParameter("date", date);
		return query.getResultList();
	}

	@Override
	public List<Trip> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, LocalDate date) {
		TypedQuery<Trip> query = entityManager.createQuery("SELECT t FROM Trip t " + "WHERE t.price >= :minPrice "
				+ "AND t.price <= :maxPrice " + "AND t.travelDate = :date " + "AND t.status = 'SCHEDULED'", Trip.class);
		query.setParameter("minPrice", minPrice);
		query.setParameter("maxPrice", maxPrice);
		query.setParameter("date", date);
		return query.getResultList();
	}

	@Override
	public List<Trip> findByBusType(String busType, LocalDate date) {
		TypedQuery<Trip> query = entityManager.createQuery("SELECT t FROM Trip t " + "JOIN FETCH t.bus b "
				+ "WHERE b.busType = :busType " + "AND t.travelDate = :date " + "AND t.status = 'SCHEDULED'",
				Trip.class);
		query.setParameter("busType", com.ash.bbus.web.BusTicketBookingSystem.entity.enums.BusType.valueOf(busType));
		query.setParameter("date", date);
		return query.getResultList();
	}

	@Override
	public List<Trip> findAllSortedByPrice(String source, String destination, LocalDate date) {
		TypedQuery<Trip> query = entityManager.createQuery(
				"SELECT t FROM Trip t " + "JOIN FETCH t.route r " + "JOIN FETCH t.bus b "
						+ "WHERE LOWER(r.source) = LOWER(:source) " + "AND LOWER(r.destination) = LOWER(:destination) "
						+ "AND t.travelDate = :date " + "AND t.status = 'SCHEDULED' " + "ORDER BY t.price ASC",
				Trip.class);
		query.setParameter("source", source);
		query.setParameter("destination", destination);
		query.setParameter("date", date);
		return query.getResultList();
	}

	@Override
	public List<Trip> findAllSortedByDeparture(String source, String destination, LocalDate date) {
		TypedQuery<Trip> query = entityManager.createQuery(
				"SELECT t FROM Trip t " + "JOIN FETCH t.route r " + "JOIN FETCH t.bus b "
						+ "WHERE LOWER(r.source) = LOWER(:source) " + "AND LOWER(r.destination) = LOWER(:destination) "
						+ "AND t.travelDate = :date " + "AND t.status = 'SCHEDULED' " + "ORDER BY t.departureTime ASC",
				Trip.class);
		query.setParameter("source", source);
		query.setParameter("destination", destination);
		query.setParameter("date", date);
		return query.getResultList();
	}
}