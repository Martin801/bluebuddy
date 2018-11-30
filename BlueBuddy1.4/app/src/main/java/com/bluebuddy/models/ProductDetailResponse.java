package com.bluebuddy.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 6/19/2017.
 */

public class ProductDetailResponse {

   // private List<ReviewDetails> review = new ArrayList<ReviewDetails>();
   private List<ReviewDetails> review = new ArrayList<ReviewDetails>();
 //   private ArrayList<ReviewDetails> review;
    private ProductDetail details;
   // private List<ReviewDetails> review = new ArrayList<ReviewDetails>();
   // private ReviewDetails review;
    private String Token = "";
    private boolean status;

    public List<ReviewDetails> getReview() {
        return review;
    }

    public void setReview(List<ReviewDetails> review) {
        this.review = review;
    }

    public ProductDetailResponse(){
        super();
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public ProductDetail getDetails() {
        return details;
    }

    public void setDetails(ProductDetail details) {
        this.details = details;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
