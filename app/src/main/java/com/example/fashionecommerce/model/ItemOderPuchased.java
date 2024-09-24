package com.example.fashionecommerce.model;

import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.google.firebase.database.FirebaseDatabase;

public class ItemOderPuchased {
    private String idProduct;
    private String name;
    private String urlImage;
    private int quantity;
    private double price;

    public ItemOderPuchased() {
    }

    private Double totalMoneyProduct(){
        return this.quantity * this.price;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
