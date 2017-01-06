package com.routes.advisor.client;

import com.google.maps.GeoApiContext;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.google.maps.GeocodingApi.geocode;
import static java.util.stream.Collectors.toList;

@Component
public class GoogleGeocodingClient {

    @Autowired
    private GeoApiContext context;

    public List<String> serve(List<String> addresses) {
        return addresses.stream()
                .map(address -> getLocation(address, context))
                .filter(results -> results.length == 0)
                .map(results -> results[0].geometry.location)
                .map(LatLng::toString).collect(toList());
    }

    private GeocodingResult[] getLocation(String address, GeoApiContext context) {
        try {
            return geocode(context, address).await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
