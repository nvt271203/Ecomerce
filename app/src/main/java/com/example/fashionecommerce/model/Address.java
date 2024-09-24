package com.example.fashionecommerce.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Address {
    private String id;
    private String location; // vị trí (thành phố - tỉnh)
    private String locality; // địa phương(thị trấn - huyện)
  //  private String townShip; // địa phương(thị trấn - huyện)
    private String street; // đường phố (bao gồm số nhà)
    public Address(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        this.setId(reference.push().getKey());
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocality() {
        return locality;
    }
    public void setLocality(String locality) {
        this.locality = locality;
    }

//    public String getTownShip() {
//        return townShip;
//    }
//
//    public void setTownShip(String townShip) {
//        this.townShip = townShip;
//    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress(){
        return getLocation() + " - " + getLocality();
    }
}
