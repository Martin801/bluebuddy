package com.bluebuddy.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReviewDetail implements Serializable {

    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public List<ReviewDetails> getReview() {
        return review;
    }

    public void setReview(List<ReviewDetails> review) {
        this.review = review;
    }

    private String message;
    private List<ReviewDetails> review = new ArrayList<ReviewDetails>();
    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
