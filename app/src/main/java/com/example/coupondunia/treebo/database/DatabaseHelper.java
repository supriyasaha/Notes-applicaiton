package com.example.coupondunia.treebo.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.coupondunia.treebo.TreeboApplication;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE = "notes.db";
    public static final int DATABASE_VERSION = 1;


    private static final String USER_NOTES_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TreeboOfflineDatabase.UserNotesTable.TABLE_NAME + " ("
                    + TreeboOfflineDatabase.UserNotesTable.ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + TreeboOfflineDatabase.UserNotesTable.NOTES + " TEXT,"
                    + TreeboOfflineDatabase.UserNotesTable.NOTES_TITLE + " TEXT,"
                    + TreeboOfflineDatabase.UserNotesTable.TIMESTAMP + " INTEGER"
                    + ")";


    private static DatabaseHelper mDatabaseHelper = new DatabaseHelper(TreeboApplication.getAppCOntext());
    private SQLiteDatabase mDatabaseManipulator;


    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance() {
        return mDatabaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public synchronized SQLiteDatabase getDatabseManipulater() {
        if (mDatabaseManipulator == null) {
            mDatabaseManipulator = getWritableDatabase();
        }
        return mDatabaseManipulator;
    }
}
