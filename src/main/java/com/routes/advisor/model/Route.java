package com.routes.advisor.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Route {

    @Id
    @GeneratedValue
    private Long id;

    private String fromPlace;
    private String toPlace;
    private LocalDate date;
    private String source;

    public class Location {
        private Double latitude;
        private Double longitude;

        public Location() {
        }

        public Location(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
    }

    public Route() {
    }

    public Route(String fromPlace, String toPlace, LocalDate date, String source) {
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.date = date;
        this.source = source;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (id != null ? !id.equals(route.id) : route.id != null) return false;
        if (fromPlace != null ? !fromPlace.equals(route.fromPlace) : route.fromPlace != null) return false;
        if (toPlace != null ? !toPlace.equals(route.toPlace) : route.toPlace != null) return false;
        if (date != null ? !date.equals(route.date) : route.date != null) return false;
        return source != null ? source.equals(route.source) : route.source == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fromPlace != null ? fromPlace.hashCode() : 0);
        result = 31 * result + (toPlace != null ? toPlace.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", fromPlace='" + fromPlace + '\'' +
                ", toPlace='" + toPlace + '\'' +
                ", date=" + date +
                ", source='" + source + '\'' +
                '}';
    }
}