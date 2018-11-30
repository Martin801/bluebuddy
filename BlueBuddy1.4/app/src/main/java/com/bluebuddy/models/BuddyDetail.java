package com.bluebuddy.models;

import java.io.Serializable;
import java.util.ArrayList;

public class BuddyDetail implements Serializable {

    private String user_id;
    private String fname;
    private String lname;
    private String dp;
    private String location;
    private String latitude;
    private String longitude;
    private String email;

/*
    List<BadgeDetail2> badge;

    public List<BadgeDetail2> getBadge() {
        return badge;
    }

    public void setBadge(List<BadgeDetail2> badge) {
        this.badge = badge;
    }*/


    //  private ArrayList<BadgeDetail2> badge;




  /*  public ArrayList<BadgeDetail2> getBadge() {
        return badge;
    }

    public void setBadge(ArrayList<BadgeDetail2> badge) {
        this.badge = badge;
    }*/

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
