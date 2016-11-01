package org.feup.potter.client.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.feup.potter.client.Util.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class ItemTable {
    private final int ITEM_ID = 1;
    private final int NAME = 2;
    private final int PRICE = 3;
    private final int DESCRIPTION = 4;
    private final int IMG = 5;
    private final int TYPE = 6;

    private DataBaseHelper SQLDataBaseHelper;

    public ItemTable(DataBaseHelper dataBaseHelper) {
        this.SQLDataBaseHelper = dataBaseHelper;
    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE items (_id INTEGER PRIMARY KEY AUTOINCREMENT,idItem TEXT, name TEXT, price TEXT, description TEXT, img TEXT, type TEXT);");
    }

    // insert data into the data base
    public long insertItem(String id, String name, String price, String description, String img, String type) {
        ContentValues row = new ContentValues();
        row.put("idItem", id);
        row.put("name", name);
        row.put("price", price);
        row.put("description", description);
        row.put("img", img);
        row.put("type", type);
        return this.SQLDataBaseHelper.getWritableDatabase().insert("items", null, row);
    }

    // update data in the data base
    public void updateItem(String id, String idItem, String name, String price, String description, String img, String type) {
        ContentValues row = new ContentValues();
        row.put("idItem", idItem);
        row.put("name", name);
        row.put("price", price);
        row.put("description", description);
        row.put("img", img);
        row.put("type", type);

        String[] args = {id};

        this.SQLDataBaseHelper.getWritableDatabase().update("items", row, "_id=?", args);
    }

    // gets all data base values
    public Cursor getAll() {
        return (this.SQLDataBaseHelper.getReadableDatabase().rawQuery("SELECT _id, idItem, name, price, description, img, type FROM items", null));
    }

    // gets all data base values
    public Cursor getByItemName(String UserName) {
        String[] args = {UserName};
        return (this.SQLDataBaseHelper.getReadableDatabase().rawQuery("SELECT _id, idItem, name, price, description, img, type FROM items WHERE name=?", args));
    }


    // gets items by id (this string id is the row id(long) received when inserting the value)
    public Cursor getByIdItem(String id) {
        String[] args = {id};
        return (this.SQLDataBaseHelper.getReadableDatabase().rawQuery("SELECT _id, idItem, name, price, description, img, type FROM items WHERE _id=?", args));
    }

    public String getIdItem(Cursor c) {
        return (c.getString(this.ITEM_ID));
    }

    public String getName(Cursor c) {
        return (c.getString(this.NAME));
    }

    public String getPrice(Cursor c) {
        return (c.getString(this.PRICE));
    }

    public String getDescription(Cursor c) {
        return (c.getString(this.DESCRIPTION));
    }

    public Bitmap getImg(Cursor c) {
        String img = c.getString(this.IMG);
        if (img == null || img.equals(""))
            return null;
        else
            return this.getImage(img);
    }

    public String getType(Cursor c) {
        return (c.getString(this.TYPE));
    }

    private byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    private Bitmap getImage(String img) {
        byte[] encodeByte = Base64.decode(img, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
    }

    public JSONObject getJSON(Cursor c) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("_id", this.getIdItem(c));
        obj.put("name", this.getName(c));
        obj.put("price", this.getPrice(c));
        obj.put("description", this.getDescription(c));
        obj.put("img", Util.getStringFromBitmap(this.getImg(c)));
        obj.put("type", this.getType(c));
        return obj;
    }
}
