package com.example.fashionecommerce.model;

import android.widget.ImageView;

import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.ktx.Firebase;

import java.util.List;

public class Chat {
    private String id;
    private String idSender;
    private String idRecever;
    private String message;
    private String pathImage;
    private List<ItemSuggest> listSuggest;
    private List<Product> listProductFilter;
    private long dataTime;

    public Chat() {
        this.setId(FirebaseDatabase.getInstance().getReference().push().getKey());
    }
    public void storeSendUser(boolean checkTime){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat")
                .child("cJLkDRhb5PdlofqIAAAhXWUrjby1")
                .child(idRecever)
                .child(id);
                reference.setValue(this);

        if (checkTime){
            reference.child("dataTime").setValue(ServerValue.TIMESTAMP);
        }
    }

    public void storeSendAdmin(boolean checkTime){
        String idUser = idSender;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat")
                .child("cJLkDRhb5PdlofqIAAAhXWUrjby1")
                .child(idUser)
                .child(id);
        reference.setValue(this);

        if (checkTime){
            reference.child("dataTime").setValue(ServerValue.TIMESTAMP);
        }
    }
//    public void store(String idSender, String idRecever, String message){
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("chat");
//
//        databaseReference.child()
//        DatabaseReference dataOderUser = referenceOderUser.child("dataOder");
//        dataOderUser.setValue(ServerValue.TIMESTAMP);
//    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public long getDataTime() {
        return dataTime;
    }

    public void setDataTime(long dataTime) {
        this.dataTime = dataTime;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdRecever() {
        return idRecever;
    }

    public void setIdRecever(String idRecever) {
        this.idRecever = idRecever;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ItemSuggest> getListSuggest() {
        return listSuggest;
    }

    public void setListSuggest(List<ItemSuggest> listSuggest) {
        this.listSuggest = listSuggest;
    }

    public List<Product> getListProductFilter() {
        return listProductFilter;
    }

    public void setListProductFilter(List<Product> listProductFilter) {
        this.listProductFilter = listProductFilter;
    }
}
