package com.bluebuddy.models;

import java.sql.Timestamp;

/**
 * Created by Aquarious Technology on 1/30/2018.
 */

public class UserChat {
    private String user_id;
    private String name;
    private String email;
    private String image;

    private String messageLocation;

    private String lastMessage;
    private Long lastMessageTime;

    public UserChat(String user_id, String name, String email, String image, String messageLocation, String lastMessage, Long lastMessageTime) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.image = image;
        this.messageLocation = messageLocation;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessageLocation() {
        return messageLocation;
    }

    public void setMessageLocation(String messageLocation) {
        this.messageLocation = messageLocation;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
