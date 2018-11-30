package com.bluebuddy.models;

public class Current_user {

    private String firebase_api = "";
    private String firebase_reg = "";


    public String toString(){
        return "firebase_api: " + firebase_api + ", firebase_reg: " + firebase_reg;
    }

    public String getFirebase_api() {
        return firebase_api;
    }

    public void setFirebase_api(String firebase_api) {
        this.firebase_api = firebase_api;
    }

    public String getFirebase_reg() {
        return firebase_reg;
    }

    public void setFirebase_reg(String firebase_reg) {
        this.firebase_reg = firebase_reg;
    }
}
