package com.example.coupondunia.treebo.database;


import android.provider.BaseColumns;

public class TreeboOfflineDatabase {

    public static class UserNotesTable implements BaseColumns {

        public static final String TABLE_NAME = "userNotes";
        public static final String ENTRY_ID = "_id";
        public static final String NOTES = "notes";
        public static final String NOTES_TITLE = "title";
        public static final String TIMESTAMP = "date";
    }

}
