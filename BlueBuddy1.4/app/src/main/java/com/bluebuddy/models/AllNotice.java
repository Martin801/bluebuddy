package com.bluebuddy.models;

import java.util.ArrayList;


public class AllNotice {

public String status;
    //private boolean status;
    private String counter;

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    private ArrayList<NoticeDetails> notification_details;

    public ArrayList<NoticeDetails> getNotification_details() {
        return notification_details;
    }

    public void setNotification_details(ArrayList<NoticeDetails> notification_details) {
        this.notification_details = notification_details;
    }

    public AllNotice(){
        super();
    }

   /* public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }*/

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /*public String toString(){
        return String.valueOf(this.status);
    }*/

}
