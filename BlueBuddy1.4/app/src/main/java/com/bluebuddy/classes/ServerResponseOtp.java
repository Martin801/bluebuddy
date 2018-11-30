package com.bluebuddy.classes;

import java.io.Serializable;


public class ServerResponseOtp implements Serializable{
    //@SerializedName("returned_email")

   // @SerializedName("returned_password")
    private String OTP;
    private String user_id;
  //  @SerializedName("message")
  private String Token;
    private String message;
  //  @SerializedName("error_code")
    private int errorCode;
    private String status ;
    private String error;


    public ServerResponseOtp(String OTP,String user_id,String Token, String message, int errorCode, String status, String error){
        this.OTP=OTP;
        this.user_id=user_id;
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
        this.Token = Token;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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