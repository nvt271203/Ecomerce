package com.example.fashionecommerce.model;

import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Oder implements Serializable {
    private String id;
    private StatusOder status; //1 - chờ kiểm duyệt;  2 - đã duyệt;   3 - hủy duyệt
    private String idUser;  // ID người mua hàng
    private DeliveryAddress addressDelivery;  // Địa chỉ nhận hàng
    private List<ItemOder> itemOderList = new ArrayList<>();   // Danh sách sản phẩm đc đặt
    private long dataOder;  //
    private long dataStatusOder;  //
    private double total;   // Tổng tiền mua hàng
    private String pay;  // Phương thức thanh toán

    public Oder(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        this.setId(reference.push().getKey());
    }
    public void store(boolean checkDayOder){
        DatabaseReference referenceOderUser = FirebaseDatabase.getInstance().getReference("userOder")
                .child(idUser)
                .child(getId());

        referenceOderUser.setValue(this);

        DatabaseReference referenceOderAdmin = FirebaseDatabase.getInstance().getReference("adminOder")
                .child(getId());
        referenceOderAdmin.setValue(this);

        if (checkDayOder){
            DatabaseReference dataOderUser = referenceOderUser.child("dataOder");
            dataOderUser.setValue(ServerValue.TIMESTAMP);

            DatabaseReference dataOderAdmin = referenceOderAdmin.child("dataOder");
            dataOderAdmin.setValue(ServerValue.TIMESTAMP);


            // Thiết lập time duyệt đơn

            DatabaseReference dataStatusOderUser = referenceOderUser.child("dataStatusOder");
            dataStatusOderUser.setValue(ServerValue.TIMESTAMP);

            DatabaseReference dataStatusOderAdmin = referenceOderAdmin.child("dataStatusOder");
            dataStatusOderAdmin.setValue(ServerValue.TIMESTAMP);
        }else {
            DatabaseReference dataStatusOderUser = referenceOderUser.child("dataStatusOder");
            dataStatusOderUser.setValue(ServerValue.TIMESTAMP);

            DatabaseReference dataStatusOderAdmin = referenceOderAdmin.child("dataStatusOder");
            dataStatusOderAdmin.setValue(ServerValue.TIMESTAMP);
        }

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StatusOder getStatus() {
        return status;
    }

    public void setStatus(StatusOder status) {
        this.status = status;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public DeliveryAddress getAddressDelivery() {
        return addressDelivery;
    }

    public void setAddressDelivery(DeliveryAddress addressDelivery) {
        this.addressDelivery = addressDelivery;
    }

    public List<ItemOder> getItemOderList() {
        return itemOderList;
    }

    public void setItemOderList(List<ItemOder> itemOderList) {
        this.itemOderList = itemOderList;
    }

    public long getDataOder() {
        return dataOder;
    }

    public void setDataOder(long dataOder) {
        this.dataOder = dataOder;
    }

    public long getDataStatusOder() {
        return dataStatusOder;
    }

    public void setDataStatusOder(long dataStatusOder) {
        this.dataStatusOder = dataStatusOder;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}
