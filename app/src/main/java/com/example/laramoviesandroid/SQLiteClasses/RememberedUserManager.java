package com.example.laramoviesandroid.SQLiteClasses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RememberedUserManager {
    private SQLiteDatabase db;
    public static final String TABLE_ROW_ID = "_id";
    public static final String TABLE_ROW_NAME = "name";
    public static final String TABLE_ROW_EMAIL = "email";
    public static final String TABLE_ROW_PASSWORD = "password";
    private static final String DB_NAME = "laramovies_android_db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_REMEMBERED_USER ="remembered_user";


    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {
        public CustomSQLiteOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String newTableQueryString = "create table "
                    + TABLE_REMEMBERED_USER + " ("
                    + TABLE_ROW_ID
                    + " integer primary key autoincrement not null,"
                    + TABLE_ROW_NAME
                    + " varchar(255) not null," // match the database settings from server
                    + TABLE_ROW_EMAIL
                    + " varchar(255) not null," // match the database settings from server
                    + TABLE_ROW_PASSWORD
                    + " varchar(64) not null);"; // max of 64 as bcrypt only allows 72
            db.execSQL(newTableQueryString);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

        public RememberedUserManager(Context context) {
        // Create an instance of our internal //CustomSQLiteOpenHelper class
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
// Get a writable database
        db = helper.getWritableDatabase();

    }

    /**
     * called on login
     * @param email
     * @param name
     * @param password
     */
    public void rememberUser(String email, String name,  String password){
        // delete anyone remembered first so we can ensure only single user is remembered
        removeRemember();
// Add all the details to the table
        String query = "INSERT INTO " + TABLE_REMEMBERED_USER + " (" +
                TABLE_ROW_EMAIL + ", " +
                TABLE_ROW_PASSWORD + ", " +
                TABLE_ROW_NAME + ") " +
                "VALUES (" +
                "'" + email + "'" + ", " +
                "'" + password + "'" + ", " +
                "'" + name + "'" +
                ");";
        Log.i("insert() = ", query);
        db.execSQL(query);
    }

    /**
     * call on logout
     */
    public void removeRemember(){
// Delete the details from the table if already exists
// no need to pinpoint an entry, the table is only supposed to store a single entry
        String query = "DELETE FROM " + TABLE_REMEMBERED_USER
//                + " WHERE " + TABLE_ROW_EMAIL
//                + " = '" + email + "';"
                ;
        Log.i("delete() = ", query);
        db.execSQL(query);
    }

    public Cursor getRememberedUser() {
        String query = "SELECT "
                + TABLE_ROW_NAME + ","
                + TABLE_ROW_EMAIL + ", "
                + TABLE_ROW_PASSWORD
                + " FROM " + TABLE_REMEMBERED_USER
                + " LIMIT 1 ";

        Cursor c = db.rawQuery(query, null);
        return c;
    }

    public boolean hasRememberedUser() {
        String query = "SELECT "
                + TABLE_ROW_ID
                + " FROM "
                + TABLE_REMEMBERED_USER
                +"; ";
        Cursor c = db.rawQuery(query, null);

        boolean hasUser= c.getCount() > 0;
        return hasUser;

    }


}
