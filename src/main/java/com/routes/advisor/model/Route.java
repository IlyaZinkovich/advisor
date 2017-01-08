package com.routes.advisor.model;

import java.time.LocalDate;

public class Route {

    private Long id;
    private Place fromPlace;
    private Place toPlace;
    private LocalDate date;
    private Period period;
    private String source;

    public enum Period {
        DAY, MONTH
    }

    public Route() {
    }

    public Route(Place fromPlace, Place toPlace, LocalDate date, Period period, String source) {
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.date = date;
        this.source = source;
        this.period = period;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Place getFromPlace() {
        return fromPlace;
    }

    public Place getToPlace() {
        return toPlace;
    }

    public LocalDate getDate() {
        return date;
    }

    public Period getPeriod() {
        return period;
    }

    public String getSource() {
        return source;
    }
}