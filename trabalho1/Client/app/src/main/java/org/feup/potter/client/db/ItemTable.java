package org.feup.potter.client.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class ItemTable {
    private final int NAME = 1;
    private final int PRICE = 2;
    private final int QUANTITY = 3;
    private final int DESCRIPTION = 4;
    private final int IMG = 5;
    private final int TYPE_MENU = 6;

    private DataBaseHelper SQLDataBaseHelper;

    public ItemTable(DataBaseHelper dataBaseHelper) {
        this.SQLDataBaseHelper = dataBaseHelper;
    }

    public void creatTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE items (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, quantity INTEGER, description TEXT, img BLOB, typemenu TEXT);");
    }

    // insert data into the data base
    public long insertItem(String name, double price, int quantity, String description, Bitmap img, String typemenu) {
        ContentValues row = new ContentValues();
        row.put("name", name);
        row.put("price", price);
        row.put("quantity", quantity);
        row.put("description", description);
        row.put("img", getBytes(img));
        row.put("typemenu", typemenu);
        return this.SQLDataBaseHelper.getWritableDatabase().insert("items", null, row);
    }

    // update data in the data base
    public void updateItem(String id, String name, double price, int quantity, String description, Bitmap img, String typemenu) {
        ContentValues row = new ContentValues();
        row.put("name", name);
        row.put("price", price);
        row.put("quantity", quantity);
        row.put("description", description);
        row.put("img", getBytes(img));
        row.put("typemenu", typemenu);

        String[] args = {id};

        this.SQLDataBaseHelper.getWritableDatabase().update("items", row, "_ID=?", args);
    }

    // gets all data base values
    public Cursor getAll() {
        return (this.SQLDataBaseHelper.getReadableDatabase().rawQuery("SELECT _id, name FROM items", null));
    }

    // gets all data base values
    public Cursor getByItemName(String UserName) {
        String[] args = {UserName};
        return (this.SQLDataBaseHelper.getReadableDatabase().rawQuery("SELECT _id, name, price, quantity, description, img, typemenu FROM items WHERE name=?", args));
    }


    // gets items by id (this string id is the row id(long) received when inserting the value)
    public Cursor getByIdItem(String id) {
        String[] args = {id};
        return (this.SQLDataBaseHelper.getReadableDatabase().rawQuery("SELECT _id, name, price, quantity, description, img, typemenu FROM items WHERE _ID=?", args));
    }

    public String getName(Cursor c) {
        return (c.getString(this.NAME));
    }

    public double getPrice(Cursor c) {
        return (c.getDouble(this.PRICE));
    }

    public int getQuantity(Cursor c) {
        return (c.getInt(this.QUANTITY));
    }

    public String getDescription(Cursor c) {
        return (c.getString(this.DESCRIPTION));
    }

    public Bitmap getImg(Cursor c) {
        return (this.getImage(c.getBlob(this.IMG)));
    }

    public String getTypeMenu(Cursor c) {
        return (c.getString(this.TYPE_MENU));
    }

    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
