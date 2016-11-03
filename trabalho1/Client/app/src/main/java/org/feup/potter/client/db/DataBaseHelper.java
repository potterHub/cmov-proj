package org.feup.potter.client.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE items (id TEXT unique, name TEXT, price TEXT, description TEXT, image TEXT, type TEXT);");
    }

    public void dropAllTables() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("items", null, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    // insert data into the data base
    public long insertItem(String id, String name, String price, String description, String img, String type) {
        ContentValues row = new ContentValues();
        row.put("id", id);
        row.put("name", name);
        row.put("price", price);
        row.put("description", description);
        row.put("image", img);
        row.put("type", type);
        return this.getWritableDatabase().insert("items", null, row);
    }
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

    // update data in the data base
    public void updateItem(String idItem, String name, String price, String description, String img, String type) {
        ContentValues row = new ContentValues();
        row.put("id", idItem);
        row.put("name", name);
        row.put("price", price);
        row.put("description", description);
        row.put("image", img);
        row.put("type", type);

        String[] args = {idItem};

        this.getWritableDatabase().update("items", row, "id=?", args);
    }
    public void updateItem(ItemInList data) {
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

    // gets all data base values
    public Cursor getAllItems() {
        return (this.getReadableDatabase().rawQuery("SELECT id, name, price, description, image, type FROM items", null));
    }

    // gets all data base values
    public Cursor getByItemName(String UserName) {
        String[] args = {UserName};
        return (this.getReadableDatabase().rawQuery("SELECT id, name, price, description, image, type FROM items WHERE name=?", args));
    }


    // gets items by id (this string id is the row id(long) received when inserting the value)
    public Cursor getByIdItem(String id) {
        String[] args = {id};
        return (this.getReadableDatabase().rawQuery("SELECT id, name, price, description, image, type FROM items WHERE id=?", args));
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

    public String getImgItem(Cursor c) {return (c.getString(this.ITEM_IMG));
    }

    public String getTypeItem(Cursor c) {
        return (c.getString(this.ITEM_TYPE));
    }

    public ItemInList getItemInList(Cursor c){
        return new ItemInList(getIdItem(c),getNameItem(c),getPriceItem(c),getDescriptionItem(c),getImgItem(c),getTypeItem(c));
    }
}