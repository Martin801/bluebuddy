package com.bluebuddy.models;



public class Interest {

    private boolean imgInterest;
    private String interestName;

    public Interest(boolean imgInterest, String interestName) {
        this.imgInterest = imgInterest;
        this.interestName = interestName;
    }

    public boolean getImgInterest() {
        return imgInterest;
    }

    public void setImgInterest(boolean imgInterest) {
        this.imgInterest = imgInterest;
    }

    public String getInterestName() {
        return interestName;
    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

}
