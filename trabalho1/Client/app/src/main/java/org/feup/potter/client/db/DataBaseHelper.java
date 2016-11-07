package org.feup.potter.client.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;

// data base bridge
public class DataBaseHelper extends SQLiteOpenHelper {
    // data base file name
    public static final String DATABASE_NAME = "LocalClientDB.db";
    // schema version 1
    private static final int SCHEMA_VERSION = 1;

    private final int ITEM_ID = 0;
    private final int ITEM_NAME = 1;
    private final int ITEM_PRICE = 2;
    private final int ITEM_DESCRIPTION = 3;
    private final int ITEM_IMG = 4;
    private final int ITEM_TYPE = 5;

    private final int VOUCHER_ID = 0;
    private final int VOUCHER_CODE = 1;
    private final int VOUCHER_DATE = 2;
    private final int VOUCHER_DESCRIPTION = 3;
    private final int VOUCHER_TYPE = 4;
    private final int VOUCHER_VALUEDISCONTE_OR_NUM_ITEMS = 5;
    private final int VOUCHER_ITEM_LIST = 6;
    private final int VOUCHER_TYPE_ITEM = 7;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE items (id TEXT unique, name TEXT, price TEXT, description TEXT, image TEXT, type TEXT);");
        db.execSQL("CREATE TABLE voucher (id TEXT, code TEXT unique, date TEXT, description TEXT, type TEXT, value_disc_or_num_items TEXT, item_list TEXT, type_item TEXT);");
    }

    public void dropAllTables() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("items", null, null);
        db.delete("voucher", null, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    // **********************************************
    // ***************** ITEM ***********************
    // **********************************************

    // insert data into the data base
    public synchronized long insertItem(ItemInList data) {
        ContentValues row = new ContentValues();
        row.put("id", data.getIdItem());
        row.put("name", data.getName());
        row.put("price", data.getPrice());
        row.put("description", data.getDescription());
        row.put("image", data.getImageString());
        row.put("type", data.getItemType());
        return this.getWritableDatabase().insert("items", null, row);
    }

    public synchronized void updateItem(ItemInList data) {
        ContentValues row = new ContentValues();
        row.put("id", data.getIdItem());
        row.put("name", data.getName());
        row.put("price", data.getPrice());
        row.put("description", data.getDescription());
        row.put("image", data.getImageString());
        row.put("type", data.getItemType());

        String[] args = {data.getIdItem()};
        this.getWritableDatabase().update("items", row, "id=?", args);
    }

    // gets all data base item values
    public Cursor getAllItems() {
        return (this.getReadableDatabase().rawQuery("SELECT id, name, price, description, image, type FROM items", null));
    }

    public String getIdItem(Cursor c) {
        return (c.getString(this.ITEM_ID));
    }

    public String getNameItem(Cursor c) {
        return (c.getString(this.ITEM_NAME));
    }

    public String getPriceItem(Cursor c) {
        return (c.getString(this.ITEM_PRICE));
    }

    public String getDescriptionItem(Cursor c) {
        return (c.getString(this.ITEM_DESCRIPTION));
    }

    public String getImgItem(Cursor c) {return (c.getString(this.ITEM_IMG)); }

    public String getTypeItem(Cursor c) {
        return (c.getString(this.ITEM_TYPE));
    }

    public ItemInList getItemInList(Cursor c){
        return new ItemInList(getIdItem(c),getNameItem(c),getPriceItem(c),getDescriptionItem(c),getImgItem(c),getTypeItem(c));
    }

    // ****************************************************
    // ****************** vouchers ************************
    // ****************************************************
    // insert data into the data base
    public synchronized long insertVoucher(VouchersInList data) {
        ContentValues row = new ContentValues();
        row.put("id", data.getIdVoucher());
        row.put("code", data.getCodeVoucher());
        row.put("date", data.getDateVoucherEmition());
        row.put("description", data.getDescriptionOfVoucher());
        row.put("type", data.getVoucherType().toString());
        row.put("value_disc_or_num_items", data.getValueOfdiscontOrNumberOfItems());
        row.put("item_list", data.getItemIdListString());
        row.put("type_item", data.getTypeItem());
        return this.getWritableDatabase().insert("voucher", null, row);
    }

    public synchronized void updateVoucher(VouchersInList data) {
        ContentValues row = new ContentValues();
        row.put("id", data.getIdVoucher());
        row.put("code", data.getCodeVoucher());
        row.put("date", data.getDateVoucherEmition());
        row.put("description", data.getDescriptionOfVoucher());
        row.put("type", data.getVoucherType().toString());
        row.put("value_disc_or_num_items", data.getValueOfdiscontOrNumberOfItems());
        row.put("item_list", data.getItemIdListString());
        row.put("type_item", data.getTypeItem());

        String[] args = {data.getCodeVoucher()};
        this.getWritableDatabase().update("voucher", row, "code=?", args);
    }

    // gets all data base voucher rows
    public Cursor getAllVouchers() {
        return (this.getReadableDatabase().rawQuery("SELECT id, code, date, description , type, value_disc_or_num_items, item_list, type_item FROM voucher", null));
    }

    public String getIdVoucher(Cursor c) {
        return (c.getString(this.VOUCHER_ID));
    }

    public String getCodeVoucher(Cursor c) {
        return (c.getString(this.VOUCHER_CODE));
    }

    public String getDateVoucher(Cursor c) {
        return (c.getString(this.VOUCHER_DATE));
    }

    public String getDescriptionVoucher(Cursor c) {
        return (c.getString(this.VOUCHER_DESCRIPTION));
    }

    public String getTypeVoucher(Cursor c) {
        return (c.getString(this.VOUCHER_TYPE));
    }

    public String getValueDiscOrNumItemsVoucher(Cursor c) {
        return (c.getString(this.VOUCHER_VALUEDISCONTE_OR_NUM_ITEMS));
    }

    public ArrayList<String> getItemListVoucher(Cursor c) {
        String itemIdArray = c.getString(this.VOUCHER_ITEM_LIST);
        return new ArrayList<String>(Arrays.asList(itemIdArray.split(",")));
    }

    public String getTypeItemVoucher(Cursor c) {
        return (c.getString(this.VOUCHER_TYPE_ITEM));
    }

    public VouchersInList getVoucherInList(Cursor c){
        VouchersInList data = new VouchersInList(getIdVoucher(c),getCodeVoucher(c),getDateVoucher(c),getDescriptionVoucher(c),
                getValueDiscOrNumItemsVoucher(c));
        data.setVoucherType(VouchersInList.VOUCHER_TYPE.valueOf(getTypeVoucher(c)));
        data.setTypeItem(getTypeItemVoucher(c));
        data.setItemIdList(getItemListVoucher(c));
        return data;
    }
}