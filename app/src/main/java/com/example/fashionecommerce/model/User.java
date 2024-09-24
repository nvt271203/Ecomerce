package com.example.fashionecommerce.model;

import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.google.firebase.database.Exclude;

public class User {
    public User() {
    }

    public User(String name, String phone, String email, String pass) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
    }

    public void storeData(){
        FirebaseHelper.databaseReference("user")
                .child(id)
                .setValue(this);
    }
    private String id;
    private String name;
    private String phone;
    private String email;
    private String pass;
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    @Exclude
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
