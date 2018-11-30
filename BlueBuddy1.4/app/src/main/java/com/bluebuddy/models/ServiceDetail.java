package com.bluebuddy.models;

import java.io.Serializable;


public class ServiceDetail implements Serializable {
    private String id = "";
    private String user_id = "";
    private String service_type = "";
    private String Picture = "";
    //  private String Description = "";

    private String location = "";
    private String latitude = "";
    private String longitude = "";


    private String rating = "";

    private String fname = "";
    private String lname = "";

    private String email = "";
    private String phone = "";

    private String is_editable = "";
    private String firebase_api = "";
    private String firebase_reg = "";

    private String image = "";
    private String description = "";

    private String hide_details = "";
    private String is_reviewed;

    public ServiceDetail() {
        super();
    }

    public String getIs_reviewed() {
        return is_reviewed;
    }
    // private List<Current_user> current_user_details = new ArrayList<Current_user>();

    public void setIs_reviewed(String is_reviewed) {
        this.is_reviewed = is_reviewed;
    }

    public String toString() {
        return "id: " + id + ", user_id: " + user_id + ", service_type: " + service_type + ", Picture: " + Picture + ", location: " + location + ",latitude:" + latitude + ", longitude: " + longitude + "," +
                "rating:" + rating + "" +
                "fname:" + fname + "" +
                "lname:" + lname + "" +
                "email:" + email + "" +
                "phone:" + phone + "" +
                "is_editable:" + is_editable + "" +
                "firebase_api:" + firebase_api + "" +
                "firebase_reg:" + firebase_reg + "" +
                "image:" + image + "" +
                "description:" + description +
                "hide_details:" + hide_details;
    }


    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHide_details() {
        return hide_details;
    }

    public void setHide_details(String hide_details) {
        this.hide_details = hide_details;
    }
}
