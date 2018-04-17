package com.example.jspr97.mykid;

import android.app.Activity;
import android.util.SparseBooleanArray;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomListAdapter extends ArrayAdapter {

    private ArrayList<KidActivity> array;
    private ArrayList<KidActivity> filterList = null;
    private final Activity context;
    private SparseBooleanArray mSelectedItemsIds;

    public CustomListAdapter(Activity context, ArrayList<KidActivity> array ) {
        super(context, R.layout.listview_row, array);

        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.array = array;

        filterList = new ArrayList<KidActivity>();
        filterList.addAll(array);
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);

        // get references to objects in the listview_row.xml file
        TextView activityName = rowView.findViewById(R.id.activitynameID);
        TextView nameOfReporter = rowView.findViewById(R.id.nameofreporterID);

        // set text from the arrays
        activityName.setText(array.get(position).getName());
        nameOfReporter.setText(array.get(position).getReporter());

        return rowView;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        // clear selected
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

    public void filter(String query) {
        query = query.toLowerCase();
        array.clear();
        if (query.length() == 0)
            array.addAll(filterList);
        else {
            // filter by activity name
            for (KidActivity x : filterList) {
                if (!array.contains(x) && x.getName().toLowerCase().contains(query)) {
                    array.add(x);
                }
            }
            // filter by reporter name
            for (KidActivity x: filterList) {
                if (!array.contains(x) && x.getReporter().toLowerCase().contains(query)) {
                    array.add(x);
                }
            }
        }
        notifyDataSetChanged();
    }
}
