package com.bluebuddy.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.sql.Timestamp;

/**
 * Created by Aquarious Technology on 1/31/2018.
 */

public class MessageDetails {
    private String id;
    private String fromID;
    private String toID;
    private String content;
    private Boolean isRead;

    public Object timestamp;

    public MessageDetails() {

    }

    public MessageDetails(String fromID, String toID, String content, Boolean isRead) {
//        this.id = id;
        this.fromID = fromID;
        this.toID = toID;
        this.content = content;
        this.isRead = isRead;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    @Exclude
    public Long getTimestamp() {
        if (timestamp instanceof Long) {
            return (Long) timestamp;
        } else {
            return null;
        }
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
}
