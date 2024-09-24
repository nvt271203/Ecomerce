package com.example.fashionecommerce.model;

import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.security.auth.Destroyable;

public class Category {
    public Category(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        this.setId(reference.push().getKey());
    }
    public void storeData(){
        FirebaseHelper.databaseReference("category")
                .child(id)
                .setValue(this);
    }
    public void delete(){
        FirebaseHelper.databaseReference("category")
                .child(id)
                .removeValue();
        FirebaseHelper.storageReference("images")
                .child("categories")
                .child(id + ".jpeg")
                .delete();
    }
    private String id;
    private String name;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
