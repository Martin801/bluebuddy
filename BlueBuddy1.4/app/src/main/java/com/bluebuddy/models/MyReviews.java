package com.bluebuddy.models;

import java.util.ArrayList;

/**
 * Created by USER on 5/28/2017.
 */

public class MyReviews {

    //  private String RvwerName;
    private String fname;
    private String lname;
    private String review_dt;
    private String title;
    private String rating;
    private String description;
    private String Rev_for;
    private String For_id;
    private ArrayList<ForDetails> fordetails;

  /*  private String RvwerName;
    private String RvwDate;
    private String RvwComplmnt;
    private String Rvw;*/

//    public MyReviews(String fname, String lname, String review_dt, String title, String rating, String description, String rev_for, String for_id, ArrayList<ForDetails> forDetails) {
//        this.fname = fname;
//        this.lname = lname;
//        this.review_dt = review_dt;
//        this.title = title;
//        this.rating = rating;
//        this.description = description;
//        Rev_for = rev_for;
//        For_id = for_id;
//        this.forDetails = forDetails;
//    }

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

    public String getReview_dt() {
        return review_dt;
    }

    public void setReview_dt(String review_dt) {
        this.review_dt = review_dt;
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

    public String getRev_for() {
        return Rev_for;
    }

    public void setRev_for(String rev_for) {
        Rev_for = rev_for;
    }

    public String getFor_id() {
        return For_id;
    }

    public void setFor_id(String for_id) {
        For_id = for_id;
    }

    public ArrayList<ForDetails> getFordetails() {
        return fordetails;
    }

    public void setFordetails(ArrayList<ForDetails> fordetails) {
        this.fordetails = fordetails;
    }
//    public MyReviews(String fname,String lname,String review_dt, String title, String rating, String description) {
//        this.fname=fname;
//        this.lname=lname;
//        this.review_dt = review_dt;
//        this.title = title;
//        this.rating = rating;
//        this.description = description;
//
//    }
//
//    public String getReview_dt() {
//        return review_dt;
//    }
//
//    public String getFname() {
//        return fname;
//    }
//
//    public void setFname(String fname) {
//        this.fname = fname;
//    }
//
//    public String getLname() {
//        return lname;
//    }
//
//    public void setLname(String lname) {
//        this.lname = lname;
//    }
//
//    public void setReview_dt(String review_dt) {
//        this.review_dt = review_dt;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getRating() {
//        return rating;
//    }
//
//    public void setRating(String rating) {
//        this.rating = rating;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
}
