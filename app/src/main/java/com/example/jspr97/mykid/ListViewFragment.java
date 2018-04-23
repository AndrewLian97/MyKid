package com.example.jspr97.mykid;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import android.widget.AbsListView.MultiChoiceModeListener;


public class ListViewFragment extends ListFragment {

    // allow fragment communicate with host activity
    public interface onMasterSelectedListener {
        public void onItemSelected(KidActivity kidActivity);
    }

    public int order = UserSQL.ALPHA_ASC;

    private onMasterSelectedListener myListener = null;
    private ArrayList<KidActivity> array;
    private CustomListAdapter adapter;
    private ActionMode actionMode;

    // a method to accept listener from host activity
    public void setOnMasterSelectedListener(onMasterSelectedListener listener) {
        myListener = listener;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (actionMode != null)
            actionMode.finish();    // close CAB when change screen
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        setEmptyText("No activities to show");

        // retrieve list of kidActivity from database
        UserSQL db = new UserSQL(getActivity());
        array = db.getKidActivityList(order);

        // set list adapter
        adapter = new CustomListAdapter(getActivity(), array);
        setListAdapter(adapter);

        // enable multiple select
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

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

        // multi select list items
        getListView().setMultiChoiceModeListener(new MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = getListView().getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                adapter.toggleSelection(position);
            }
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        // confirmation dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Confirm Delete")
                                .setMessage("Are you sure you want to delete?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Calls getSelectedIds method from ListViewAdapter Class
                                        SparseBooleanArray selected = adapter
                                                .getSelectedIds();
                                        int n = adapter.getSelectedCount();
                                        // Captures all selected ids with a loop
                                        for (int i = (selected.size() - 1); i >= 0; i--) {
                                            if (selected.valueAt(i)) {
                                                KidActivity selectedItem = (KidActivity) adapter
                                                        .getItem(selected.keyAt(i));

                                                // Remove selected items following the ids
                                                adapter.remove(selectedItem);
                                                UserSQL db = new UserSQL(getActivity());
                                                db.delete(selectedItem.getId());
                                            }
                                        }
                                        // Close CAB
                                        actionMode.finish();

                                        Snackbar.make(getActivity().findViewById(R.id.coordinator),
                                                 n + " deleted", Snackbar.LENGTH_LONG).show();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.show();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.cab_menu, menu);
                actionMode = mode;
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
                adapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        }

    public void updateResult() {
        // update arraylist and refresh listview
        UserSQL db = new UserSQL(getActivity());
        array = db.getKidActivityList(order);
        adapter = new CustomListAdapter(getActivity(), array);
        setListAdapter(adapter);
    }

    public void filterList(String query) {
        adapter.filter(query);
    }
}
