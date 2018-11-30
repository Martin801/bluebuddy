package com.bluebuddy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by Aquarious Technology on 2/2/2018.
 */

public class TestModal{

    @JsonProperty
    public Object timestamp;

//    public HashMap<String, Object> dateLastChanged;

//    public TestModal(){
//        //Otherwise make a new object set to ServerValue.TIMESTAMP
//        //Date last changed will always be set to ServerValue.TIMESTAMP
//        HashMap<String, Object> dateLastChangedObj = new HashMap<String, Object>();
//        dateLastChangedObj.put("timestamp", ServerValue.TIMESTAMP);
//        this.dateLastChanged = dateLastChangedObj;
//    }

    public TestModal(){
        this.timestamp = ServerValue.TIMESTAMP;
    }

//    @Exclude
//    public long getDateLastChanged() {
//
//        return (long)dateLastChanged.get("timestamp");
//    }

    @Exclude
    public Long getCreatedTimestamp() {
        if (timestamp instanceof Long) {
            return (Long) timestamp;
        }
        else {
            return null;
        }
    }
}
