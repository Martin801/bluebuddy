package com.bluebuddy.models;

public class ReviewDetails {

    private String reviewer_fname = "";
    private String reviewer_lname = "";
    private String date = "";
    private String title = "";
    private String rating = "";
    private String description = "";
    private String firebase_reg_id="";
    private String id="";
    private String profile_pic="";
    private String user_id="";


    public String toString(){
        return "reviewer_fname: " + reviewer_fname + ", reviewer_lname: " + reviewer_lname + ", date: " + date + ",  title: " + title + ", rating: " + rating + ",description:"+description + ",firebase_reg_id:"+firebase_reg_id+",id"+id+",profile_pic"+profile_pic+",user_id"+user_id;
    }

    public ReviewDetails(){
        super();
    }

    public String getReviewer_fname() {
        return reviewer_fname;
    }

    public void setReviewer_fname(String reviewer_fname) {
        this.reviewer_fname = reviewer_fname;
    }

    public String getReviewer_lname() {
        return reviewer_lname;
    }

    public void setReviewer_lname(String reviewer_lname) {
        this.reviewer_lname = reviewer_lname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirebase_reg_id() {return firebase_reg_id;}

    public void setFirebase_reg_id(String firebase_reg_id) {this.firebase_reg_id = firebase_reg_id;}

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getProfile_pic() {return profile_pic;}

    public void setProfile_pic(String profile_pic) {this.profile_pic = profile_pic;}

    public String getUser_id() {return user_id;}

    public void setUser_id(String user_id) {this.user_id = user_id;}
}
