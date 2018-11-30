package com.bluebuddy.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AllCharterDetails implements Serializable {

    private String id = "";
    private String fname = "";
    private String lname = "";
    private String image = "";
    private String charter_nm = "";
    private String location = "";
    private String full_price = "";
    private String half_price = "";
    private String featured = "";
    private String price = "";
    private String email = "";
    private String firebase_api = "";
    private String firebase_id = "";
    private String is_editable = "";
    private String user_id = "";
    private String firebase_reg="";
    private String profile_pic="";
    private String is_flagged="";


    private List<Current_user> current_user_details = new ArrayList<Current_user>();

    public List<Current_user> getCurrent_user_details() {
        return current_user_details;
    }

    public void setCurrent_user_details(List<Current_user> current_user_details) {
        this.current_user_details = current_user_details;
    }
    /*private List<current_user_details> current_user_details = new ArrayList<current_user_details>();*/

   /* public List<com.example.admin.bluebuddy.models.current_user_details> getCurrent_user_details() {
        return current_user_details;
    }

    public void setCurrent_user_details(List<com.example.admin.bluebuddy.models.current_user_details> current_user_details) {
        this.current_user_details = current_user_details;
    }*/

    public AllCharterDetails(){
        super();
    }

    public String toString(){
        return "id: " + id + ", fname: " + fname + ", lname: " + lname + ", image: " + image + ",  charter_nm: " + charter_nm + ", location: " + location + ",full_price:"+full_price+", half_price: " + half_price + "," +
                "featured:"+featured+"," +
                "price:"+price+"," +
                "firebase_api:"+firebase_api+"," +
                "firebase_id:"+firebase_id+"," +
                "is_editable:"+is_editable+"," +
                "user_id:"+user_id+"," +
                "firebase_reg:"+firebase_reg+"," +
                "email:"+email+"," +
                "is_flagged:"+is_flagged+","+"pro_pic:"+profile_pic;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getFirebase_reg() {return firebase_reg;}

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIs_editable() {
        return is_editable;
    }

    public void setIs_editable(String is_editable) {
        this.is_editable = is_editable;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCharter_nm() {
        return charter_nm;
    }

    public void setCharter_nm(String charter_nm) {
        this.charter_nm = charter_nm;
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

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }
    public String getIs_flagged() {
        return is_flagged;
    }

    public void setIs_flagged(String is_flagged) {
        this.is_flagged = is_flagged;
    }

    public void setFirebase_reg(String firebase_reg) {this.firebase_reg = firebase_reg;}

    public String getProfile_pic() {return profile_pic;}

    public void setProfile_pic(String profile_pic) {this.profile_pic = profile_pic;}

}
