package org.feup.potter.client.db;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PastTransactionsInList {
    private String idSale;

    // 3 houses[nameItem, quantity, price]
    private JSONArray items;

    // 2 houses[Type voucher, code]
    private JSONArray vouchers;

    private String data;

    private String finalPrice;

    public PastTransactionsInList(String idSale, String data, String finalPrice) {
        this.idSale = idSale;
        this.data = data;

        this.items = new JSONArray();
        this.vouchers = new JSONArray();

        this.finalPrice = finalPrice;
    }

    public void setIdSale(String idSale) {
        this.idSale = idSale;
    }

    public void setItems(JSONArray arrJson) {
        this.items = arrJson;
    }

    public void setVouchers(JSONArray arrJson) {
        this.vouchers = arrJson;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getIdSale() {
        return idSale;
    }


    public JSONArray getVouchers() {
        return vouchers;
    }

    /*for (int x = 0; x < vouchers.length(); x++) {
        JSONObject voucherObj = vouchers.getJSONObject(x);
        JSONObject voucherTemplate = voucherObj.getJSONObject("voucherTemplate");
        String descriptionofVoucher = voucherTemplate.getString("description");
        String codeVoucher = voucherObj.getString("code");
    }*/

    public JSONArray getItems() {
        return items;
    }

    /*for (int x = 0; x < items.length(); x++) {
        JSONObject itemObj = items.getJSONObject(x);
        String idItem = itemObj.getString("idItem");
        String quantity = itemObj.getString("quantity");

        data.addItems(idItem, quantity, "price");
    }*/

    public String getData() {
        return data;
    }

    public String getOnlyDate() {
        String[] d = getData().split("T");
        return d.length > 0 ? d[0] : getData();
    }

    public String getOnlyHour() {
        String[] d = getData().split("T");
        if (d.length > 1) {
            String[] h = d[1].split("\\.");
            return h.length > 0 ? h[0] : "";
        } else
            return "";
    }


    public String getFinalPrice() {
        return finalPrice;
    }

    public String getTotalQuantityItems() throws JSONException {
        int i = 0;
        for (int x = 0; x < items.length(); x++) {
            JSONObject itemObj = items.getJSONObject(x);
            i += Integer.parseInt(itemObj.getString("quantity"));
        }
        return i + "";
    }

    public String getTotalVouchersUsed() {
        return this.vouchers.length() + "";
    }
}
