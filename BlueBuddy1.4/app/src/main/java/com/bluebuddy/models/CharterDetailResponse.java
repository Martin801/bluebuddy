package com.bluebuddy.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 6/19/2017.
 */

public class CharterDetailResponse {

   // private List<ReviewDetails> review = new ArrayList<ReviewDetails>();
   private List<ReviewDetails> review = new ArrayList<ReviewDetails>();
 //   private ArrayList<ReviewDetails> review;
    private CharterDetail details;
   // private List<ReviewDetails> review = new ArrayList<ReviewDetails>();
   // private ReviewDetails review;
    private AllServiceDetails sdt;
    private String Token = "";
    private boolean status;

    public AllServiceDetails getSdt() {
        return sdt;
    }

    public void setSdt(AllServiceDetails sdt) {
        this.sdt = sdt;
    }

    public List<ReviewDetails> getReview() {
        return review;
    }

    public void setReview(List<ReviewDetails> review) {
        this.review = review;
    }

    public CharterDetail getDetails() {
        return details;
    }

    public CharterDetailResponse(){
        super();
    }

    public void setDetails(CharterDetail details) {
        this.details = details;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
