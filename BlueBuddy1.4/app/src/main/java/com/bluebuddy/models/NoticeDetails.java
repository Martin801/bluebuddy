package com.bluebuddy.models;

import java.io.Serializable;


public class NoticeDetails implements Serializable {
    private String Id;
    private String Notify_for;
    private String For_id;
    private String Requestor_id;
    private String Receiver_id;
    private String Message;
    private String Buddy_phone;
    private String Is_seen;
    private String Viewed_at;
    private String create_dt;
    private String update_dt;
    private String Profile_pic;
    private String First_name;
    private String Last_name;
    private String Firebase_API_key;
    private String Firebase_reg_id;
    private String User_id;
    private String Activity_type;
    private String is_accepted;


    public NoticeDetails() {
        super();
    }

    public NoticeDetails(String Id, String Notify_for, String For_id, String Requestor_id, String Receiver_id, String Message, String Buddy_phone, String Is_seen,
                         String Viewed_at, String update_dt,String Profile_pic,String First_name,String Last_name ,String Firebase_API_key,String Firebase_reg_id,String User_id,String Activity_type,String is_accepted ) {
        this.Id = Id;
        this.Notify_for = Notify_for;
        this.For_id = For_id;
        this.Requestor_id = Requestor_id;
        this.Receiver_id = Receiver_id;
        this.Message = Message;
        this.Buddy_phone = Buddy_phone;
        this.Is_seen = Is_seen;
        this.Viewed_at = Viewed_at;
        this.update_dt = update_dt;
        this.Profile_pic=Profile_pic;
        this.First_name=First_name;
        this.Last_name=Last_name;
        this.Firebase_API_key=Firebase_API_key;
        this.Firebase_reg_id=Firebase_reg_id;
        this.User_id=User_id;
        this.Activity_type=Activity_type;
        this.is_accepted=is_accepted;


    }

    public String toString() {
        return "Id: " + Id + ", Notify_for: " + Notify_for + ", For_id: " + For_id + "," +
                " Requestor_id: " + Requestor_id + ", Receiver_id: " + Receiver_id + ", Message: " + Message + ", " +
                "Buddy_phone: " + Buddy_phone + ", Is_seen: " + Is_seen + ", Viewed_at: " + Viewed_at + ", update_dt: " + update_dt
                + ", Profile_pic: " + Profile_pic +  ", First_name: " + First_name +  ", Last_name: " + Last_name +  ", Firebase_API_key: " + Firebase_API_key
                + ", Firebase_reg_id: " + Firebase_reg_id +  ", User_id: " + User_id +  ", Activity_type: " + Activity_type +",is_accepted:"+is_accepted;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNotify_for() {
        return Notify_for;
    }

    public void setNotify_for(String notify_for) {
        Notify_for = notify_for;
    }

    public String getFor_id() {
        return For_id;
    }

    public void setFor_id(String for_id) {
        For_id = for_id;
    }

    public String getRequestor_id() {
        return Requestor_id;
    }

    public void setRequestor_id(String requestor_id) {
        Requestor_id = requestor_id;
    }

    public String getReceiver_id() {
        return Receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        Receiver_id = receiver_id;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getBuddy_phone() {
        return Buddy_phone;
    }

    public void setBuddy_phone(String buddy_phone) {
        Buddy_phone = buddy_phone;
    }

    public String getIs_seen() {
        return Is_seen;
    }

    public void setIs_seen(String is_seen) {
        Is_seen = is_seen;
    }

    public String getViewed_at() {
        return Viewed_at;
    }

    public void setViewed_at(String viewed_at) {
        Viewed_at = viewed_at;
    }

    public String getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(String create_dt) {
        this.create_dt = create_dt;
    }

    public String getUpdate_dt() {
        return update_dt;
    }

    public void setUpdate_dt(String update_dt) {
        this.update_dt = update_dt;
    }

    public String getProfile_pic() {
        return Profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        Profile_pic = profile_pic;
    }

    public String getFirst_name() {
        return First_name;
    }

    public void setFirst_name(String first_name) {
        First_name = first_name;
    }

    public String getLast_name() {
        return Last_name;
    }

    public void setLast_name(String last_name) {
        Last_name = last_name;
    }

    public String getFirebase_API_key() {
        return Firebase_API_key;
    }

    public void setFirebase_API_key(String firebase_API_key) {
        Firebase_API_key = firebase_API_key;
    }

    public String getFirebase_reg_id() {
        return Firebase_reg_id;
    }

    public void setFirebase_reg_id(String firebase_reg_id) {
        Firebase_reg_id = firebase_reg_id;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getActivity_type() {
        return Activity_type;
    }

    public void setActivity_type(String activity_type) {
        Activity_type = activity_type;
    }

    public String getIs_accepted() {
        return is_accepted;
    }

    public void setIs_accepted(String is_accepted) {
        this.is_accepted = is_accepted;
    }
}
