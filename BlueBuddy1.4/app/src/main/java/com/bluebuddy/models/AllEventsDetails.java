package com.bluebuddy.models;

import java.io.Serializable;

public class AllEventsDetails implements Serializable {

    private String event_id = "";
    private String location = "";
    private String event_date = "";
    private String activity = "";
    private String description = "";
    private String creator_fname = "";
    private String creator_lname = "";
    private String creator_dp = "";

    public AllEventsDetails(){
        super();
    }

    public String toString(){
        return "event_id: " + event_id + ", location: " + location + ", event_date: " + event_date + ",  activity: " + activity + ", description: " + description + ",creator_fname:"+creator_fname+", creator_lname: " + creator_lname + ",creator_dp:"+creator_dp+"";
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator_fname() {
        return creator_fname;
    }

    public void setCreator_fname(String creator_fname) {
        this.creator_fname = creator_fname;
    }

    public String getCreator_lname() {
        return creator_lname;
    }

    public void setCreator_lname(String creator_lname) {
        this.creator_lname = creator_lname;
    }

    public String getCreator_dp() {
        return creator_dp;
    }

    public void setCreator_dp(String creator_dp) {
        this.creator_dp = creator_dp;
    }

}
