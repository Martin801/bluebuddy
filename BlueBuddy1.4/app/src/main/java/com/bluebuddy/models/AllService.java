package com.bluebuddy.models;

import java.util.ArrayList;



public class AllService {

    private boolean status;

    private ArrayList<AllServiceDetails> service_list;

    public ArrayList<AllServiceDetails> getService_list() {
        return service_list;
    }

    public void setService_list(ArrayList<AllServiceDetails> service_list) {
        this.service_list = service_list;
    }

/* public ArrayList<AllCharterDetails> getCharter_list() {
        return charter_list;
    }

    public void setCharter_list(ArrayList<AllCharterDetails> charter_list) {
        this.charter_list = charter_list;
    }*/

    public AllService(){
        super();
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String toString(){
        return String.valueOf(this.status);
    }

}
