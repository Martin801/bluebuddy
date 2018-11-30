package com.bluebuddy.models;

import java.util.ArrayList;

public class AllTrip {

    private boolean status;
    private ArrayList<EventDetails> details;
    private String message;

    /*private ArrayList<EventDetails> trip_details;*/

    private ArrayList<EventDetails> trip_details = new ArrayList<EventDetails>();

    public AllTrip(){
        super();
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<EventDetails> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<EventDetails> details) {
        this.details = details;
    }

    public String toString(){
        return String.valueOf(this.status);
    }

   /* public ArrayList<EventDetails> getTrip_details() {
        return trip_details;
    }

    public void setTrip_details(ArrayList<EventDetails> trip_details) {
        this.trip_details = trip_details;
    }*/

    public ArrayList<EventDetails> getTrip_details() {
        return trip_details;
    }

    public void setTrip_details(ArrayList<EventDetails> trip_details) {
        this.trip_details = trip_details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
