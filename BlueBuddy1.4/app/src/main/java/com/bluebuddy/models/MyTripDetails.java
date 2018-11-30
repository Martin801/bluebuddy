package com.bluebuddy.models;

import java.io.Serializable;

/**
 * Created by SANANDA on 7/8/2017.
 */

public class MyTripDetails implements Serializable {

    private String event_id;
    private String location;
    private String event_date;
    private String activity;
    private String description;

    public String toString(){
        return "event_id: " + event_id + ", location: " + location + ", event_date: " + event_date + ", activity: " + activity + ", description: " + description;
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
}
