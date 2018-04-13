package com.example.jspr97.mykid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListDetailFragment extends android.support.v4.app.Fragment {

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.activity_details, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // argument set in MainActivity using setArguments
        Bundle bundle = getArguments();

        if (bundle != null) {
            showDetails(bundle);
        }
    }

    public void showDetails(Bundle bundle){
        // set references
        TextView myText = rootView.findViewById(R.id.activitynameID);
        TextView myText1 = rootView.findViewById(R.id.locationID);
        TextView myText2 = rootView.findViewById(R.id.dateID);
        TextView myText3 = rootView.findViewById(R.id.timeID);
        TextView myText4 = rootView.findViewById(R.id.nameofreporterID);

        // retrieve strings from intent
        String savedExtra = bundle.getString("name");
        String savedExtra1 = bundle.getString("location");
        String savedExtra2 = bundle.getString("date");
        String savedExtra3 = bundle.getString("time");
        String savedExtra4 = bundle.getString("nameofreporter");

        // display strings
        myText.setText(savedExtra);
        myText1.setText(savedExtra1);
        myText2.setText(savedExtra2);
        myText3.setText(savedExtra3);
        myText4.setText(savedExtra4);
    }
}

