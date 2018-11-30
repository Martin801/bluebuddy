package com.bluebuddy.models;

import java.util.ArrayList;


public class AllCourse {

    private boolean status;
    private ArrayList<AllCourseDetails> course_list;

    /*public ArrayList<AllProductDetails> getCourse_list() {
        return course_list;
    }

    public void setCourse_list(ArrayList<AllProductDetails> course_list) {
        this.course_list = course_list;
    }*/

    public ArrayList<AllCourseDetails> getCourse_list() {
        return course_list;
    }

    public void setCourse_list(ArrayList<AllCourseDetails> course_list) {
        this.course_list = course_list;
    }

    public AllCourse(){
        super();
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String toString(){
        return String.valueOf(this.status);
    }


}
