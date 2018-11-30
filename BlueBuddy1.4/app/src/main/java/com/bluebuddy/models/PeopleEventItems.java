package com.bluebuddy.models;

/**
 * Created by admin on 5/13/2017.
 */

public class PeopleEventItems {
    private String Ename, Eloc, Efromdate, Edesc;
   // private int Upic;

    public PeopleEventItems(String Ename, String Eloc, String Efromdate,String Edesc, int i) {

        this.Ename = Ename;
        this.Eloc = Eloc;
        this.Efromdate = Efromdate;
        this.Edesc = Edesc;



    }



    public String getEname() {
        return Ename;
    }

    public void setEname(String ename) {
        Ename = ename;
    }

    public String getEloc() {
        return Eloc;
    }

    public void setEloc(String eloc) {
        Eloc = eloc;
    }

    public String getEfromdate() {
        return Efromdate;
    }

    public void setEfromdate(String efromdate) {
        Efromdate = efromdate;
    }

    public String getEdesc() {
        return Edesc;
    }

    public void setEdesc(String edesc) {
        Edesc = edesc;
    }

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
}
