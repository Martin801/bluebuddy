package com.bluebuddy.models;

import java.io.Serializable;


public class CharterDetail implements Serializable {

    private String id;
    private String image;
    private String name;
    private String description;
    private String location;
    private String full_price;
    private String half_price;
    private String type_of_boat;
    private String size;
    private String capacity;
    private String featured;
    private String fname;
    private String lname;
    private String phone;
    private String email;
    private String firebase_api;
    private String firebase_reg;
    private String rating;
    private String is_reviewed;
    private String hide_details;
    private String user_id;

    public CharterDetail() {
        super();
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_reviewed() {
        return is_reviewed;
    }

    public void setIs_reviewed(String is_reviewed) {
        this.is_reviewed = is_reviewed;
    }

    public String toString() {
        return "image:" + this.image + ", name:" + this.name + ", description:" + this.description + ", location:" + this.location + ", full_price:" + this.full_price + ", half_price:" + this.half_price
                + ", type_of_boat:" + this.type_of_boat + ", size:" + this.size + ", capacity:" + this.capacity
                + ", featured:" + this.featured
                + ", fname:" + this.fname + ", lname:" + this.lname + ", phone:" + this.phone
                + ", email:" + this.email
                + ", firebase_api:" + this.firebase_api
                + ", firebase_reg:" + this.firebase_reg
                + ", rating:" + this.rating
                + ", hide_details:" + this.hide_details;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getFull_price() {
        return full_price;
    }

    public void setFull_price(String full_price) {
        this.full_price = full_price;
    }

    public String getHalf_price() {
        return half_price;
    }

    public void setHalf_price(String half_price) {
        this.half_price = half_price;
    }

    public String getType_of_boat() {
        return type_of_boat;
    }

    public void setType_of_boat(String type_of_boat) {
        this.type_of_boat = type_of_boat;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

    public String getHide_details() {
        return hide_details;
    }

    public void setHide_details(String hide_details) {
        this.hide_details = hide_details;
    }
}
