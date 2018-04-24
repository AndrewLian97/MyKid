package com.example.jspr97.mykid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class UserSQL {
    public static final int ALPHA_ASC = 1;
    public static final int ALPHA_DESC = 2;
    public static final int DATE_ASC = 3;
    public static final int DATE_DESC = 4;

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
        values.put(KidActivity.KEY_DATE, inputDateFormat(kidActivity.getDate()));
        values.put(KidActivity.KEY_TIME, kidActivity.getTime());
        values.put(KidActivity.KEY_REPORTER, kidActivity.getReporter());
        values.put(KidActivity.KEY_IMAGE, kidActivity.getImagePath());

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
        values.put(KidActivity.KEY_DATE, inputDateFormat(kidActivity.getDate()));
        values.put(KidActivity.KEY_TIME, kidActivity.getTime());
        values.put(KidActivity.KEY_REPORTER, kidActivity.getReporter());
        values.put(KidActivity.KEY_IMAGE, kidActivity.getImagePath());

        db.update(KidActivity.TABLE_NAME, values, KidActivity.KEY_ID + "= ?",
                new String[] {String.valueOf(kidActivity.getId())});
        db.close();     // close connection
    }

    public ArrayList<KidActivity> getKidActivityList(int order) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " +
                KidActivity.KEY_ID + ", " +
                KidActivity.KEY_NAME + ", " +
                KidActivity.KEY_LOCATION + ", " +
                KidActivity.KEY_DATE + ", " +
                KidActivity.KEY_TIME + ", " +
                KidActivity.KEY_REPORTER + ", " +
                KidActivity.KEY_IMAGE +
                " FROM " + KidActivity.TABLE_NAME;
        if (order == ALPHA_ASC) {
            // retrieve all rows alphabetically
            query += " ORDER BY " + KidActivity.KEY_NAME +
                    " COLLATE NOCASE";      // sort case insensitive
        } else if (order == ALPHA_DESC) {
            // retrieve all rows alphabetically descending
            query += " ORDER BY " + KidActivity.KEY_NAME +
                    " COLLATE NOCASE DESC";
        } else if (order == DATE_ASC) {
            // retrieve all rows start from oldest
            query += " ORDER BY date(" + KidActivity.KEY_DATE + ")";
        } else if (order == DATE_DESC) {
            // retrieve all rows start from newest
            query += " ORDER BY date(" + KidActivity.KEY_DATE + ") DESC";
        }

        ArrayList<KidActivity> kidActivityList = new ArrayList<KidActivity>();
        Cursor cursor = db.rawQuery(query, null);

        // initialize kidActivity objects then add to array
        if (cursor.moveToFirst()) {
            do {
                KidActivity kidActivity = new KidActivity();
                kidActivity.setId(cursor.getInt(cursor.getColumnIndex(KidActivity.KEY_ID)));
                kidActivity.setName(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_NAME)));
                kidActivity.setLocation(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_LOCATION)));
                kidActivity.setDate(outputDateFormat(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_DATE))));
                kidActivity.setTime(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_TIME)));
                kidActivity.setReporter(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_REPORTER)));
                kidActivity.setImagePath(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_IMAGE)));
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
                KidActivity.KEY_REPORTER + ", " +
                KidActivity.KEY_IMAGE +
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
                kidActivity.setDate(outputDateFormat(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_DATE))));
                kidActivity.setTime(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_TIME)));
                kidActivity.setReporter(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_REPORTER)));
                kidActivity.setImagePath(cursor.getString(cursor.getColumnIndex(KidActivity.KEY_IMAGE)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();         // close connection
        return kidActivity;
    }

    private String inputDateFormat(String date) {
        // format date to yyyy-mm-dd
        String formatDate = date.substring(6) + '-' +
                date.substring(3,5) + '-' +
                date.substring(0,2);
        return formatDate;
    }

    private String outputDateFormat(String date) {
        // format date to dd/mm-/yyy
        String displayDate = date.substring(8) + '/' +
                date.substring(5,7) + '/' +
                date.substring(0,4);
        return displayDate;
    }
}
