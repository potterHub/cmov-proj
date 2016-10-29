package org.feup.potter.client.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.feup.potter.client.Util.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class ItemTable {
    private final int ITEM_ID = 0;
    private final int NAME = 1;
    private final int PRICE = 2;
    private final int DESCRIPTION = 3;
    private final int IMG = 4;
    private final int TYPE = 5;

    private DataBaseHelper SQLDataBaseHelper;

    public ItemTable(DataBaseHelper dataBaseHelper) {
        this.SQLDataBaseHelper = dataBaseHelper;
    }

    public void creatTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE items (itemId INTEGER PRIMARY KEY, name TEXT, price REAL, description TEXT, img BLOB, type TEXT);");
    }

    // insert data into the data base
    public long insertItem(int id, String name, double price, String description, Bitmap img, String type) {
        ContentValues row = new ContentValues();
        row.put("itemId", id);
        row.put("name", name);
        row.put("price", price);
        row.put("description", description);
        row.put("img", getBytes(img));
        row.put("type", type);
        return this.SQLDataBaseHelper.getWritableDatabase().insert("items", null, row);
    }

    // update data in the data base
    public void updateItem(String id, String name, double price, String description, Bitmap img, String type) {
        ContentValues row = new ContentValues();
        row.put("name", name);
        row.put("price", price);
        row.put("description", description);
        row.put("img", getBytes(img));
        row.put("type", type);

        String[] args = {id};

        this.SQLDataBaseHelper.getWritableDatabase().update("items", row, "itemId=?", args);
    }

    // gets all data base values
    public Cursor getAll() {
        return (this.SQLDataBaseHelper.getReadableDatabase().rawQuery("SELECT itemId, name, price, description, img, type FROM items", null));
    }

    // gets all data base values
    public Cursor getByItemName(String UserName) {
        String[] args = {UserName};
        return (this.SQLDataBaseHelper.getReadableDatabase().rawQuery("SELECT itemId, name, price, description, img, type FROM items WHERE name=?", args));
    }


    // gets items by id (this string id is the row id(long) received when inserting the value)
    public Cursor getByIdItem(String id) {
        String[] args = {id};
        return (this.SQLDataBaseHelper.getReadableDatabase().rawQuery("SELECT itemId, name, price, description, img, type FROM items WHERE itemId=?", args));
    }

    public int getId(Cursor c) {
        return (c.getInt(this.ITEM_ID));
    }

    public String getName(Cursor c) {
        return (c.getString(this.NAME));
    }

    public double getPrice(Cursor c) {
        return (c.getDouble(this.PRICE));
    }

    public String getDescription(Cursor c) {
        return (c.getString(this.DESCRIPTION));
    }

    public Bitmap getImg(Cursor c) {
        return (this.getImage(c.getBlob(this.IMG)));
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
    private Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public JSONObject getJSON(Cursor c) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("name", this.getName(c));
        obj.put("price", this.getPrice(c) + "");
        obj.put("description", this.getDescription(c));
        obj.put("img", Util.getStringFromBitmap(this.getImg(c)));
        obj.put("type", this.getType(c));
        return obj;
    }
}
