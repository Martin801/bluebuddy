package com.bluebuddy.models;

public class BuddyUIDS {

    private boolean check = false;
    private String user_id;

    public BuddyUIDS(){
        super();
    }

    public boolean getCheck(){
        return this.check;
    }

    public void setCheck(boolean c){
        this.check = c;
    }

    public boolean isCheck() {return check;}

    public String getUser_id() {return user_id;}

    public void setUser_id(String user_id) {this.user_id = user_id;}

}