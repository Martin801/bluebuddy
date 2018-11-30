package com.bluebuddy.classes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ServerResponseCp3 implements Serializable{

    private String cert_agency;
    private String cert_no;
    private String cert_level;
    private String Token;
    @SerializedName("message")
    private String message;
    @SerializedName("error_code")
    private int errorCode;
    private String status;
    private String error;

    public String getCert_agency() {
        return cert_agency;
    }

    public void setCert_agency(String cert_agency) {
        this.cert_agency = cert_agency;
    }

    public String getCert_no() {
        return cert_no;
    }

    public void setCert_no(String cert_no) {
        this.cert_no = cert_no;
    }

    public String getCert_level() {
        return cert_level;
    }

    public void setCert_level(String cert_level) {
        this.cert_level = cert_level;
    }

    public ServerResponseCp3(String cert_agency, String cert_level, String cert_no,  String Token, String message, int errorCode, String status, String error){
       this.cert_agency=cert_agency;
        this.cert_level=cert_level;
        this.cert_no=cert_no;
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