package com.example.jspr97.mykid;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewFragment extends ListFragment {

    // allow fragment communicate with host activity
    public interface onMasterSelectedListener {
        public void onItemSelected(KidActivity kidActivity);
    }

    private onMasterSelectedListener myListener = null;
    private ArrayList<KidActivity> array;
    private CustomListAdapter adapter;

    // a method to accept listener from host activity
    public void setOnMasterSelectedListener(onMasterSelectedListener listener) {
        myListener = listener;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        // retrieve list of kidActivity from database
        UserSQL db = new UserSQL(getActivity());
        array = db.getKidActivityList();

        adapter = new CustomListAdapter(getActivity(), array);

        // set adapter
        setListAdapter(adapter);

        // minor configuration on the list
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // implement onItemClickListener
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (myListener != null) {
                    // send selected activity
                    myListener.onItemSelected(array.get(position));
                }
            }
        });
    }

    public void updateResult() {
        // update arraylist and refresh listview
        UserSQL db = new UserSQL(getActivity());
        array = db.getKidActivityList();
        adapter = new CustomListAdapter(getActivity(), array);
        setListAdapter(adapter);
    }
}
