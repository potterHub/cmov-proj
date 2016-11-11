package org.feup.potter.client.db;


import java.util.ArrayList;

public class PastTransactionsInList {
    private String idSale;

    // 3 houses[nameItem, quantity, price]
    private ArrayList<String[]> items;

    // 2 houses[Type voucher, code]
    private ArrayList<String[]> vouchers;

    private String data;

    public PastTransactionsInList(String idSale, String data) {
        this.idSale = idSale;
        this.data = data;

        this.items = new ArrayList<String[]>();
        this.vouchers = new ArrayList<String[]>();
    }

    public void setIdSale(String idSale) {
        this.idSale = idSale;
    }

    public void addItems(String nameItem, String quantity, String price) {
        String[] arr = {nameItem, quantity, price};
        this.items.add(arr);
    }

    public void addVouchers(String type, String code) {
        String[] arr = {type, code};
        this.vouchers.add(arr);
    }

    public String getData() {
        return data;
    }

    public String getIdSale() {
        return idSale;
    }

    public ArrayList<String[]> getItems() {
        return items;
    }

    public ArrayList<String[]> getVouchers() {
        return vouchers;
    }

    public void setData(String data) {
        this.data = data;
    }

}
