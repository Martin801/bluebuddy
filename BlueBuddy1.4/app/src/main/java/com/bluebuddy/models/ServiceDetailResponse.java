package com.bluebuddy.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 6/19/2017.
 */

public class ServiceDetailResponse {

   // private List<ReviewDetails> review = new ArrayList<ReviewDetails>();
   private List<ReviewDetails> review = new ArrayList<ReviewDetails>();
 //   private ArrayList<ReviewDetails> review;
    private ServiceDetail details;
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


    public ServiceDetailResponse(){
        super();
    }

    public ServiceDetail getDetails() {
        return details;
    }

    public void setDetails(ServiceDetail details) {
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
