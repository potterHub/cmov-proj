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

    // only used in order
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    // only used in order

    public ItemInList(String idItem, String name, String price, String description, String image, String itemType){
        this.idItem = idItem;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.itemType = itemType;
    }
    // only called when adding to the order so quantity == 1
    public ItemInList(ItemInList item){
        if(item != null) {
            this.idItem = item.idItem;
            this.name = item.name;
            this.price = item.price;
            this.description = item.description;
            this.image = item.image;
            this.itemType = item.itemType;
            this.quantity = 1;
        }
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

    public void incQ() {
        this.quantity++;
    }

    public void decQ() {
        this.quantity--;
    }

    // only checks the id
    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ItemInList))
            return false;
        ItemInList other = (ItemInList) obj;
        return idItem.equals(other.getIdItem());
    }
}
