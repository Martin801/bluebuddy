package com.bluebuddy.models;

import java.util.ArrayList;


public class Trip {

    private boolean status;
    private ArrayList<TripDetail> details;

    public Trip(){
        super();
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<TripDetail> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<TripDetail> details) {
        this.details = details;
    }

    public String toString(){
        return String.valueOf(this.status);
    }

}
