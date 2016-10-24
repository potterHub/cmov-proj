package org.feup.potter.client.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class ItemTable {
    private final int NAME = 1;
    private final int DESCRIPTION = 2;
    private final int PRICE = 3;
    private final int IMG = 4;

    private DataBaseHelper SQLDataBaseHelper;

    public ItemTable(DataBaseHelper dataBaseHelper) {
        this.SQLDataBaseHelper = dataBaseHelper;
    }

    public void creatTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE items (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, price REAL, img BLOB);");
    }

    // insert data into the data base
    public long insertItem(String name, String description, double price,Bitmap img) {
        ContentValues row = new ContentValues();
        row.put("name", name);
        row.put("description", description);
        row.put("price", price);
        row.put("img", getBytes(img));
        return this.SQLDataBaseHelper.getWritableDatabase().insert("items", null, row);
    }

    // update data in the data base
    public void updateItem(String id, String name, String description, double price,Bitmap img) {
        ContentValues row = new ContentValues();
        row.put("name", name);
        row.put("description", description);
        row.put("price", price);
        row.put("img", getBytes(img));

        String[] args={id};

        this.SQLDataBaseHelper.getWritableDatabase().update("items", row, "_ID=?", args);
    }

    // gets all data base values
    public Cursor getAll() {
        return(this.SQLDataBaseHelper.getReadableDatabase().rawQuery("SELECT _id, name, description, price, img FROM items", null));
    }

    // gets all data base values
    public Cursor getByItemName(String UserName) {
        String[] args={UserName};
        return(this.SQLDataBaseHelper.getReadableDatabase().rawQuery("SELECT _id, name, description, price, img FROM items WHERE name=?", args));
    }


    // gets items by id (this string id is the row id(long) received when inserting the value)
    public Cursor getByIdItem(String id) {
        String[] args={id};
        return(this.SQLDataBaseHelper.getReadableDatabase().rawQuery("SELECT _id, name, description, price, img FROM items WHERE _ID=?", args));
    }

    public String getName(Cursor c) {
        return(c.getString(this.NAME));
    }

    public String getDescription(Cursor c) {
        return(c.getString(this.DESCRIPTION));
    }

    public double getPrice(Cursor c) {
        return(c.getDouble(this.PRICE));
    }

    public Bitmap getImg(Cursor c) {
        return(this.getImage(c.getBlob(this.IMG)));
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
