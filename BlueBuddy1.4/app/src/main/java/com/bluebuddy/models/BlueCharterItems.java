package com.bluebuddy.models;

/**
 * Created by admin on 5/13/2017.
 */

public class BlueCharterItems {
    private String BCCname, BCCprice, BCCloc, BCCposted;
    private int BCCpic;

    /*
    public BlueMarketItems(String Uname, String Ename, String Eloc, String Efromdate, String Etodate, String Edesc, int i, int Upic) {
        this.Uname=Uname;
        this.Ename=Ename;
        this.Eloc=Eloc;
        this.Efromdate=Efromdate;
        this.Etodate=Etodate;
        this.Edesc=Edesc;
        this.Upic=Upic;
    }
*/
    public BlueCharterItems(String BCCname, String BCCprice, String BCCloc, String BCCposted, int i, int BCCpic) {
        this.BCCname = BCCname;
        this.BCCprice = BCCprice;
        this.BCCloc = BCCloc;
        this.BCCposted = BCCposted;
        this.BCCpic = BCCpic;
    }

    public String getBMCname() {
        return BCCname;
    }

    public void setBMCname(String BMCname) {
        this.BCCname = BMCname;
    }

    public String getBMCprice() {
        return BCCprice;
    }

    public void setBMCprice(String BMCprice) {
        this.BCCprice = BMCprice;
    }

    public String getBMCloc() {
        return BCCloc;
    }

    public void setBMCloc(String BMCloc) {
        this.BCCloc = BMCloc;
    }

    public String getBMCposted() {
        return BCCposted;
    }

    public void setBMCposted(String BMCposted) {
        this.BCCposted = BMCposted;
    }

    public int getBMCpic() {
        return BCCpic;
    }

    public void setBMCpic(int BMCpic) {
        this.BCCpic = BMCpic;
    }



/*
    public static class Glossary {

        private String name;

        private String sortKey;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSortKey() {
            return sortKey;
        }

        public void setSortKey(String sortKey) {
            this.sortKey = sortKey;
        }

    }
    */
}
