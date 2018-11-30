package com.bluebuddy.models;

import java.util.ArrayList;



public class AllReview {

    private boolean status;
    private ArrayList<MyReviews> details;

    public ArrayList<MyReviews> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<MyReviews> details) {
        this.details = details;
    }

    public AllReview(){
        super();
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    /*public ArrayList<AllCharterDetails> getDetails() {
        return charter_list;
    }

    public void setDetails(ArrayList<AllCharterDetails> charter_list) {
        this.charter_list = charter_list;
    }*/

    public String toString(){
        return String.valueOf(this.status);
    }

}
