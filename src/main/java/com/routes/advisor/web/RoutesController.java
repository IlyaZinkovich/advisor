package com.routes.advisor.web;

import com.routes.advisor.client.GoogleGeocodingClient;
import com.routes.advisor.model.Route;
import com.routes.advisor.service.RouteService;
import com.routes.advisor.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class RoutesController {

    @Autowired
    private TripService tripService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private GoogleGeocodingClient geocodingService;

    @GetMapping(path = "/trips")
    public List<String> getTrips(@RequestParam("year") String year,
                                 @RequestParam("month") String month,
                                 @RequestParam("latitude") Double latitude,
                                 @RequestParam("longitude") Double longitude) {
//        routeService.saveRoute(new Route("Minsk", "Brussels", LocalDate.now(), "Flickr"));
        List<Route> routes = routeService.getRoutes("Brussels", LocalDate.of(2017, Month.JANUARY, 1), LocalDate.now());
        if (!routes.isEmpty()) {
            return routes.stream().map(Route::toString).collect(toList());
        }
        return tripService.getTravelers(Year.parse(year), Month.valueOf(month), latitude, longitude);
    }
}
