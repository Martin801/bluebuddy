package com.bluebuddy.models;

import java.util.ArrayList;



public class AllProduct {

    private boolean status;
    private ArrayList<AllProductDetails> product_list;

    /*public ArrayList<AllProductDetails> getCharter_list() {
        return PRODUCT_list;
    }

    public void setCharter_list(ArrayList<AllProductDetails> charter_list) {
        this.PRODUCT_list = PRODUCT_list;
    }*/

    public AllProduct(){
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

    /*public ArrayList<AllProductDetails> getPRODUCT_list() {
        return PRODUCT_list;
    }

    public void setPRODUCT_list(ArrayList<AllProductDetails> PRODUCT_list) {
        this.PRODUCT_list = PRODUCT_list;
    }*/

    public ArrayList<AllProductDetails> getProduct_list() {
        return product_list;
    }

    public void setProduct_list(ArrayList<AllProductDetails> product_list) {
        this.product_list = product_list;
    }
}
