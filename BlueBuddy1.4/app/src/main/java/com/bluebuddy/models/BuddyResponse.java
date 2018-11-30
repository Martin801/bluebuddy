package com.bluebuddy.models;

import java.util.ArrayList;

/**
 * Created by SANANDA on 7/8/2017.
 */

public class BuddyResponse {

    private boolean status;
    private ArrayList<BuddyDetail> details;


    public boolean isStatus() {
        return status;
    }

    public ArrayList<BuddyDetail> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<BuddyDetail> details) {
        this.details = details;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
