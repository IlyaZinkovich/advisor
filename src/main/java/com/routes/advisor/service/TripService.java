package com.routes.advisor.service;

import com.routes.advisor.client.FlickrClient;
import com.routes.advisor.client.GoogleGeocodingClient;
import com.routes.advisor.client.StubClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.Year;
import java.util.List;

@Service
public class TripService {

    @Autowired
    private FlickrClient flickrClient;

    @Autowired
    private GoogleGeocodingClient geocodingClient;

    @Autowired
    private StubClient stubClient;

    public List<String> getTravelers(Year year, Month month, Double latitude, Double longitude) {
        return stubClient.serve();
//        return geocodingClient.serve(flickrClient.searchTravelers(year, month, latitude, longitude));
    }
}
