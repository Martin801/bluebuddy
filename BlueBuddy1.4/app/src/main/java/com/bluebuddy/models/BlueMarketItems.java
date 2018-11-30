package com.bluebuddy.models;

/**
 * Created by admin on 5/13/2017.
 */

public class BlueMarketItems {
    private String BMCname, BMCprice, BMCloc, BMCposted;
    private int BMCpic;

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
    public BlueMarketItems(String BMCname, String BMCprice, String BMCloc, String BMCposted, int i, int BMCpic) {
        this.BMCname = BMCname;
        this.BMCprice = BMCprice;
        this.BMCloc = BMCloc;
        this.BMCposted = BMCposted;
        this.BMCpic = BMCpic;
    }

    public String getBMCname() {
        return BMCname;
    }

    public void setBMCname(String BMCname) {
        this.BMCname = BMCname;
    }

    public String getBMCprice() {
        return BMCprice;
    }

    public void setBMCprice(String BMCprice) {
        this.BMCprice = BMCprice;
    }

    public String getBMCloc() {
        return BMCloc;
    }

    public void setBMCloc(String BMCloc) {
        this.BMCloc = BMCloc;
    }

    public String getBMCposted() {
        return BMCposted;
    }

    public void setBMCposted(String BMCposted) {
        this.BMCposted = BMCposted;
    }

    public int getBMCpic() {
        return BMCpic;
    }

    public void setBMCpic(int BMCpic) {
        this.BMCpic = BMCpic;
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
