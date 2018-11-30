package com.bluebuddy.models;

import java.util.ArrayList;



public class EventDetailsResponse {

    private boolean status;
    private TripDetail tripDetail;

    public TripDetail getTripDetail() {
        return tripDetail;
    }

    public void setTripDetail(TripDetail tripDetail) {
        this.tripDetail = tripDetail;
    }

    private EventDetails details;
    private ArrayList<Participant> participation_list;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public EventDetails getDetails() {
        return details;
    }

    public void setDetails(EventDetails details) {
        this.details = details;
    }

    public ArrayList<Participant> getParticipation_list() {
        return participation_list;
    }

    public void setParticipation_list(ArrayList<Participant> participation_list) {
        this.participation_list = participation_list;
    }
}
