package com.bluebuddy.models;

import java.io.Serializable;



public class Participant implements Serializable {

    private String Id;
    private String Email;
    private String First_name;
    private String Last_name;
    private String Phone;
    private String Profile_pic;
    private String Location;
    private String Location_lat;
    private String Location_long;
    private String Firebase_API_key;
    private String Firebase_reg_id;

    public String toString(){
        return "Id: " + Id + ", Email: " + Email + ", First_name: " + First_name + ", Last_name: " + Last_name + ", Phone: " + Phone + ", Profile_pic: " + Profile_pic + ", Location: " + Location + ", Location_lat: " + Location_lat + ", Location_long: " + Location_long + ", Firebase_API_key: " + Firebase_API_key + ", Firebase_reg_id: " + Firebase_reg_id;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirst_name() {
        return First_name;
    }

    public void setFirst_name(String first_name) {
        First_name = first_name;
    }

    public String getLast_name() {
        return Last_name;
    }

    public void setLast_name(String last_name) {
        Last_name = last_name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getProfile_pic() {
        return Profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        Profile_pic = profile_pic;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getLocation_lat() {
        return Location_lat;
    }

    public void setLocation_lat(String location_lat) {
        Location_lat = location_lat;
    }

    public String getLocation_long() {
        return Location_long;
    }

    public void setLocation_long(String location_long) {
        Location_long = location_long;
    }

    public String getFirebase_API_key() {
        return Firebase_API_key;
    }

    public void setFirebase_API_key(String firebase_API_key) {
        Firebase_API_key = firebase_API_key;
    }

    public String getFirebase_reg_id() {
        return Firebase_reg_id;
    }

    public void setFirebase_reg_id(String firebase_reg_id) {
        Firebase_reg_id = firebase_reg_id;
    }
}
