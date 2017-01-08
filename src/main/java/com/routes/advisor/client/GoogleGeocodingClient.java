package com.routes.advisor.client;

import com.google.maps.GeoApiContext;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.routes.advisor.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.google.maps.GeocodingApi.geocode;
import static com.google.maps.model.AddressComponentType.COUNTRY;
import static com.google.maps.model.AddressComponentType.LOCALITY;
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

    public Place findPlace(String name) {
        GeocodingResult[] geocodingResults = getLocation(name, context);
        if (geocodingResults.length == 0)
            return new Place(name, name, null, null);
        GeocodingResult geocodingResult = geocodingResults[0];
        String city = null;
        String country = null;
        for (AddressComponent addressComponent : geocodingResult.addressComponents) {
            AddressComponentType type = addressComponent.types[0];
            if (LOCALITY.equals(type))
                city = addressComponent.longName;
            else if (COUNTRY.equals(type))
                country = addressComponent.longName;
            if (city != null && country != null) break;
        }
        return new Place(city, country,
                geocodingResult.geometry.location.lat, geocodingResult.geometry.location.lng);
    }

    private GeocodingResult[] getLocation(String address, GeoApiContext context) {
        try {
            return geocode(context, address).await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
