package org.feup.potter.client.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// data base bridge
public class DataBaseHelper extends SQLiteOpenHelper {
    // data base name
    private static final String DATABASE_NAME = "LocalClientDb.db";

    // schema version 1
    private static final int SCHEMA_VERSION = 1;

    // tables
    private UserTable users;
    private ItemTable items;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);

        this.users = new UserTable(this);
        this.items = new ItemTable(this);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.users.creatTable(db);
        this.items.creatTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public UserTable getUsersTable(){
        return users;
    }
    public ItemTable getItemsTable(){
        return items;
    }
}