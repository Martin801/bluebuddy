package com.bluebuddy.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class ProductDetail implements Serializable {

//    private String image;
//    private String name;
//    private String description;
//    private String price;
//    private String featured;
//    private String fname;
//    private String lname;
//    private String phone;
//    private String email;
//    private String firebase_api;
//    private String firebase_reg;
//    private String rating;
//    private  String category;
//    private String location;
//
//    public String getIs_reviewed() {
//        return is_reviewed;
//    }
//
//    public void setIs_reviewed(String is_reviewed) {
//        this.is_reviewed = is_reviewed;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(String user_id) {
//        this.user_id = user_id;
//    }
//
//    private String is_reviewed;
//    private String id;
//    private  String user_id;
//
//
//    public ProductDetail(){
//        super();
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getFname() {
//        return fname;
//    }
//
//    public void setFname(String fname) {
//        this.fname = fname;
//    }
//
//    public String getLname() {
//        return lname;
//    }
//
//    public void setLname(String lname) {
//        this.lname = lname;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getRating() {
//        return rating;
//    }
//
//    public void setRating(String rating) {
//        this.rating = rating;
//    }
//
//    public String toString(){
//        return "image:"+this.image
//                + "name:" + this.name
//                + ", description:" + this.description
//                + ", price:" + this.price + ","
//                + ", featured:" + this.featured + ","
//                + ", fname:" + this.fname + ","
//                + " lname:" + this.lname
//                + " phone:" + this.phone
//
//                + ", email:" + this.email
//                + ", firebase_api:" + this.firebase_api
//                + ", firebase_reg:" + this.firebase_reg
//                + ", rating:" + this.rating
//                + ", category:" + this.category
//                + ", location:" + this.location
//                ;
//    }
//
//
//    public String getPrice() {
//        return price;
//    }
//
//    public void setPrice(String price) {
//        this.price = price;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }
//
//    public String getFeatured() {
//        return featured;
//    }
//
//    public void setFeatured(String featured) {
//        this.featured = featured;
//    }
//
//
//
//    public String getFirebase_api() {
//        return firebase_api;
//    }
//
//    public void setFirebase_api(String firebase_api) {
//        this.firebase_api = firebase_api;
//    }
//
//    public String getFirebase_reg() {
//        return firebase_reg;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    public void setFirebase_reg(String firebase_reg) {
//        this.firebase_reg = firebase_reg;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("featured")
    @Expose
    private String featured;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("firebase_api")
    @Expose
    private String firebaseApi;
    @SerializedName("firebase_reg")
    @Expose
    private String firebaseReg;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("product_images")
    @Expose
    private List<ProductImage> productImages = null;
    @SerializedName("is_reviewed")
    @Expose
    private String isReviewed;
    @SerializedName("hide_details")
    @Expose
    private String hideDetails;
    @SerializedName("rating")
    @Expose
    private Integer rating;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirebaseApi() {
        return firebaseApi;
    }

    public void setFirebaseApi(String firebaseApi) {
        this.firebaseApi = firebaseApi;
    }

    public String getFirebaseReg() {
        return firebaseReg;
    }

    public void setFirebaseReg(String firebaseReg) {
        this.firebaseReg = firebaseReg;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public String getIsReviewed() {
        return isReviewed;
    }

    public void setIsReviewed(String isReviewed) {
        this.isReviewed = isReviewed;
    }

    public String getHideDetails() {
        return hideDetails;
    }

    public void setHideDetails(String hideDetails) {
        this.hideDetails = hideDetails;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
