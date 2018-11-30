package com.bluebuddy.models;

public class CommonResponse {

    private boolean status;
    private String message;
    private String Token;
    private String new_socialuser;
    ServiceDetail details;
    private ProfileDetails2 user_details;

    public String getNew_socialuser() {
        return new_socialuser;
    }

    public void setNew_socialuser(String new_socialuser) {
        this.new_socialuser = new_socialuser;
    }

    public ServiceDetail getDetails() {
        return details;
    }

    public void setDetails(ServiceDetail details) {
        this.details = details;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
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
    }

    public ProfileDetails2 getUser_details() {
        return user_details;
    }

    public void setUser_details(ProfileDetails2 user_details) {
        this.user_details = user_details;
    }
}
