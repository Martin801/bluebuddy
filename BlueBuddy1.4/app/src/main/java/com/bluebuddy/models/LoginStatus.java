package com.bluebuddy.models;

/**
 * Created by admin on 6/19/2017.
 */

public class LoginStatus {

    private String Token = "";
    private UserDetails user_details;
    private Boolean status;
    private String user_status;
    private String message;

    public LoginStatus() {
        super();
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

    public UserDetails getUser_details() {
        return user_details;
    }

    public void setUser_details(UserDetails user_details) {
        this.user_details = user_details;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUserStatus() {
        return user_status;
    }

    public void setUserStatus(String userStatus) {
        this.user_status = userStatus;
    }
}
