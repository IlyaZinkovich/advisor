package com.routes.advisor.service;

import com.routes.advisor.client.FlickrClient;
import com.routes.advisor.model.Place;
import com.routes.advisor.model.Route;
import com.routes.advisor.repository.RoutesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

import static com.routes.advisor.model.Route.Period.MONTH;
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
        List<String> strings = flickrClient.searchTravelers(Month.valueOf(month), Year.of(year), destination);
        return strings.stream().map(geolocationService::findPlace).map(place -> new Route(place, destination,
                LocalDate.of(year, Month.valueOf(month), 1), MONTH, "Flickr"))
                .collect(toList());
    }

    public void saveRoute(Route route) {
        routesRepository.save(route);
    }
}
