package com.example.fashionecommerce.model;

public class Localtion {
    private String idLocaltion;
    private String nameLocaltion;

    public Localtion(String idLocaltion, String nameLocaltion) {
        this.idLocaltion = idLocaltion;
        this.nameLocaltion = nameLocaltion;
    }

    public String getIdLocaltion() {
        return idLocaltion;
    }

    public void setIdLocaltion(String idLocaltion) {
        this.idLocaltion = idLocaltion;
    }

    public String getNameLocaltion() {
        return nameLocaltion;
    }

    public void setNameLocaltion(String nameLocaltion) {
        this.nameLocaltion = nameLocaltion;
    }
}
