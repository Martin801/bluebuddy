package com.bluebuddy.models;

import java.util.ArrayList;



public class AfterLoginStatus {

    private ArrayList<TripDetail> trip_details;
    private ArrayList<TripDetail> my_participation;
    private ProfileDetails profile_details;
    private ArrayList<CertificateDetail> certification_details;
    private ArrayList<PeopleDetail> user_details_arr;
    private ArrayList<BuddyDetail> buddy_details_arr;

    public ArrayList<BuddyDetail> getBuddy_details_arr() {
        return buddy_details_arr;
    }

    public void setBuddy_details_arr(ArrayList<BuddyDetail> buddy_details_arr) {
        this.buddy_details_arr = buddy_details_arr;
    }

    public ArrayList<TripDetail> getMy_participation() {
        return my_participation;
    }

    public ArrayList<PeopleDetail> getUser_details_arr() {
        return user_details_arr;
    }

    public void setUser_details_arr(ArrayList<PeopleDetail> user_details_arr) {
        this.user_details_arr = user_details_arr;
    }

    public void setMy_participation(ArrayList<TripDetail> my_participation) {
        this.my_participation = my_participation;
    }

    public ArrayList<TripDetail> getTrip_details() {
        return trip_details;
    }

    public void setTrip_details(ArrayList<TripDetail> trip_details) {
        this.trip_details = trip_details;
    }

    public ProfileDetails getProfile_details() {
        return profile_details;
    }

    public void setProfile_details(ProfileDetails profile_details) {
        this.profile_details = profile_details;
    }

    public ArrayList<CertificateDetail> getCertification_details() {
        return certification_details;
    }

    public void setCertification_details(ArrayList<CertificateDetail> certification_details) {
        this.certification_details = certification_details;
    }

    private String Token = "";
    private boolean status;

    public AfterLoginStatus(){
        super();
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
