package org.feup.potter.client.db;

import android.graphics.Bitmap;

import org.feup.potter.client.Util.Util;

import java.io.Serializable;

public class ItemInList implements Serializable{
    private String idItem;
    private String name;
    private String price;
    private String description;
    private String image;
    private String itemType;

    public ItemInList(String idItem, String name, String price, String description, String image, String itemType){
        this.idItem = idItem;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.itemType = itemType;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getIdItem() {
        return idItem;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getImage() {
        return Util.getBitmapFromString(image);
    }

    public String getItemType() {
        return itemType;
    }

    public String getImageString() {
        return image;
    }
}
