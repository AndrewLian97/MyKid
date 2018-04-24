package com.example.jspr97.mykid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "activities.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_KIDACTIVITY = "CREATE TABLE " + KidActivity.TABLE_NAME + "("
                + KidActivity.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KidActivity.KEY_NAME + " TEXT, "
                + KidActivity.KEY_LOCATION + " TEXT, "
                + KidActivity.KEY_DATE + " TEXT, "
                + KidActivity.KEY_TIME + " TEXT, "
                + KidActivity.KEY_REPORTER + " TEXT, "
                + KidActivity.KEY_IMAGE + " TEXT )";

        db.execSQL(CREATE_TABLE_KIDACTIVITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + KidActivity.TABLE_NAME);

        // Create table again
        onCreate(db);
    }
}
