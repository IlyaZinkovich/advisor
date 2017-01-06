import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.time.Month.AUGUST;
import static java.util.stream.Collectors.toSet;

public class Main {

    public static Flickr flickr;
    public static Set<String> userIds = new HashSet<>();
    public static String apiKey = "7702646706f40b6c735fec76b0dd1cb5";
    public static String secret = "5a0bf605f48d7d6c";

    static {
        flickr = new Flickr(apiKey, secret, new REST());
    }

//    public static void main(String[] args) throws FileNotFoundException, FlickrException {
//        PrintWriter printWriter = new PrintWriter(new File("output.csv"));
//        searchPhotos(printWriter);
//
//        printWriter.flush();
//        printWriter.close();
//    }

    private static int searchPhotos(PrintWriter printWriter) {
        int page = 0;
        int perPage = 250;
        try {
            SearchParameters parameters = getSearchParametersForTags();
            PhotoList<Photo> photos = new PhotoList<>();
            PhotoList<Photo> newPhotos = flickr.getPhotosInterface().search(parameters, perPage, page);
            while (!newPhotos.isEmpty() && !newPhotos.equals(photos)) {
                photos = newPhotos;
                Set<String> newUserIds = photos.stream().map(photo -> photo.getOwner().getId())
                        .filter(userId -> !userIds.contains(userId))
                        .collect(toSet());
                newUserIds.stream().map(Main::processPhoto)
                        .filter(Objects::nonNull)
                        .forEach(printWriter::println);
                userIds.addAll(newUserIds);
                page++;
                newPhotos = flickr.getPhotosInterface().search(parameters, perPage, page);
                if (page == 100) break;
            }

        } catch (FlickrException e) {
            e.printStackTrace();
        }
        return page;
    }

    private static SearchParameters getSearchParametersForLocation() {
        SearchParameters parameters = new SearchParameters();
        parameters.setLatitude("50.85");
        parameters.setLongitude("4.35");
        parameters.setMaxTakenDate(Date.valueOf(LocalDate.of(2016, AUGUST, 1)));
        parameters.setMinTakenDate(Date.valueOf(LocalDate.of(2016, AUGUST, 31)));
        return parameters;
    }

    private static SearchParameters getSearchParametersForTags() throws FlickrException {
        SearchParameters parameters = new SearchParameters();
//        parameters.setText("Belgique");
        parameters.setTags(new String[]{"Belgium", "Belgique", "Bruxelles"});
        parameters.setMaxTakenDate(Date.valueOf(LocalDate.of(2016, AUGUST, 1)));
        parameters.setMinTakenDate(Date.valueOf(LocalDate.of(2016, AUGUST, 31)));
        return parameters;
    }

    private static String processPhoto(String userId) {
//        System.out.println(photo.getUrl());
        System.out.println(userId);
        try {
            User userInfo = flickr.getPeopleInterface().getInfo(userId);
            return userInfo.getLocation();
//            if (location != null) {
//                System.out.println(location);
//            } else {
//                SearchParameters parameters = new SearchParameters();
//                parameters.setUserId(photo.getOwner().getId());
//                parameters.setExtras(Stream.of("geo", "date_taken").collect(toSet()));
//                PhotoList<Photo> userPhotos = flickr.getPhotosInterface().search(parameters, 250, 0);
//                userPhotos.forEach(userPhoto -> System.out.println(userPhoto.getGeoData() + " " + userPhoto.getDateTaken()));
//            }
        } catch (Exception ignored) {
            System.err.println(ignored);
        }
        return null;
    }
}
