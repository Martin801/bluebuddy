package com.bluebuddy.models;

import java.util.ArrayList;

/**
 * Created by SANANDA on 7/8/2017.
 */

public class MyTripsResponse {

    private boolean status;
    private ArrayList<MyTripDetails> details;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<MyTripDetails> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<MyTripDetails> details) {
        this.details = details;
    }
}
