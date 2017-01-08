package com.routes.advisor.web;

import com.routes.advisor.model.Route;
import com.routes.advisor.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class RoutesController {

    @Autowired
    private RouteService routeService;

    @GetMapping(path = "/routes")
    public List<Route> getRoutes(@RequestParam("city") String city,
                                 @RequestParam("country") String country,
                                 @RequestParam("startDate")
                                 @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                 @RequestParam("endDate")
                                 @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return routeService.getRoutes(city, country, startDate, endDate);
    }
}
