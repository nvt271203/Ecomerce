package com.example.fashionecommerce.model;

import java.io.Serializable;

public class UploadImage implements Serializable {
    private int index;
    private String pathUrlSelected;

    public UploadImage() {
    }

    public UploadImage(int index, String pathUrlSelected) {
        this.index = index;
        this.pathUrlSelected = pathUrlSelected;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPathUrlSelected() {
        return pathUrlSelected;
    }

    public void setPathUrlSelected(String pathUrlSelected) {
        this.pathUrlSelected = pathUrlSelected;
    }
}
