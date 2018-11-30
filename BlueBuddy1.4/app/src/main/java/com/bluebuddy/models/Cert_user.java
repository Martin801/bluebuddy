package com.bluebuddy.models;

public class Cert_user {

    private String Cert_type = "";

    public String toString(){
        return "Cert_type: " + Cert_type ;
    }

    public String getCert_type() {
        return Cert_type;
    }

    public void setCert_type(String cert_type) {
        Cert_type = cert_type;
    }
}
