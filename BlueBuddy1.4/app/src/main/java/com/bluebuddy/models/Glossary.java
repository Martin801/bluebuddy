package com.bluebuddy.models;

public class Glossary {

    private boolean check = false;
    private String name;
    private String sortKey;
    private String phone;

    public Glossary(){
        super();
    }

    public boolean getCheck(){
        return this.check;
    }

    public void setCheck(boolean c){
        this.check = c;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}