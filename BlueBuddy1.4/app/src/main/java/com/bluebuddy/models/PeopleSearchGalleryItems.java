package com.bluebuddy.models;

/**
 * Created by admin on 5/29/2017.
 */

public class PeopleSearchGalleryItems {
    private String Srchpname;

    public PeopleSearchGalleryItems(String srchpname, int srchppic) {
        Srchpname = srchpname;
        Srchppic = srchppic;
    }

    public String getSrchpname() {
        return Srchpname;
    }

    public void setSrchpname(String srchpname) {
        Srchpname = srchpname;
    }

    public int getSrchppic() {
        return Srchppic;
    }

    public void setSrchppic(int srchppic) {
        Srchppic = srchppic;
    }

    private int Srchppic;
}
