package com.bluebuddy.classes;

import com.bluebuddy.models.UserDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServerResponseCp7 implements Serializable {

    /*private String profile_img;
    private String Token;
    @SerializedName("message")
    private String message;
    @SerializedName("error_code")
    private int errorCode;
    private String status;
    private String error;

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public ServerResponseCp7(String profile_img, String Token, String message, int errorCode, String status, String error){

        this.profile_img = profile_img;
        this.Token = Token;
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.error = error;

    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        Token = Token;
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
    }*/

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("user_details")
    @Expose
    private UserDetails userDetails;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }



    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}