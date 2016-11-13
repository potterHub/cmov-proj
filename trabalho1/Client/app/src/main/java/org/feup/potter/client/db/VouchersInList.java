package org.feup.potter.client.db;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class VouchersInList implements Serializable {
    public enum VOUCHER_TYPE {
        GLOBAL_DISCOUNT {
            public String toString() {
                return "Global discount";
            }
        },
        FREE_ITEM {
            public String toString() {
                return "Free Item";
            }
        },
        FREE_ITEM_TYPE {
            public String toString() {
                return "Free Item type";
            }
        }
    }

    private String idVoucher;

    private String codeVoucher;
    private String dateVoucherEmition;
    private String descriptionOfVoucher;

    private VOUCHER_TYPE voucherType;
    // In case of discount is percentage like (0.05 -> in a 5% discount)
    // and in case of free item is the number of items in the list
    private String valueOfdiscontOrNumberOfItems;

    // data that varies with voucher type
    private ArrayList<String> itemIdList;// if "free item" as a list of items

    private String typeItem;// if "free item type" insted of the item id tem o nome do tipo por exemplo cafe

    public VouchersInList(String idVoucher, String codeVoucher, String dateVoucher, String descriptionOfVoucher, String valueOfdiscontOrNumberOfItems) {
        this.idVoucher = idVoucher;

        this.codeVoucher = codeVoucher;
        this.dateVoucherEmition = dateVoucher;
        this.descriptionOfVoucher = descriptionOfVoucher;

        this.voucherType = VOUCHER_TYPE.GLOBAL_DISCOUNT;

        this.valueOfdiscontOrNumberOfItems = valueOfdiscontOrNumberOfItems;
        this.itemIdList = new ArrayList<String>();

        this.typeItem = "";
    }

    public String getIdVoucher() {
        return idVoucher;
    }

    public String getCodeVoucher() {
        return codeVoucher;
    }

    public String getCodeToShowUser(){
        String [] arr = this.codeVoucher.split("\\.");
        return arr.length > 0 ? arr[0] : this.codeVoucher;
    }

    public String getDateVoucherEmition() {
        return dateVoucherEmition;
    }

    public String getDescriptionOfVoucher() {
        return descriptionOfVoucher;
    }

    public VOUCHER_TYPE getVoucherType() {
        return voucherType;
    }

    public String getValueOfdiscontOrNumberOfItems() {
        return valueOfdiscontOrNumberOfItems;
    }

    public ArrayList<String> getItemIdList() {
        return itemIdList;
    }

    public String getItemIdListString() {
        return TextUtils.join(",", this.itemIdList);
    }

    public String getTypeItem() {
        return typeItem;
    }

    public void setCodeVoucher(String codeVoucher) {
        this.codeVoucher = codeVoucher;
    }

    public void setDateVoucherEmition(String dateVoucherEmition) {
        this.dateVoucherEmition = dateVoucherEmition;
    }

    public void setDescriptionOfVoucher(String descriptionOfVoucher) {
        this.descriptionOfVoucher = descriptionOfVoucher;
    }

    public void setVoucherType(VOUCHER_TYPE voucherType) {
        this.voucherType = voucherType;
    }

    public void setValueOfdiscontOrNumberOfItems(String valueOfdiscontOrNumberOfItems) {
        this.valueOfdiscontOrNumberOfItems = valueOfdiscontOrNumberOfItems;
    }

    public void setItemIdList(ArrayList<String> itemIdList) {
        this.itemIdList = itemIdList;
    }

    public void setTypeItem(String typeItem) {
        this.typeItem = typeItem;
    }

    public void setIdVoucher(String idVoucher) {
        this.idVoucher = idVoucher;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof VouchersInList)) return false;
        VouchersInList otherMyClass = (VouchersInList) other;
        return this.codeVoucher.equals(otherMyClass.getCodeVoucher());
    }

}
