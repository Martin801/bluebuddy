package com.bluebuddy.models;

import java.util.ArrayList;

/**
 * Created by SANANDA on 7/8/2017.
 */

public class InviteBuddyResponse {

    private String status;

    private ArrayList<InviteBuddyDetail> details;



    public ArrayList<InviteBuddyDetail> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<InviteBuddyDetail> details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
