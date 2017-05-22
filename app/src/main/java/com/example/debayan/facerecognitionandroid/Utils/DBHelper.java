package com.example.debayan.facerecognitionandroid.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.debayan.facerecognitionandroid.User;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userData.db";
    public static final String COLUMN_ID = "_id";

    /* USERS TABLE */
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_COMPANY = "company";
    public static final String COLUMN_PASSWORD = "password";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //context.deleteDatabase(DATABASE_NAME);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createUserTable(db);
    }

    public void createUserTable(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_USERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_COMPANY + " TEXT, " +
                COLUMN_PASSWORD + " TEXT " +
                ");";
        db.execSQL(query);
    }

    public void dropUserTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropUserTable(db);
        onCreate(db);
    }

    public int getUserId(SQLiteDatabase db, User user) {
        Cursor mCursor = db.rawQuery("SELECT " + COLUMN_ID + "  FROM  " + TABLE_USERS + " WHERE " + COLUMN_NAME + "= '" + user.getUserName() + "'", null);
        mCursor.moveToFirst();
        return Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(COLUMN_ID)));
    }

    // Add new user
    public void addUser(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, user.getUserName());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_USERS, null, contentValues);
    }

    public ArrayList<User> getAllUsers() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE 1";

        ArrayList<User> users = new ArrayList<User>();

        // Cursor points to a location in query results
        Cursor c = db.rawQuery(query, null);
        // Move to first record
        c.moveToFirst();
        while (!c.isAfterLast()) {
            User user = new User();
            if (c.getString(c.getColumnIndex(COLUMN_NAME)) != null) {
                user.setId(Integer.parseInt(c.getString(c.getColumnIndex(COLUMN_ID))));
                user.setUserName(c.getString(c.getColumnIndex(COLUMN_NAME)));
            }
            c.moveToNext();
            users.add(user);
        }
        db.close();
        return users;
    }

    public int userCount(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT count(*) FROM " + TABLE_USERS;
        Cursor mcursor = db.rawQuery(query, null);
        mcursor.moveToFirst();
        return mcursor.getInt(0);
    }
}