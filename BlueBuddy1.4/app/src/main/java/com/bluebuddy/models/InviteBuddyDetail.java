package com.bluebuddy.models;

import java.io.Serializable;
import java.util.ArrayList;

public class InviteBuddyDetail implements Serializable {

    private String user_id;
    private String fname;
    private String lname;
    private String dp;
    private String location;
    private String latitude;
    private String longitude;
    private String firebase_api;
    private String firebase_regid;
    private String email;
    private boolean imgCheckUser;


    private ArrayList<BadgeDetail> badgeDetails = new ArrayList<BadgeDetail>();

    public ArrayList<BadgeDetail> getBadgeDetails() {
        return badgeDetails;
    }

    public void setBadgeDetails(ArrayList<BadgeDetail> badgeDetails) {
        this.badgeDetails = badgeDetails;
    }



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getFirebase_api() {
        return firebase_api;
    }

    public void setFirebase_api(String firebase_api) {
        this.firebase_api = firebase_api;
    }

    public String getFirebase_regid() {
        return firebase_regid;
    }

    public void setFirebase_regid(String firebase_regid) {
        this.firebase_regid = firebase_regid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isImgCheckUser() {return imgCheckUser;}

    public void setImgCheckUser(boolean imgCheckUser) {this.imgCheckUser = imgCheckUser;}

}
