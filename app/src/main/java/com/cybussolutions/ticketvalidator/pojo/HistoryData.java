package com.cybussolutions.ticketvalidator.pojo;

/**
 * Created by AQSA SHaaPARR on 4/18/2017.
 */

public class HistoryData {
    public HistoryData(){}


    String Fare_Price;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    String Date;

    public String getFare_Price() {
        return Fare_Price;
    }

    public void setFare_Price(String fare_Price) {
        Fare_Price = fare_Price;
    }

    public String getRoute_destinition() {
        return route_destinition;
    }

    public void setRoute_destinition(String route_destinition) {
        this.route_destinition = route_destinition;
    }

    public String getRouteStart() {
        return routeStart;
    }

    public void setRouteStart(String routeStart) {
        this.routeStart = routeStart;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPersonTravelling() {
        return personTravelling;
    }

    public void setPersonTravelling(String personTravelling) {
        this.personTravelling = personTravelling;
    }

    String route_destinition;
    String routeStart;
    String time;
    String personTravelling;

    public HistoryData(String Route_destinition,
                       String fare_Price,
                       String RouteStart,
                       String Time,
                       String PersonTravelling){

        this.Fare_Price = fare_Price;
        this.time = Time;
        this.routeStart = RouteStart;
        this.route_destinition = Route_destinition;
        this.personTravelling = PersonTravelling;


    }
}
