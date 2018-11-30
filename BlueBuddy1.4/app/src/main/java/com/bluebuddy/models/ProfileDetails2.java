package com.bluebuddy.models;

/**
 * Created by admin on 6/19/2017.
 */

public class ProfileDetails2 {
    private String id = "";
    private String email = "";
    private String first_name = "";
    private String last_name = "";
    private String full_name = "";
    private String phone = "";
    private String about = "";
    private String Social_link_type = "";
    private String Social_link = "";
    private String website_link = "";
    private String dp = "";
    private String interested_in = "";
    private String location = "";
    private String next_step = "";
    private String buddy_uid = "";
    private String buddy_counter = "";
    private String fb_url = "";
    private String ggle_url = "";
    private String ingm_url = "";
    private String twtr_url = "";
    private String latitude = "";
    private String longitude = "";
    private String firebase_api_key = "";
    private String firebase_reg_id = "";

    public String getBuddy_counter() {
        return buddy_counter;
    }

    public void setBuddy_counter(String buddy_counter) {
        this.buddy_counter = buddy_counter;
    }

    private String Location_lat = "";
    private String Location_long = "";
    private String Firebase_API_key = "";
    private String Firebase_reg_id = "";
    private String Certification_type = "";

    public String toString() {
        return "Email: " + email + ", First_name: " + first_name + ", Last_name: " + last_name + ",  Phone: " + phone + ", About: " + about + ",Social_link_type:" + Social_link_type + ", Social_link: " + Social_link + ",Website_link:" + website_link + ", Profile_pic: " + dp + ", Interested_in: " + interested_in + ",Location:" + location + ",next_step:" + next_step + ", Location: " + location + ", buddy_uid: " + buddy_uid
                + "Id: " + id + ", Location_lat: " + Location_lat + ", Location_long: " + Location_long +
                "Firebase_API_key: " + Firebase_API_key + ", Firebase_reg_id: " + Firebase_reg_id + ", Certification_type: " + Certification_type + ", buddy_counter: " + buddy_counter
                + ", fb_url: " + fb_url + ", ggle_url: " + ggle_url + ", ingm_url: " + ingm_url + ", twtr_url: " + twtr_url;
    }

    public String getSocial_link_type() {
        return Social_link_type;
    }

    public void setSocial_link_type(String Social_link_type) {
        this.Social_link_type = Social_link_type;
    }



    public String getNext_step() {
        return next_step;
    }

    public void setNext_step(String next_step) {
        this.next_step = next_step;
    }

    public ProfileDetails2() {
        super();
    }



    public String getSocial_link() {
        return Social_link;
    }

    public void setSocial_link(String social_link) {
        this.Social_link = Social_link;
    }



    public String getBuddy_uid() {
        return buddy_uid;
    }

    public void setBuddy_uid(String buddy_uid) {
        this.buddy_uid = buddy_uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCertification_type() {
        return Certification_type;
    }

    public void setCertification_type(String certification_type) {
        Certification_type = certification_type;
    }

    public String getFb_url() {
        return fb_url;
    }

    public void setFb_url(String fb_url) {
        this.fb_url = fb_url;
    }

    public String getGgle_url() {
        return ggle_url;
    }

    public void setGgle_url(String ggle_url) {
        this.ggle_url = ggle_url;
    }

    public String getIngm_url() {
        return ingm_url;
    }

    public void setIngm_url(String ingm_url) {
        this.ingm_url = ingm_url;
    }

    public String getTwtr_url() {
        return twtr_url;
    }

    public void setTwtr_url(String twtr_url) {
        this.twtr_url = twtr_url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getWebsite_link() {
        return website_link;
    }

    public void setWebsite_link(String website_link) {
        this.website_link = website_link;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getInterested_in() {
        return interested_in;
    }

    public void setInterested_in(String interested_in) {
        this.interested_in = interested_in;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getFirebase_api_key() {
        return firebase_api_key;
    }

    public void setFirebase_api_key(String firebase_api_key) {
        this.firebase_api_key = firebase_api_key;
    }
}
