package com.example.jspr97.mykid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // set references
        TextView myText = (TextView) findViewById(R.id.activitynameID);
        TextView myText1 = (TextView) findViewById(R.id.locationID);
        TextView myText2 = (TextView) findViewById(R.id.dateID);
        TextView myText3 = (TextView) findViewById(R.id.timeID);
        TextView myText4 = (TextView) findViewById(R.id.nameofreporterID);

        // retrieve strings from intent
        String savedExtra = getIntent().getStringExtra("name");
        String savedExtra1 = getIntent().getStringExtra("location");
        String savedExtra2 = getIntent().getStringExtra("date");
        String savedExtra3 = getIntent().getStringExtra("time");
        String savedExtra4 = getIntent().getStringExtra("nameofreporter");

        // display strings
        myText.setText(savedExtra);
        myText1.setText(savedExtra1);
        myText2.setText(savedExtra2);
        myText3.setText(savedExtra3);
        myText4.setText(savedExtra4);

    }
}
