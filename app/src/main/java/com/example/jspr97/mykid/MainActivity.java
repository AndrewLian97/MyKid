package com.example.jspr97.mykid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String PREFERENCES = "prefs";
    public static final String KEY = "activities";

    private static final int REQUEST_CODE = 1;
    private ArrayList<KidActivity> array;
    private ListView listView;
    private CustomListAdapter listViewAdapter;
    private View parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentView = findViewById(R.id.coordinator);

        // retrieve saved arraylist
        getArrayList();

        // display arraylist in list view
        listViewAdapter = new CustomListAdapter(this, array );
        listView = (ListView) findViewById(R.id.listviewID);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // send selected data to view screen
                Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                String message = array.get(position).getName();
                String message1 = array.get(position).getLocation();
                String message2 = array.get(position).getDate();
                String message3 = array.get(position).getTime();
                String message4 = array.get(position).getReporter();
                intent.putExtra("name", message);
                intent.putExtra("location", message1);
                intent.putExtra("date", message2);
                intent.putExtra("time", message3);
                intent.putExtra("nameofreporter", message4);
                startActivity(intent);
            }
        });
    }

    private void getArrayList(){
        // retrieve saved arraylist
        SharedPreferences prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(KEY, null);
        Type type = new TypeToken<ArrayList<KidActivity>>() {}.getType();
        array = gson.fromJson(json, type);

        // create new arraylist if none saved
        if (array == null){
            array = new ArrayList<KidActivity>();

            // save
            SharedPreferences.Editor editor = prefs.edit();
            json = gson.toJson(array);
            editor.putString(MainActivity.KEY, json);
            editor.apply();
        }
    }

    public void onClickAdd(View view) {
        // go to input screen
        Intent intent = new Intent(this,InputActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // update arraylist and refresh listview
                getArrayList();
                listViewAdapter.add(array.get(array.size()-1));
                listViewAdapter.notifyDataSetChanged();

                Snackbar.make(parentView,
                        "New activity added",
                        Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
