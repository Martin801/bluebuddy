package com.bluebuddy.models;

/**
 * Created by USER on 5/28/2017.
 */

public class PeopleSearch {
    private String Pplname, Pplloc, Pplint;
    private int Pplpic;
    private int Pplcrt1;
    private int Pplcrt2;

    public String getPplname() {
        return Pplname;
    }

    public PeopleSearch(String pplname, String pplloc, String pplint, int pplpic, int pplcrt1, int pplcrt2) {
        Pplname = pplname;
        Pplloc = pplloc;
        Pplint = pplint;
        Pplpic = pplpic;
        Pplcrt1 = pplcrt1;
        Pplcrt2 = pplcrt2;
    }

    public void setPplname(String pplname) {
        Pplname = pplname;
    }

    public String getPplloc() {
        return Pplloc;
    }

    public void setPplloc(String pplloc) {
        Pplloc = pplloc;
    }

    public String getPplint() {
        return Pplint;
    }

    public void setPplint(String pplint) {
        Pplint = pplint;
    }

    public int getPplpic() {
        return Pplpic;
    }

    public void setPplpic(int pplpic) {
        Pplpic = pplpic;
    }

    public int getPplcrt1() {
        return Pplcrt1;
    }

    public void setPplcrt1(int pplcrt1) {
        Pplcrt1 = pplcrt1;
    }

    public int getPplcrt2() {
        return Pplcrt2;
    }

    public void setPplcrt2(int pplcrt2) {
        Pplcrt2 = pplcrt2;
    }


}
