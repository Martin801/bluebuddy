package com.bluebuddy.models;



public class AllEventItems {
    private String Uname, Ename, Eloc, Efromdate, Etodate, Edesc;
    private int Upic;

    public AllEventItems(String Uname, String Ename, String Eloc, String Efromdate, String Etodate, String Edesc, int i, int Upic) {
        this.Uname = Uname;
        this.Ename = Ename;
        this.Eloc = Eloc;
        this.Efromdate = Efromdate;
        this.Etodate = Etodate;
        this.Edesc = Edesc;
        this.Upic = Upic;


    }

    public String getUname() {
        return Uname;
    }

    public void setUname(String uname) {
        Uname = uname;
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

    public String getEtodate() {
        return Etodate;
    }

    public void setEtodate(String etodate) {
        Etodate = etodate;
    }

    public String getEdesc() {
        return Edesc;
    }

    public void setEdesc(String edesc) {
        Edesc = edesc;
    }

    public int getUpic() {
        return Upic;
    }

    public void setUpic(int upic) {
        Upic = upic;
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
