package com.bluebuddy.classes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ServerResponseCp1 implements Serializable{

    private String first_name;
    private String Last_name;
    private String Token;
    @SerializedName("message")
    private String message;
    @SerializedName("error_code")
    private int errorCode;
    private String status;
    private String error;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        first_name = first_name;
    }

    public String getLast_name() {
        return Last_name;
    }

    public void setLast_name(String last_name) {
        Last_name = last_name;
    }

    public ServerResponseCp1(String first_name, String Last_name,String Token, String message, int errorCode, String status, String error){
        this.first_name = first_name;
        this.Last_name = Last_name;
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