package com.routes.advisor.client;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;
import com.routes.advisor.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Component
public class FlickrClient {

    private static final int FIRST_DAY = 1;
    private static final int FIRST_PAGE = 0;
    private static final int SECOND_PAGE = 1;

    @Autowired
    private Flickr flickr;

    @Value("${flickr.photosPerPage}")
    private int photosPerPage;

    public List<String> searchTravelers(Month month, Year year, Place place) {
        PhotoList<Photo> photos = getPhotos(month, year, place.getLatitude(), place.getLongitude());
        return getPhotosOwnerLocation(photos);
    }

    private PhotoList<Photo> getPhotos(Month month, Year year, double latitude, double longitude) {
        SearchParameters parameters = getSearchParametersForLocation(month, year, latitude, longitude);
        PhotoList<Photo> photos = searchPhotos(parameters, FIRST_PAGE, photosPerPage);
        for (int page = SECOND_PAGE; page < photos.getPages(); page++) {
            photos.addAll(searchPhotos(parameters, page, photosPerPage));
        }
        return photos;
    }

    private static SearchParameters getSearchParametersForLocation(Month month, Year year,
                                                                   double latitude, double longitude) {
        SearchParameters parameters = new SearchParameters();
        parameters.setLatitude(String.valueOf(latitude));
        parameters.setLongitude(String.valueOf(longitude));
        parameters.setMaxTakenDate(Date.valueOf(LocalDate.of(year.getValue(), month, FIRST_DAY)));
        parameters.setMinTakenDate(Date.valueOf(LocalDate.of(year.getValue(), month, month.length(year.isLeap()))));
        return parameters;
    }

    private PhotoList<Photo> searchPhotos(SearchParameters parameters, int page, int perPage) {
        try {
            return flickr.getPhotosInterface().search(parameters, perPage, page);
        } catch (FlickrException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getPhotosOwnerLocation(PhotoList<Photo> photos) {
        return photos.stream()
                .map(Photo::getOwner)
                .distinct()
                .map(this::getUserLocation)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private String getUserLocation(User user) {
        User userInfo = getUserInfo(user.getId());
        return userInfo.getLocation();
    }

    private User getUserInfo(String userId) {
        try {
            return flickr.getPeopleInterface().getInfo(userId);
        } catch (FlickrException e) {
            throw new RuntimeException(e);
        }
    }
}