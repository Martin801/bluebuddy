package com.bluebuddy.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDetails implements Serializable {

    private String Id;
    private String Email = "";
    private String First_name = "";
    private String Last_name = "";
    private String Phone = "";

    @SerializedName("Password")
    @Expose
    private String password;

    @SerializedName("is_social")
    @Expose
    private Boolean isSocial;

    private String About = "";
    private String Social = "";
    private String Social_id = "";
    private String Social_link_type = "";
    private String Social_link = "";
    private String Website_link = "";
    private String Profile_pic = "";
    private String Interested_in;
    private String Buddy_uid;
    private String Location = "";
    private String Location_lat = "";
    private String Location_long = "";
    private String Firebase_API_key = "";
    private String Firebase_reg_id = "";
    private String next_step = "";
    private String buddy_counter = "";

    private String fb_url = "";
    private String ggle_url = "";
    private String ingm_url = "";
    private String twtr_url = "";
    /*private String Token = "";
    private String message;*/

    public UserDetails() {
        super();
    }

    public String toString() {
        return (
                "Id: " + Id +
                        ", Email: " + Email +
                        ", First_name: " + First_name +
                        ", Last_name: " + Last_name +
                        ", Phone: " + Phone +
                        ", About: " + About +
                        ", Social: " + Social +
                        ", Social_id: " + Social_id +
                        ", Social_link_type: " + Social_link_type +
                        ", Social_link: " + Social_link +
                        ", Website_link: " + Website_link +
                        ", Profile_pic: " + Profile_pic +
                        ", Interested_in: " + Interested_in +
                        ", Buddy_uid: " + Buddy_uid +
                        ", Location: " + Social_id +
                        ", Location_lat: " + Social_link_type +
                        ", Location_long: " + Social_link +
                        ", Website_link: " + Website_link +
                        ", Firebase_API_key: " + Firebase_API_key +
                        ", Firebase_reg_id: " + Firebase_reg_id +
                        ", buddy_counter: " + buddy_counter +
                        ", fb_url: " + fb_url +
                        ", ggle_url: " + ggle_url +
                        ", ingm_url: " + ingm_url +
                        ", twtr_url: " + twtr_url
        );
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsSocial() {
        return isSocial;
    }

    public void setIsSocial(Boolean isSocial) {
        this.isSocial = isSocial;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    public String getSocial() {
        return Social;
    }

    public void setSocial(String social) {
        Social = social;
    }

    public String getSocial_id() {
        return Social_id;
    }

    public void setSocial_id(String social_id) {
        Social_id = social_id;
    }

    public String getSocial_link_type() {
        return Social_link_type;
    }

    public void setSocial_link_type(String social_link_type) {
        Social_link_type = social_link_type;
    }

    public String getSocial_link() {
        return Social_link;
    }

    public void setSocial_link(String social_link) {
        Social_link = social_link;
    }

    public String getWebsite_link() {
        return Website_link;
    }

    public void setWebsite_link(String website_link) {
        Website_link = website_link;
    }

    public String getProfile_pic() {
        return Profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        Profile_pic = profile_pic;
    }

    public String getInterested_in() {
        return Interested_in;
    }

    public void setInterested_in(String interested_in) {
        Interested_in = interested_in;
    }

    public String getBuddy_uid() {
        return Buddy_uid;
    }

    public void setBuddy_uid(String buddy_uid) {
        Buddy_uid = buddy_uid;
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

    public String getNext_step() {
        return next_step;
    }

    public void setNext_step(String next_step) {
        this.next_step = next_step;
    }

    public String getBuddy_counter() {
        return buddy_counter;
    }

    public void setBuddy_counter(String buddy_counter) {
        this.buddy_counter = buddy_counter;
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
   /* public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }*/

    /*@SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("First_name")
    @Expose
    private String firstName;
    @SerializedName("Last_name")
    @Expose
    private String lastName;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("About")
    @Expose
    private String about;
    @SerializedName("Social")
    @Expose
    private String social;
    @SerializedName("Social_id")
    @Expose
    private String socialId;
    @SerializedName("fb_url")
    @Expose
    private String fbUrl;
    @SerializedName("twtr_url")
    @Expose
    private String twtrUrl;
    @SerializedName("ingm_url")
    @Expose
    private String ingmUrl;
    @SerializedName("ggle_url")
    @Expose
    private String ggleUrl;
    @SerializedName("Website_link")
    @Expose
    private String websiteLink;
    @SerializedName("Profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("Interested_in")
    @Expose
    private String interestedIn;
    @SerializedName("Buddy_uid")
    @Expose
    private String buddyUid;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("Location_lat")
    @Expose
    private String locationLat;
    @SerializedName("Location_long")
    @Expose
    private String locationLong;
    @SerializedName("Firebase_API_key")
    @Expose
    private String firebaseAPIKey;
    @SerializedName("Firebase_reg_id")
    @Expose
    private String firebaseRegId;
    @SerializedName("next_step")
    @Expose
    private String nextStep;
    @SerializedName("device_type")
    @Expose
    private String deviceType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getFbUrl() {
        return fbUrl;
    }

    public void setFbUrl(String fbUrl) {
        this.fbUrl = fbUrl;
    }

    public String getTwtrUrl() {
        return twtrUrl;
    }

    public void setTwtrUrl(String twtrUrl) {
        this.twtrUrl = twtrUrl;
    }

    public String getIngmUrl() {
        return ingmUrl;
    }

    public void setIngmUrl(String ingmUrl) {
        this.ingmUrl = ingmUrl;
    }

    public String getGgleUrl() {
        return ggleUrl;
    }

    public void setGgleUrl(String ggleUrl) {
        this.ggleUrl = ggleUrl;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getInterestedIn() {
        return interestedIn;
    }

    public void setInterestedIn(String interestedIn) {
        this.interestedIn = interestedIn;
    }

    public String getBuddyUid() {
        return buddyUid;
    }

    public void setBuddyUid(String buddyUid) {
        this.buddyUid = buddyUid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(String locationLat) {
        this.locationLat = locationLat;
    }

    public String getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(String locationLong) {
        this.locationLong = locationLong;
    }

    public String getFirebaseAPIKey() {
        return firebaseAPIKey;
    }

    public void setFirebaseAPIKey(String firebaseAPIKey) {
        this.firebaseAPIKey = firebaseAPIKey;
    }

    public String getFirebaseRegId() {
        return firebaseRegId;
    }

    public void setFirebaseRegId(String firebaseRegId) {
        this.firebaseRegId = firebaseRegId;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }*/
}
