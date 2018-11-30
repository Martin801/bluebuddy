package com.bluebuddy.classes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ServerResponsechrt implements Serializable{


    private String Token;
    @SerializedName("message")
    private String message;
    @SerializedName("error_code")
    private int errorCode;
    private String status;
    private String error;
    private  String id;



    public ServerResponsechrt(String Token, String message, int errorCode, String status, String error,String id){

        this.Token=Token;
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.error = error;
        this.id = id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}