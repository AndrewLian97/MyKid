package com.example.jspr97.mykid;

import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomListAdapter extends ArrayAdapter {

    private ArrayList<KidActivity> array;
    private final Activity context;
    private SparseBooleanArray mSelectedItemsIds;

    public CustomListAdapter(Activity context, ArrayList<KidActivity> array ) {
        super(context, R.layout.listview_row, array);

        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.array = array;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView activityName = (TextView) rowView.findViewById(R.id.activitynameID);
        TextView nameOfReporter = (TextView) rowView.findViewById(R.id.nameofreporterID);

        //this code sets the values of the objects to values from the arrays
        activityName.setText(array.get(position).getName());
        nameOfReporter.setText(array.get(position).getReporter());

        return rowView;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }



    public void update(ArrayList<KidActivity> newArray) {
        array = newArray;
    }
}
