package com.bluebuddy.models;

import java.util.HashMap;

public class PushRequest {
    private String to;
    private String serverKey;
    private HashMap<String, String> notification;

    public PushRequest(String to, HashMap<String, String> notification){
        this.to = to;
        this.notification = notification;
        this.serverKey=serverKey;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public HashMap<String, String> getNotification() {
        return notification;
    }

    public void setNotification(HashMap<String, String> notification) {
        this.notification = notification;
    }
    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }
}
