package com.bluebuddy.models;

import java.util.ArrayList;



public class PeopleProfileResponse {

    private boolean status;
    private UserDetails details;


    private String is_buddy;
    private ArrayList<TripDetail> trip_details;
    private ArrayList<CertificateDetail> certification_details;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public UserDetails getDetails() {
        return details;
    }

    public void setDetails(UserDetails details) {
        this.details = details;
    }

    public ArrayList<TripDetail> getTrip_details() {
        return trip_details;
    }

    public void setTrip_details(ArrayList<TripDetail> trip_details) {
        this.trip_details = trip_details;
    }

    public ArrayList<CertificateDetail> getCertification_details() {
        return certification_details;
    }

    public void setCertification_details(ArrayList<CertificateDetail> certification_details) {
        this.certification_details = certification_details;
    }
    public String getIs_buddy() {
        return is_buddy;
    }

    public void setIs_buddy(String is_buddy) {
        this.is_buddy = is_buddy;
    }


}
