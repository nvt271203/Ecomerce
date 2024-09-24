package com.example.fashionecommerce.model;

import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    private String id;
    private int idLocal;
    private String name;
    private String desc;
    private int count;
    private boolean statusDraft;
    private double wholesalePrice;
    private double sellingPrice;
    private double salePrice;
    private List<String> idsCategories = new ArrayList<>();
    private List<UploadImage> urlsImages = new ArrayList<>();

    public Product() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        this.setId(database.push().getKey());

    }
    public void checkProductNew(boolean checkProductNew){
        FirebaseHelper.databaseReference("products")
                .child(id)
                .setValue(this);
    }

    public void storeData(boolean statusDraft){
        FirebaseHelper.databaseReference("products")
                .child(id)
                .setValue(this);
    }
    public void delete(){
        FirebaseHelper.databaseReference("products")
                .child(id)
                .removeValue();
        for (int i=0; i<urlsImages.size(); i++){
            FirebaseHelper.storageReference("images")
                    .child("products")
                    .child(id)
                    .child("image_" + i + ".jpeg")
                    .delete();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public boolean isStatusDraft() {
        return statusDraft;
    }

    public void setStatusDraft(boolean statusDraft) {
        this.statusDraft = statusDraft;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(double wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public List<String> getIdsCategories() {
        return idsCategories;
    }

    public void setIdsCategories(List<String> idsCategories) {
        this.idsCategories = idsCategories;
    }

    public List<UploadImage> getUrlsImages() {
        return urlsImages;
    }

    public void setUrlsImages(List<UploadImage> urlsImages) {
        this.urlsImages = urlsImages;
    }
}
