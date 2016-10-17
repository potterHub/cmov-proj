package DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserTable {
    private final int NAME = 1;
    private final int USERNAME = 2;
    private final int PASSWORD = 3;
    private final int CREDITCARD = 4;

    private DataBaseHelper SQLDataBaseHelper;

    public UserTable(DataBaseHelper dataBaseHelper) {
        this.SQLDataBaseHelper = dataBaseHelper;
    }

    public void creatTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, username TEXT, password TEXT, creditcard TEXT);");
    }

    // insert data into the data base
    public long insertUser(String name, String username, String password, String creditcard) {
        ContentValues row = new ContentValues();
        row.put("name", name);
        row.put("username", username);
        row.put("password", password);
        row.put("creditcard", creditcard);
        return this.SQLDataBaseHelper.getWritableDatabase().insert("users", null, row);
    }

    // update data in the data base
    public void updateUser(String id, String name, String username, String password, String creditcard) {
        ContentValues row = new ContentValues();
        row.put("name", name);
        row.put("username", username);
        row.put("password", password);
        row.put("creditcard", creditcard);

        String[] args={id};

        this.SQLDataBaseHelper.getWritableDatabase().update("users", row, "_ID=?", args);
    }

    // gets all data base values
    public Cursor getAllUsers() {
        return(this.SQLDataBaseHelper.getReadableDatabase().rawQuery("SELECT _id, name, username, password, creditcard FROM users", null));
    }

    // gets all data base values
    public Cursor getByUserNameUser(String UserName) {
        String[] args={UserName};
        return(this.SQLDataBaseHelper.getReadableDatabase().rawQuery("SELECT _id, name, username, password, creditcard FROM users WHERE username=?", args));
    }


    // gets restaurant by id (this string id is the row id(long) received when inserting the value)
    public Cursor getByIdUser(String id) {
        String[] args={id};
        return(this.SQLDataBaseHelper.getReadableDatabase().rawQuery("SELECT _id, name, username, password, creditcard FROM users WHERE _ID=?", args));
    }

    public String getName(Cursor c) {
        return(c.getString(this.NAME));
    }

    public String getUserName(Cursor c) {
        return(c.getString(this.USERNAME));
    }

    public String getPassword(Cursor c) {
        return(c.getString(this.PASSWORD));
    }

    public String getCreditCard(Cursor c) {
        return(c.getString(this.CREDITCARD));
    }
}
