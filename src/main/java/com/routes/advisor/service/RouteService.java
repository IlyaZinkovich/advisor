package com.routes.advisor.service;

import com.routes.advisor.client.FlickrClient;
import com.routes.advisor.model.GeoTaggedTrip;
import com.routes.advisor.model.Place;
import com.routes.advisor.model.Route;
import com.routes.advisor.model.Trip;
import com.routes.advisor.repository.RoutesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class RouteService {

    @Autowired
    private RoutesRepository routesRepository;

    @Autowired
    private GeolocationService geolocationService;

    @Autowired
    private FlickrClient flickrClient;

    public List<Route> getRoutes(String city, String country, LocalDate startDate, LocalDate endDate) {
        return getRoutes(city, country, startDate.getMonth().toString(), startDate.getYear());
    }

    public List<Route> getRoutes(String city, String country, String month, Integer year) {
        List<Route> routes = routesRepository.findByDestinationAndDataRange(city, country,
                LocalDate.of(year, Month.valueOf(month), 1),
                LocalDate.of(year, Month.valueOf(month), Month.valueOf(month).length(Year.isLeap(year))));
        if (routes.isEmpty()) {
            Place destination = geolocationService.findPlace(city + ", " + country);
            routes = getRoutesFromFlickr(month, year, destination);
            routes.forEach(this::saveRoute);
        }
        return routes;
    }

    private List<Route> getRoutesFromFlickr(String month, Integer year, Place destination) {
        List<Trip> strings = flickrClient.searchTrips(Month.valueOf(month), Year.of(year), destination);
        return strings.stream()
                .map(trip -> new GeoTaggedTrip(geolocationService.findPlace(trip.getOrigin()), trip.getDate()))
                .filter(trip -> !samePlace(destination, trip.getOrigin()))
                .map(trip -> new Route(trip.getOrigin(), destination, trip.getDate(), "Flickr"))
                .collect(toList());
    }

    private boolean samePlace(Place destination, Place origin) {
        if (destination.getCountry() != null && origin.getCountry() != null)
            if (!destination.getCountry().equals(origin.getCountry()))
                return false;
        if (destination.getCity() != null && origin.getCity() != null)
            return destination.getCity().equals(origin.getCity());
        return true;
    }

    public void saveRoute(Route route) {
        routesRepository.save(route);
    }
}
