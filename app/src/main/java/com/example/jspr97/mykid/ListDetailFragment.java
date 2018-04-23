package com.example.jspr97.mykid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListDetailFragment extends android.support.v4.app.Fragment {

    private View rootView;
    private TextView myText, myText1, myText2, myText3, myText4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_details, container, false);

        // set references
        myText = rootView.findViewById(R.id.activitynameID);
        myText1 = rootView.findViewById(R.id.locationID);
        myText2 = rootView.findViewById(R.id.dateID);
        myText3 = rootView.findViewById(R.id.timeID);
        myText4 = rootView.findViewById(R.id.nameofreporterID);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // arguments set from ViewActivity
        Bundle bundle = getArguments();

        if (bundle != null) {
            showDetails(bundle);
        }
    }

    public void showDetails(Bundle bundle){
        // retrieve strings
        int id = bundle.getInt(KidActivity.KEY_ID);
        UserSQL db = new UserSQL(getActivity());
        KidActivity kidActivity = db.getKidActivity(id);

        // display strings
        myText.setText(kidActivity.getName());
        myText1.setText(kidActivity.getLocation());
        myText2.setText(kidActivity.getDate());
        myText3.setText(kidActivity.getTime());
        myText4.setText(kidActivity.getReporter());
    }
}

