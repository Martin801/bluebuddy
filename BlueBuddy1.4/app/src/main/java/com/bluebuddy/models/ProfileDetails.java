package com.bluebuddy.models;

/**
 * Created by admin on 6/19/2017.
 */

public class ProfileDetails {

    private String Email = "";
    private String First_name = "";
    private String Last_name = "";
    private String Phone = "";
    private String About = "";
    private String Social_link_type = "";
    private String Social_link = "";
    private String Website_link = "";
    private String Profile_pic = "";
    private String Interested_in = "";
    private String Location = "";
    private String next_step = "";
    private String buddy_uid = "";
    private String buddy_counter = "";
    private String fb_url = "";
    private String ggle_url = "";
    private String ingm_url = "";
    private String twtr_url = "";

    public String getBuddy_counter() {
        return buddy_counter;
    }

    public void setBuddy_counter(String buddy_counter) {
        this.buddy_counter = buddy_counter;
    }

    private String Id = "";
    private String Location_lat = "";
    private String Location_long = "";
    private String Firebase_API_key = "";
    private String Firebase_reg_id = "";
    private String Certification_type = "";

    public String toString() {
        return "Email: " + Email + ", First_name: " + First_name + ", Last_name: " + Last_name + ",  Phone: " + Phone + ", About: " + About + ",Social_link_type:" + Social_link_type + ", Social_link: " + Social_link + ",Website_link:" + Website_link + ", Profile_pic: " + Profile_pic + ", Interested_in: " + Interested_in + ",Location:" + Location + ",next_step:" + next_step + ", Location: " + Location + ", buddy_uid: " + buddy_uid
                + "Id: " + Id + ", Location_lat: " + Location_lat + ", Location_long: " + Location_long +
                "Firebase_API_key: " + Firebase_API_key + ", Firebase_reg_id: " + Firebase_reg_id + ", Certification_type: " + Certification_type + ", buddy_counter: " + buddy_counter
                + ", fb_url: " + fb_url+ ", ggle_url: " + ggle_url+ ", ingm_url: " + ingm_url+ ", twtr_url: " + twtr_url;
    }

    public String getSocial_link_type() {
        return Social_link_type;
    }

    public void setSocial_link_type(String Social_link_type) {
        this.Social_link_type = Social_link_type;
    }

    public String getWebsite_link() {
        return Website_link;
    }

    public void setWebsite_link(String Website_link) {
        this.Website_link = Website_link;
    }

    public String getProfile_pic() {
        return Profile_pic;
    }

    public void setProfile_pic(String Profile_pic) {
        this.Profile_pic = Profile_pic;
    }

    public String getNext_step() {
        return next_step;
    }

    public void setNext_step(String next_step) {
        this.next_step = next_step;
    }

    public ProfileDetails() {
        super();
    }

    public String getFirst_name() {
        return First_name;
    }

    public void setFirst_name(String First_name) {
        this.First_name = First_name;
    }

    public String getLast_name() {
        return Last_name;
    }

    public void setLast_name(String full_name) {
        this.Last_name = Last_name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = Email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = Phone;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        this.About = About;
    }

    public String getSocial_link() {
        return Social_link;
    }

    public void setSocial_link(String social_link) {
        this.Social_link = Social_link;
    }

    public String getInterested_in() {
        return Interested_in;
    }

    public void setInterested_in(String Interested_in) {
        this.Interested_in = Interested_in;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public String getBuddy_uid() {
        return buddy_uid;
    }

    public void setBuddy_uid(String buddy_uid) {
        this.buddy_uid = buddy_uid;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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
}
