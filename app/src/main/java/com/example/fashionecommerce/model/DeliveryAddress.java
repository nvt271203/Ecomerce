package com.example.fashionecommerce.model;

import android.util.Log;
import android.widget.Toast;

import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.Objects;

public class DeliveryAddress implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String address;
    private String location;
    private String locality;
    private String street;

    public DeliveryAddress() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        this.setId(reference.push().getKey());
    }
    public DeliveryAddress(String name, String phone, String address, String street) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.street = street;
    }
//    public void storeData(){
//        String idUserCurrent ="";
//        if (FirebaseHelper.checkUserCurrent()){
//            idUserCurrent = FirebaseHelper.getUIDpersonCurrent();
//        }
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        reference.child("user")
//                .child(idUserCurrent)
//                .child(this.id)
//                .setValue(this);
//    }
public void storeData() {
    String idUserCurrent = "";
    if (FirebaseHelper.checkUserCurrent()) {
        idUserCurrent = FirebaseHelper.getUIDpersonCurrent();
        Log.i("checkUID", FirebaseHelper.getUIDpersonCurrent());
    }

    // Kiểm tra nếu idUserCurrent không rỗng hoặc null và this.id cũng không rỗng hoặc null trước khi sử dụng nó
    if (idUserCurrent != null && !idUserCurrent.isEmpty() && this.id != null && !this.id.isEmpty()) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("user")
                .child(idUserCurrent)
                .setValue(this);
        Log.i("checkUID", FirebaseHelper.getUIDpersonCurrent());
    } else {
        // Xử lý trường hợp idUserCurrent hoặc this.id là rỗng hoặc null
        // Ví dụ: Log thông báo lỗi, hoặc thực hiện hành động khác tùy thuộc vào logic của bạn
    }
}

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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
}
