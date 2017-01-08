package com.routes.advisor.service;

import com.routes.advisor.cache.PlacesCache;
import com.routes.advisor.client.GoogleGeocodingClient;
import com.routes.advisor.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeolocationService {

    @Autowired
    private GoogleGeocodingClient geocodingClient;

    @Autowired
    private PlacesCache placesCache;

    public Place findPlace(String name) {
        Place place = placesCache.findPlace(name);
        if (place != null)
            return place;
        else {
            place = geocodingClient.findPlace(name);
            placesCache.putInCache(name, place);
            return place;
        }
    }
}
