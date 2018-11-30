package com.bluebuddy.models;

import java.util.ArrayList;


public class AllCharter {

    private boolean status;

    private ArrayList<AllCharterDetails> charter_list;


    public ArrayList<AllCharterDetails> getCharter_list() {
        return charter_list;
    }

    public void setCharter_list(ArrayList<AllCharterDetails> charter_list) {
        this.charter_list = charter_list;
    }

    public AllCharter(){
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
