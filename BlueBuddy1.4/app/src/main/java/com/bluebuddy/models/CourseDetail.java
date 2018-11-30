package com.bluebuddy.models;

import java.io.Serializable;


public class CourseDetail implements Serializable {

    private String category;
    private String agency_name;
    private String agency_url;
    private String duration;
    private String description;
    private String price;
    private String location;
    private String featured;
    private String firebase_api;
    private String firebase_reg_id;
    private String image;
    private String rating;
    private String is_reviewed;
    private String id;
    private  String user_id;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }



    public String getIs_reviewed() {
        return is_reviewed;
    }

    public void setIs_reviewed(String is_reviewed) {
        this.is_reviewed = is_reviewed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public CourseDetail(){
        super();
    }
    public String toString(){
        return "category:" + this.category + ", agency_name:" + this.agency_name
                + ", agency_url:" + this.agency_url + ","
                + ", duration:" + this.duration + ","
                + ", description:" + this.description + ","
                + " price:" + this.price
                + " location:" + this.location
                + ", featured:" + this.featured
                + ", firebase_api:" + this.firebase_api
                + ", firebase_reg:" + this.firebase_reg_id
                + " image:" + this.image;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
    }

    public String getAgency_url() {
        return agency_url;
    }

    public void setAgency_url(String agency_url) {
        this.agency_url = agency_url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirebase_api() {
        return firebase_api;
    }

    public void setFirebase_api(String firebase_api) {
        this.firebase_api = firebase_api;
    }

    public String getFirebase_reg() {
        return firebase_reg_id;
    }

    public void setFirebase_reg(String firebase_reg) {
        this.firebase_reg_id = firebase_reg;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
