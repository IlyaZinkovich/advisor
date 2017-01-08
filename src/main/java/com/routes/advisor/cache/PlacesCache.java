package com.routes.advisor.cache;

import com.routes.advisor.model.Place;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PlacesCache {

    private Map<String, Place> places = new ConcurrentHashMap<>();

    public Place findPlace(String name) {
        return places.get(name);
    }

    public void putInCache(String name, Place place) {
        places.put(name, place);
    }
}
