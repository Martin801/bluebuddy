package com.bluebuddy.models;

import java.util.ArrayList;

public class AllChatHistory {

    private boolean status;

    private ArrayList<AllChatHisDetails> response;

    public AllChatHistory(){
        super();
    }

    public String toString(){
        return String.valueOf(this.status);
    }

    public ArrayList<AllChatHisDetails> getResponse() {return response;}

    public void setResponse(ArrayList<AllChatHisDetails> response) {this.response = response;}

    public boolean isStatus() {return status;}

    public void setStatus(boolean status) {this.status = status;}

}
