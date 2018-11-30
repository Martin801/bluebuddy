package com.bluebuddy.classes;

import java.io.Serializable;


public class ServerResponseUnf implements Serializable{

    private String message;
    private String status;

    public ServerResponseUnf(String cert_agency, String cert_level, String cert_no, String Token, String message, int errorCode, String status, String error){

        this.message = message;
        this.status = status;

    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }





}