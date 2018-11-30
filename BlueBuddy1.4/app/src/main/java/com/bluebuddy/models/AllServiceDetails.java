package com.bluebuddy.models;

import java.io.Serializable;

public class AllServiceDetails implements Serializable {
    private String Id = "";
    private String User_id = "";
    private String Service_type = "";
    private String Picture = "";
    private String Description = "";

    private String Location_address = "";
    private String Location_lat = "";
    private String Location_long = "";


    private String rating = "";

    private String fname = "";
    private String lname = "";

    private String email = "";
    private String phone = "";

    private String is_editable = "";
    private String firebase_api = "";
    private String firebase_reg = "";
    private String is_flagged = "";
    private String profile_pic = "";
    private String hide_details = "";

    // private List<Current_user> current_user_details = new ArrayList<Current_user>();

    public AllServiceDetails() {
        super();
    }

    public String toString() {
        return "Id: " + Id + ", User_id: " + User_id + ", Service_type: " + Service_type + ", Picture: " + Picture + ",  Description: " + Description + ", Location_address: " + Location_address + ",Location_lat:" + Location_lat + ", Location_long: " + Location_long + "," +
                "rating:" + rating + "" +
                "fname:" + fname + "" +
                "lname:" + lname + "" +
                "email:" + email + "" +
                "phone:" + phone + "" +
                "is_editable:" + is_editable + "" +
                "firebase_api:" + firebase_api + "" +
                "firebase_reg:" + firebase_reg + "" +
                "is_flagged:" + is_flagged + "" +
                "profile_pic:" + profile_pic +
                "hide_details:" + hide_details;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    /*public String getService_type() {
        return Service_type;
    }

    public void setService_type(String service_type) {
        Service_type = service_type;
    }*/

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation_address() {
        return Location_address;
    }

    public void setLocation_address(String location_address) {
        Location_address = location_address;
    }

    public String getLocation_lat() {
        return Location_lat;
    }

    public void setLocation_lat(String location_lat) {
        Location_lat = location_lat;
    }

    public String getLocation_long() {
        return Location_long;
    }

    public void setLocation_long(String location_long) {
        Location_long = location_long;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIs_editable() {
        return is_editable;
    }

    public void setIs_editable(String is_editable) {
        this.is_editable = is_editable;
    }

    public String getFirebase_api() {
        return firebase_api;
    }

    public void setFirebase_api(String firebase_api) {
        this.firebase_api = firebase_api;
    }

    public String getFirebase_reg() {
        return firebase_reg;
    }

    public void setFirebase_reg(String firebase_reg) {
        this.firebase_reg = firebase_reg;
    }

    public String getService_type() {
        return Service_type;
    }

    public void setService_type(String service_type) {
        Service_type = service_type;
    }

    public String getIs_flagged() {
        return is_flagged;
    }

    public void setIs_flagged(String is_flagged) {
        this.is_flagged = is_flagged;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getHide_details() {
        return hide_details;
    }

    public void setHide_details(String hide_details) {
        this.hide_details = hide_details;
    }

}
