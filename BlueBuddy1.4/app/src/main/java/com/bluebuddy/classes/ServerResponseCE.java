package com.bluebuddy.classes;

import com.bluebuddy.models.EventDetails;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServerResponseCE implements Serializable{
    @SerializedName("returned_email")
    private String trip_location;
    @SerializedName("returned_mobile_no")
    private String trip_dt;
    @SerializedName("returned_password")
    private String trip_type;
    private String description;
    @SerializedName("message")
    private String message;
    @SerializedName("error_code")
    private int errorCode;
    private String status;
    private String error;
    private String trip_id;
    private EventDetails trip_details;

    public EventDetails getTrip_details() {
        return trip_details;
    }

    public void setTrip_details(EventDetails trip_details) {
        this.trip_details = trip_details;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getTrip_location() {
        return trip_location;
    }

    public void setTrip_location(String trip_location) {
        this.trip_location = trip_location;
    }

    public String getTrip_dt() {
        return trip_dt;
    }

    public void setTrip_dt(String trip_dt) {
        this.trip_dt = trip_dt;
    }

    public String getTrip_type() {
        return trip_type;
    }

    public void setTrip_type(String trip_type) {
        this.trip_type = trip_type;
    }

    public ServerResponseCE(String trip_location, String trip_dt, String trip_type,String description, String message, int errorCode, String status, String error){
        this.trip_location = trip_location;
        this.trip_dt=trip_dt;
        this.trip_type = trip_type;
        this.description = description;
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}