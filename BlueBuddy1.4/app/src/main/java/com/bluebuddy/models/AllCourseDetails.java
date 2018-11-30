package com.bluebuddy.models;

import java.io.Serializable;

public class AllCourseDetails implements Serializable {
    private String id = "";
    private String category = "";
    private String location = "";
    private String fname = "";
    private String lname = "";
    private String price = "";
    private String firebase_api = "";
    private String firebase_id = "";
    private String firebase_reg_id="";
    private String featured = "";
    private String image = "";
    private String is_editable = "";
    private String user_id = "";
    private String email = "";
    private String is_flagged = "";
    private String profile_pic="";

    public String toString(){

        return "id: " + id +
                ",category:"+category+
                ",location:"+location+"" +
                ",fname:"+fname+"" +
                ",lname:"+lname+"" +
                ",price:"+price+"" +
                ",firebase_api:"+firebase_api+"" +
                ",firebase_id:"+firebase_id+"" +
                ",featured:"+featured+"" +
                ",is_editable:"+is_editable+"" +
                ",image:"+image+"" +
                ",user_id:"+user_id+"" +
                ",firebase_reg_id"+firebase_reg_id+"" +
                ",email"+email+"" +
                ",is_flagged"+is_flagged+"" +
                ",profile_pic"+profile_pic;
    }

    public String getFirebase_reg_id() {return firebase_reg_id;}

    public void setFirebase_reg_id(String firebase_reg_id) {this.firebase_reg_id = firebase_reg_id;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public AllCourseDetails(){
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFirebase_api() {
        return firebase_api;
    }

    public void setFirebase_api(String firebase_api) {
        this.firebase_api = firebase_api;
    }

    public String getFirebase_id() {
        return firebase_id;
    }

    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
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

    public String getIs_editable() {
        return is_editable;
    }

    public void setIs_editable(String is_editable) {
        this.is_editable = is_editable;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIs_flagged() {
        return is_flagged;
    }

    public void setIs_flagged(String is_flagged) {this.is_flagged = is_flagged;}

    public String getProfile_pic() {return profile_pic;}

    public void setProfile_pic(String profile_pic) {this.profile_pic = profile_pic;}
}
