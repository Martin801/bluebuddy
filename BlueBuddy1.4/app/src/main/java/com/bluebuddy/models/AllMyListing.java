package com.bluebuddy.models;

import java.util.ArrayList;


public class AllMyListing {

    private boolean status;

    private ArrayList<AllCharterDetails> charter_list;
    private ArrayList<AllCourseDetails> course_list;
    private ArrayList<AllProductDetails> product_list;
    private ArrayList<AllServiceDetails> service_list;

    public ArrayList<AllCourseDetails> getCourse_list() {
        return course_list;
    }

    public void setCourse_list(ArrayList<AllCourseDetails> course_list) {
        this.course_list = course_list;
    }

    public ArrayList<AllProductDetails> getProduct_list() {
        return product_list;
    }

    public void setProduct_list(ArrayList<AllProductDetails> product_list) {
        this.product_list = product_list;
    }

    public ArrayList<AllServiceDetails> getService_list() {
        return service_list;
    }

    public void setService_list(ArrayList<AllServiceDetails> service_list) {
        this.service_list = service_list;
    }



    public ArrayList<AllCharterDetails> getCharter_list() {
        return charter_list;
    }

    public void setCharter_list(ArrayList<AllCharterDetails> charter_list) {
        this.charter_list = charter_list;
    }

    public AllMyListing(){
        super();
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    /*public ArrayList<AllCharterDetails> getDetails() {
        return charter_list;
    }

    public void setDetails(ArrayList<AllCharterDetails> charter_list) {
        this.charter_list = charter_list;
    }*/

    public String toString(){
        return String.valueOf(this.status);
    }

}
