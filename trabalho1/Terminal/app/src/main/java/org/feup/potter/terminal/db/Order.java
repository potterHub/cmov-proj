package org.feup.potter.terminal.db;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Order {
    private String userId;

    // [idItem, Quantity]
    public final int ID_ITEM = 0;
    public final int NUM_ITEM = 1;
    private ArrayList<String[]> items;

    // [code, type]
    public final int CODE_VOUCH = 0;
    public final int TYPE_VOUCH = 1;
    private ArrayList<String[]> vouchers;

    public Order(JSONObject obj) throws JSONException {
        this.userId = obj.getString("idUser");

        this.items = new ArrayList<String[]>();
        JSONArray jsonArrayItems = obj.getJSONArray("items");
        for (int i = 0; i < jsonArrayItems.length(); i++) {
            final JSONObject itemObj = jsonArrayItems.getJSONObject(i);
            String[] strArr = new String[2];
            strArr[ID_ITEM] = itemObj.getString("idItem");
            strArr[NUM_ITEM] = itemObj.getString("quantity");
            this.items.add(strArr);
        }

        this.vouchers = new ArrayList<String[]>();
        JSONArray jsonArrayVouchers = obj.getJSONArray("items");
        for (int i = 0; i < jsonArrayVouchers.length(); i++) {
            final JSONObject voucherObj = jsonArrayVouchers.getJSONObject(i);
            String[] strArr2 = new String[2];
            strArr2[CODE_VOUCH] = voucherObj.getString("code");
            strArr2[TYPE_VOUCH] = voucherObj.getString("type");
            this.vouchers.add(strArr2);
        }
    }

    public ArrayList<String[]> getItems() {
        return items;
    }

    public ArrayList<String[]> getVouchers() {
        return vouchers;
    }

    public String getUserId() {
        return userId;
    }
}
