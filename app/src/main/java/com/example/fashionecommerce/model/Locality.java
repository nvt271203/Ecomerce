package com.example.fashionecommerce.model;

import java.io.Serializable;

public class Locality implements Serializable {

    private String nameLocaltion;
    private String nameLocality;

    public Locality() {
    }

    public Locality(String nameLocaltion, String nameLocality) {
        this.nameLocaltion = nameLocaltion;
        this.nameLocality = nameLocality;
    }

    public String getNameLocaltion() {
        return nameLocaltion;
    }

    public void setNameLocaltion(String nameLocaltion) {
        this.nameLocaltion = nameLocaltion;
    }

    public String getNameLocality() {
        return nameLocality;
    }

    public void setNameLocality(String nameLocality) {
        this.nameLocality = nameLocality;
    }
}
