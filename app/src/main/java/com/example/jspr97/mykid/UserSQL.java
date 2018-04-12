package com.example.jspr97.mykid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class UserSQL {
    private DBHelper dbHelper;

    public UserSQL(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(KidActivity kidActivity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // insert kidActivity details into database
        ContentValues values = new ContentValues();
        values.put(KidActivity.KEY_NAME, kidActivity.getName());
        values.put(KidActivity.KEY_LOCATION, kidActivity.getLocation());
        values.put(KidActivity.KEY_DATE, kidActivity.getDate());
        values.put(KidActivity.KEY_TIME, kidActivity.getTime());
        values.put(KidActivity.KEY_REPORTER, kidActivity.getReporter());

        long activity_id = db.insert(KidActivity.TABLE_NAME, null, values);
        db.close();         // close connection
        return (int) activity_id;
    }

    public void delete(int activity_id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(KidActivity.TABLE_NAME, KidActivity.KEY_ID + "= ?",
                new String[] {String.valueOf(activity_id)});
        db.close();
    }

    public void update(KidActivity kidActivity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KidActivity.KEY_NAME, kidActivity.getName());
        values.put(KidActivity.KEY_LOCATION, kidActivity.getLocation());
        values.put(KidActivity.KEY_DATE, kidActivity.getDate());
        values.put(KidActivity.KEY_TIME, kidActivity.getTime());
        values.put(KidActivity.KEY_REPORTER, kidActivity.getReporter());

        db.update(KidActivity.TABLE_NAME, values, KidActivity.KEY_ID + "= ?",
                new String[] {String.valueOf(kidActivity.getId())});
        db.close();     // close connection
    }

    public ArrayList<KidActivity> getKidActivityList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // retrieve all rows alphabetically
        String query = "SELECT " +
                KidActivity.KEY_ID + ", " +
                KidActivity.KEY_NAME + ", " +
                KidActivity.KEY_LOCATION + ", " +
                KidActivity.KEY_DATE + ", " +
                KidActivity.KEY_TIME + ", " +
                KidActivity.KEY_REPORTER +
                " FROM " + KidActivity.TABLE_NAME +
                " ORDER BY " + KidActivity.KEY_NAME +
                " COLLATE NOCASE";      // sort case insensitive

        ArrayList<KidActivity> kidActivityList = new ArrayList<KidActivity>();
        Cursor cursor = db.rawQuery(query, null);

        // initialize kidActivity objects then add to array
        if (cursor.moveToFirst()) {
            do {
                KidActivity kidActivity = new KidActivity();
                kidActivity.setId(cursor.getInt(cursor.getColumnIndex(KidActivity.KEY_ID)));
                kidActivity.setName(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_NAME)));
                kidActivity.setLocation(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_LOCATION)));
                kidActivity.setDate(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_DATE)));
                kidActivity.setTime(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_TIME)));
                kidActivity.setReporter(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_REPORTER)));
                kidActivityList.add(kidActivity);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();         // close connection
        return kidActivityList;
    }

    public KidActivity getKidActivity(int activity_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // retrieve row matching the id
        String query = "SELECT " +
                KidActivity.KEY_ID + ", " +
                KidActivity.KEY_NAME + ", " +
                KidActivity.KEY_LOCATION + ", " +
                KidActivity.KEY_DATE + ", " +
                KidActivity.KEY_TIME + ", " +
                KidActivity.KEY_REPORTER +
                " FROM " + KidActivity.TABLE_NAME +
                " WHERE " + KidActivity.KEY_ID + "= ?";

        KidActivity kidActivity = new KidActivity();
        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(activity_id)});

        // initialize kidActivity object
        if (cursor.moveToFirst()) {
            do {
                kidActivity.setId(cursor.getInt(cursor.getColumnIndex(KidActivity.KEY_ID)));
                kidActivity.setName(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_NAME)));
                kidActivity.setLocation(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_LOCATION)));
                kidActivity.setDate(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_DATE)));
                kidActivity.setTime(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_TIME)));
                kidActivity.setReporter(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_REPORTER)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();         // close connection
        return kidActivity;
    }
}
