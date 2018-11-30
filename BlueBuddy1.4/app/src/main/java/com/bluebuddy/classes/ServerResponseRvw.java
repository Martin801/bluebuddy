package com.bluebuddy.classes;


import com.bluebuddy.models.ReviewDetail;

import java.util.ArrayList;

public class ServerResponseRvw {
    private ArrayList<ReviewDetail> detail;
    public ArrayList<ReviewDetail> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<ReviewDetail> detail) {
        this.detail = detail;
    }




    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
    private String rev_title;
    private String rev_description;
    private String rev_rating;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRev_title() {
        return rev_title;
    }

    public void setRev_title(String rev_title) {
        this.rev_title = rev_title;
    }

    public String getRev_description() {
        return rev_description;
    }

    public void setRev_description(String rev_description) {
        this.rev_description = rev_description;
    }

    public String getRev_rating() {
        return rev_rating;
    }

    public void setRev_rating(String rev_rating) {
        this.rev_rating = rev_rating;
    }

    public String getRev_for() {
        return rev_for;
    }

    public void setRev_for(String rev_for) {
        this.rev_for = rev_for;
    }

    public String getFor_id() {
        return for_id;
    }

    public void setFor_id(String for_id) {
        this.for_id = for_id;
    }

    private String rev_for;
    private String for_id;



}