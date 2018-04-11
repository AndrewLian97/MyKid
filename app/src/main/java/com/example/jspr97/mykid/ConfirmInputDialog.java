package com.example.jspr97.mykid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ConfirmInputDialog extends AppCompatDialogFragment {

    // bundle keys
    public static final String ACTIVITY_NAME = "activity name";
    public static final String LOCATION = "location";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String REPORTER = "reporter";

    private TextView activityName, location, date, time, reporter;
    private String activityNameString, locationString, dateString, timeString, reporterString;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final Activity currentActivity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        LayoutInflater inflater = currentActivity.getLayoutInflater();

        // inflate layout_confirm.xml for dialog
        View view = inflater.inflate(R.layout.layout_confirm, null);

        // set references
        activityName = view.findViewById(R.id.activityName);
        location = view.findViewById(R.id.location);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        reporter = view.findViewById(R.id.reporter);

        // retrieve strings from bundle
        activityNameString = getArguments().getString(ACTIVITY_NAME);
        locationString = getArguments().getString(LOCATION);
        dateString = getArguments().getString(DATE);
        timeString = getArguments().getString(TIME);
        reporterString = getArguments().getString(REPORTER);

        // display the strings in textview
        activityName.setText(activityNameString);
        location.setText(locationString);
        date.setText(dateString);
        time.setText(timeString);
        reporter.setText(reporterString);

        // build the dialog
        builder.setView(view)
                .setTitle("Confirm")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // store data into new KidActivity object
                        KidActivity newActivity = new KidActivity(activityNameString,
                                                                    locationString,
                                                                    dateString,
                                                                    timeString,
                                                                    reporterString);

                        // add the object into the arraylist and save
                        ArrayList<KidActivity> arrayList = getArrayList();
                        arrayList.add(newActivity);
                        saveArrayList(arrayList);
                        currentActivity.setResult(Activity.RESULT_OK, currentActivity.getIntent());
                        currentActivity.finish();
                    }
                });

        return builder.create();
    }

    private ArrayList<KidActivity> getArrayList() {
        SharedPreferences prefs = getContext().getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(MainActivity.KEY, null);
        Type type = new TypeToken<ArrayList<KidActivity>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private void saveArrayList(ArrayList<KidActivity> array){
        // convert arraylist to json string
        SharedPreferences prefs = getContext().getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(array);

        // save
        editor.putString(MainActivity.KEY, json);
        editor.apply();
    }
}
