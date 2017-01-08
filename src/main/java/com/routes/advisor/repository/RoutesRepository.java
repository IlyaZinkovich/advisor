package com.routes.advisor.repository;

import com.routes.advisor.model.Place;
import com.routes.advisor.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static com.routes.advisor.model.Route.Period.valueOf;

@Repository
public class RoutesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Query("from Route r where r.toPlace= :destination and r.date >= :after and r.date <= :before")
    public List<Route> findByDestinationAndDataRange(String city, String country, LocalDate after, LocalDate before) {
        return jdbcTemplate.query("SELECT * FROM \"ROUTES\" WHERE \"TO_PLACE_CITY\" = ? AND \"TO_PLACE_COUNTRY\" = ? " +
                "AND \"ROUTE_DATE\" >= ? AND \"ROUTE_DATE\" <= ?",
                new Object[]{city, country, Date.valueOf(after), Date.valueOf(before)},
                (rs, rowNum) -> getRoute(rs));
    }

    private Route getRoute(ResultSet rs) {
        try {
            Place from = new Place(rs.getString("FROM_PLACE_CITY"), rs.getString("FROM_PLACE_COUNTRY"),
                    rs.getDouble("FROM_PLACE_LAT"), rs.getDouble("FROM_PLACE_LNG"));
            Place to = new Place(rs.getString("TO_PLACE_CITY"), rs.getString("TO_PLACE_COUNTRY"),
                    rs.getDouble("TO_PLACE_LAT"), rs.getDouble("TO_PLACE_LNG"));
            return new Route(from, to, rs.getDate("ROUTE_DATE").toLocalDate(), valueOf(rs.getString("PERIOD")),
                    rs.getString("SOURCE"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Route route) {
        Place fromPlace = route.getFromPlace();
        Place toPlace = route.getToPlace();
        jdbcTemplate.update("INSERT INTO \"ROUTES\"" +
                "(\"FROM_PLACE_CITY\", \"FROM_PLACE_COUNTRY\", \"FROM_PLACE_LAT\", \"FROM_PLACE_LNG\", " +
                "\"TO_PLACE_CITY\", \"TO_PLACE_COUNTRY\", \"TO_PLACE_LAT\", \"TO_PLACE_LNG\", " +
                "\"ROUTE_DATE\", \"PERIOD\", \"SOURCE\") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                fromPlace.getCity(), fromPlace.getCountry(), fromPlace.getLatitude(), fromPlace.getLongitude(),
                toPlace.getCity(), toPlace.getCountry(), toPlace.getLatitude(), toPlace.getLongitude(),
                Date.valueOf(route.getDate()), route.getPeriod().toString(), route.getSource());
    }
}