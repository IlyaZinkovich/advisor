package com.routes.advisor.service;

import com.routes.advisor.model.Route;
import com.routes.advisor.repository.RoutesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class RouteService {

    @Autowired
    private RoutesRepository routesRepository;

    public List<Route> getRoutes(String to, LocalDate after, LocalDate before) {
        return routesRepository.findByDestinationAndDataRange(to, after, before);
    }

    public void saveRoute(Route route) {
        routesRepository.save(route);
    }
}
