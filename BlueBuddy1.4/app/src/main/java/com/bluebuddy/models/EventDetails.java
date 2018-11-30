package com.bluebuddy.models;

import java.io.Serializable;
import java.util.ArrayList;


public class EventDetails implements Serializable {
    private String event_id;
    private String user_id;
    private String location;
    private String event_date;
    private String activity;
    private String description;
    private String participants;
    private String creator_fname;
    private String creator_lname;
    private String creator_dp;
    private String creator_email;
    private String creator_firebase_api_key;
    private String creator_firebase_reg_id;
    private String participated;
    private String no_of_participant;
    private String is_editable;
    private String is_flagged;


    private ArrayList<EventDetails> trip_details = new ArrayList<EventDetails>();

    public EventDetails() {
        super();
    }

//    public EventDetails(String event_id, String user_id, String location, String event_date, String activity, String fragment, String description, String participants, String creator_fname, String creator_lname, String creator_dp, String creator_email, String creator_firebase_api_key, String creator_firebase_reg_id, String participated, String no_of_participant, String is_editable, String is_flagged) {
//        this.event_id = event_id;
//        this.user_id = user_id;
//        this.location = location;
//        this.event_date = event_date;
//        this.activity = activity;
//        this.description = description;
//        this.participants = participants;
//        this.creator_fname = creator_fname;
//        this.creator_lname = creator_lname;
//        this.creator_dp = creator_dp;
//        this.creator_email = creator_email;
//        this.creator_firebase_api_key = creator_firebase_api_key;
//        this.creator_firebase_reg_id = creator_firebase_reg_id;
//        this.participated = participated;
//        this.no_of_participant = no_of_participant;
//        this.is_editable = is_editable;
//        this.is_flagged = is_flagged;
//    }

    public EventDetails(String event_id, String user_id, String location, String event_date, String activity, String fragment, String description, String participants, String creator_fname, String creator_lname, String creator_dp, String creator_email, String creator_firebase_api_key, String creator_firebase_reg_id, String participated, String no_of_participant, String is_editable) {
        this.event_id = event_id;
        this.user_id = user_id;
        this.location = location;
        this.event_date = event_date;
        this.activity = activity;
        this.description = description;
        this.participants = participants;
        this.creator_fname = creator_fname;
        this.creator_lname = creator_lname;
        this.creator_dp = creator_dp;
        this.creator_email = creator_email;
        this.creator_firebase_api_key = creator_firebase_api_key;
        this.creator_firebase_reg_id = creator_firebase_reg_id;
        this.participated = participated;
        this.no_of_participant = no_of_participant;
        this.is_editable = is_editable;
    }

    public String toString() {
        return "event_id: " + event_id + ", is_editable: " + is_editable + ", location: " + is_editable + ", event_date: " + event_date + ", activity: " + activity + ", description: " + description + ", participants: " + participants + ", creator_fname: " + creator_fname + ", creator_lname: " + creator_lname + " , is_flagged: " + is_flagged;
    }

    public ArrayList<EventDetails> getTrip_details() {
        return trip_details;
    }

    public void setTrip_details(ArrayList<EventDetails> trip_details) {
        this.trip_details = trip_details;
    }

    public String getCreator_dp() {
        return creator_dp;
    }

    public void setCreator_dp(String creator_dp) {
        this.creator_dp = creator_dp;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreator_email() {
        return creator_email;
    }

    public void setCreator_email(String creator_email) {
        this.creator_email = creator_email;
    }

    public String getCreator_firebase_api_key() {
        return creator_firebase_api_key;
    }

    public void setCreator_firebase_api_key(String creator_firebase_api_key) {
        this.creator_firebase_api_key = creator_firebase_api_key;
    }

    public String getCreator_firebase_reg_id() {
        return creator_firebase_reg_id;
    }

    public void setCreator_firebase_reg_id(String creator_firebase_reg_id) {
        this.creator_firebase_reg_id = creator_firebase_reg_id;
    }

    public String getParticipated() {
        return participated;
    }

    public void setParticipated(String participated) {
        this.participated = participated;
    }

    public String getNo_of_participant() {
        return no_of_participant;
    }

    public void setNo_of_participant(String no_of_participant) {
        this.no_of_participant = no_of_participant;
    }

    public String getIs_editable() {
        return is_editable;
    }

    public void setIs_editable(String is_editable) {
        this.is_editable = is_editable;
    }

    public String getIs_flagged() {
        return is_flagged;
    }

    public void setIs_flagged(String is_flagged) {
        this.is_flagged = is_flagged;
    }

}
