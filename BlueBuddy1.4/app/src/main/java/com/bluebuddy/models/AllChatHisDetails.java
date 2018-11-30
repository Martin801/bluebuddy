package com.bluebuddy.models;

import java.io.Serializable;

public class AllChatHisDetails implements Serializable {
    private String email="";
    private String profile_img="";
    private String fname="";
    private String lname="";
    private String Firebase_API_key="";

    public String toString(){
        return "email: " + email + ", profile_img: " + profile_img + ", fname: " + fname + ", lname: " + lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
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

    public String getFirebase_API_key() {
        return Firebase_API_key;
    }

    public void setFirebase_API_key(String firebase_API_key) {
        Firebase_API_key = firebase_API_key;
    }
}
