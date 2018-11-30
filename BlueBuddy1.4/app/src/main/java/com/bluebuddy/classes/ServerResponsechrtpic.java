package com.bluebuddy.classes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ServerResponsechrtpic implements Serializable{


    private String ch_img;
    private String Token;
    @SerializedName("message")
    private String message;
    @SerializedName("error_code")
    private int errorCode;
    private String status;
    private String error;


    public String getCh_img() {
        return ch_img;
    }

    public void setCh_img(String ch_img) {
        this.ch_img = ch_img;
    }

    public ServerResponsechrtpic(String ch_img, String Token, String message, int errorCode, String status, String error){

        this.ch_img=ch_img;
        this.Token=Token;
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
    }





}