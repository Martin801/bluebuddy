package com.bluebuddy.classes;

import java.io.Serializable;


public class ServerResponseFp implements Serializable{
    //@SerializedName("returned_email")
    private String email;
   // @SerializedName("returned_password")
    private String password;

  //  @SerializedName("message")
    private String message;
  //  @SerializedName("error_code")
    private int errorCode;
    private String status ;
    private String error;
    private String Phone;


    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }





    public ServerResponseFp(String email, String password, String message, int errorCode, String status, String error, String Phone){
        this.email = email;
        this.password = password;
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.error = error;
        this.Phone=Phone;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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