package com.bluebuddy.models;

/**
 * Created by USER on 5/28/2017.
 */

public class ParticipantsList {
    private String Pplname;
    private int Pplpic;

    public String getPplname() {
        return Pplname;
    }

    public ParticipantsList(String pplname,int pplpic) {
        Pplname = pplname;
        Pplpic = pplpic;
    }

    public void setPplname(String pplname) {
        Pplname = pplname;
    }

    public int getPplpic() {
        return Pplpic;
    }

    public void setPplpic(int pplpic) {
        Pplpic = pplpic;
    }

}
