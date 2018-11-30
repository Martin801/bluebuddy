package com.bluebuddy.models;

import java.io.Serializable;



public class TripDetail implements Serializable {

    private String user_id;
    private String event_id;
    private String location;
    private String event_date;
    private String activity;
    private String description;
    private String participants;
    private String creator_fname;
    private String creator_lname;
    private String creator_dp;

    public TripDetail(){
        super();
    }

    public TripDetail(String user_id, String event_id, String location, String event_date, String activity, String description, String participants, String creator_fname, String creator_lname, String creator_dp){
        this.user_id = user_id;
        this.event_id = event_id;
        this.location = location;
        this.event_date = event_date;
        this.activity = activity;
        this.description = description;
        this.participants = participants;
        this.creator_fname = creator_fname;
        this.creator_lname = creator_lname;
        this.creator_dp = creator_dp;
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

    public String toString(){
        return "event_id:" + this.event_id + ", location:" + this.location + ", event_date:" + this.event_date + ", activity:" + this.activity + ", description:" + this.description + "," +
                " participants:" + this.participants +
        ",creator_lname:" + this.creator_lname +
                ", creator_lname:" + this.creator_lname +
                " ,creator_dp:" + this.creator_dp
        ;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

}
