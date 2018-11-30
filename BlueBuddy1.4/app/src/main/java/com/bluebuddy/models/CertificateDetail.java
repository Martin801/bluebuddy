package com.bluebuddy.models;

import java.io.Serializable;



public class CertificateDetail implements Serializable {

    private String Id;
    private String User_id;
    private String Cert_type;
    private String Cert_name;
    private String Cert_no;
    private String Cert_level;
    private String Cert_level2;
    private String Cert_level3;
    private String create_dt;
    private String update_dt;

    public CertificateDetail(){
        super();
    }

    public CertificateDetail(String Id, String User_id, String Cert_type, String Cert_name, String Cert_no, String Cert_level, String Cert_level2, String Cert_level3, String create_dt, String update_dt){
        this.Id = Id;
        this.User_id = User_id;
        this.Cert_type = Cert_type;
        this.Cert_name = Cert_name;
        this.Cert_no = Cert_no;
        this.Cert_level = Cert_level;
        this.Cert_level2 = Cert_level2;
        this.Cert_level3 = Cert_level3;
        this.create_dt = create_dt;
        this.update_dt = update_dt;
    }


    public String toString(){
        return "Id:" + this.Id + ", User_id:" + this.User_id + ", Cert_type:" + this.Cert_type + ", Cert_name:" + this.Cert_name + ", Cert_no:" + this.Cert_no + "," +
                "Cert_level:" + this.Cert_level+
                " Cert_level2:" + this.Cert_level2 +
        ",Cert_level3:" + this.Cert_level3 +
                ", create_dt:" + this.create_dt +
                " ,update_dt:" + this.update_dt
        ;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getCert_type() {
        return Cert_type;
    }

    public void setCert_type(String cert_type) {
        Cert_type = cert_type;
    }

    public String getCert_name() {
        return Cert_name;
    }

    public void setCert_name(String cert_name) {
        Cert_name = cert_name;
    }

    public String getCert_no() {
        return Cert_no;
    }

    public void setCert_no(String cert_no) {
        Cert_no = cert_no;
    }

    public String getCert_level() {
        return Cert_level;
    }

    public void setCert_level(String cert_level) {
        Cert_level = cert_level;
    }

    public String getCert_level2() {
        return Cert_level2;
    }

    public void setCert_level2(String cert_level2) {
        Cert_level2 = cert_level2;
    }

    public String getCert_level3() {
        return Cert_level3;
    }

    public void setCert_level3(String cert_level3) {
        Cert_level3 = cert_level3;
    }

    public String getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(String create_dt) {
        this.create_dt = create_dt;
    }

    public String getUpdate_dt() {
        return update_dt;
    }

    public void setUpdate_dt(String update_dt) {
        this.update_dt = update_dt;
    }
}
