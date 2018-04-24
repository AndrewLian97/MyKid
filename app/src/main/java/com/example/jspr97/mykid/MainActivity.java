package com.example.jspr97.mykid;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.support.v7.widget.SearchView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_NEW = 1;
    private static final int REQUEST_CODE_VIEW = 2;
    private View coordinatorView;
    private boolean landscape;
    private ListViewFragment listViewFragment;
    private int currentActivityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listViewFragment = null;
        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        coordinatorView = findViewById(R.id.coordinator);

        // if this is under portrait mode
        if (frameLayout != null) {
            landscape = false;

            FragmentTransaction t1 = getSupportFragmentManager().beginTransaction();
            listViewFragment = (ListViewFragment) getSupportFragmentManager().findFragmentByTag("ListView");

            // first time loading of fragment
            if (listViewFragment == null) {
                listViewFragment = new ListViewFragment();
                // add to frame layout
                t1.add(R.id.frameLayout, listViewFragment, "ListView");
            }
            // save work
            t1.commit();
        } else {
            // landscape mode
            landscape = true;

            FragmentTransaction t2 = getSupportFragmentManager().beginTransaction();
            listViewFragment = (ListViewFragment) getSupportFragmentManager().findFragmentById(R.id.frameLayoutList);

            // add list fragment
            if (listViewFragment == null) {
                listViewFragment = new ListViewFragment();
                t2.add(R.id.frameLayoutList, listViewFragment);
            }

            // add detail fragment
            ListDetailFragment listDetailFragment = (ListDetailFragment) getSupportFragmentManager().findFragmentById(R.id.frameLayoutDetail);

            if (listDetailFragment == null) {
                listDetailFragment = new ListDetailFragment();
                t2.add(R.id.frameLayoutDetail, listDetailFragment);
            }

            t2.commit();
        }
        listViewFragment.setOnMasterSelectedListener(new ListViewFragment.onMasterSelectedListener() {
            @Override
            public void onItemSelected(KidActivity kidActivity) {
                sendActivity(kidActivity);
            }
        });
    }

    public void sendActivity (KidActivity kidActivity){
        ListDetailFragment listDetailFragment;

        currentActivityId = kidActivity.getId();
        Bundle bundle = new Bundle();
        bundle.putInt(KidActivity.KEY_ID, currentActivityId);

        // if landscape mode, show beside
        if (landscape) {
            listDetailFragment = (ListDetailFragment) getSupportFragmentManager().findFragmentById(R.id.frameLayoutDetail);
            listDetailFragment.showDetails(bundle);
            findViewById(R.id.frameLayoutDetail).setVisibility(View.VISIBLE);
            findViewById(R.id.floatingActionButton2).setVisibility(View.VISIBLE);
        } else {
            // if portrait, display details in ViewActivity
            Intent intent = new Intent(this, ViewActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE_VIEW);
        }
    }

    public void onClickAdd(View view) {
        // go to input screen
        Intent intent = new Intent(this,InputActivity.class);
        startActivityForResult(intent, REQUEST_CODE_NEW);
    }

    public void onClickNext(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(KidActivity.KEY_ID, currentActivityId);

        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE_VIEW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // new kid activity added
        if (requestCode == REQUEST_CODE_NEW) {
            if (resultCode == RESULT_OK) {
                listViewFragment.updateResult();

                // display message
                Snackbar.make(coordinatorView,
                        "New activity added",
                        Snackbar.LENGTH_LONG).show();
            }
        } // kid activity deleted
        else if (requestCode == REQUEST_CODE_VIEW) {
            if (resultCode == RESULT_OK) {
                listViewFragment.updateResult();

                // display message
                Snackbar.make(coordinatorView,
                        "Activity deleted",
                        Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        // setup search bar
        final MenuItem search = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Search activity");
        EditText searchText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchText.setTextColor(Color.WHITE);
        searchText.setHintTextColor(Color.WHITE);

        // filter list by query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listViewFragment.filterList(newText);
                return false;
            }
        });

        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchView.requestFocus();
                setMenuItemVisible(menu, search, false); // hide other menu items
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // clear filter on exit search
                listViewFragment.filterList("");
                searchView.setQuery("", false);
                searchView.clearFocus();
                setMenuItemVisible(menu, search, true); // restore menu items
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void setMenuItemVisible(Menu menu, MenuItem search, boolean visible) {
        // toggle visibility of menu items
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item != search)
                item.setVisible(visible);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // sort option selected
            case R.id.action_alpha_asc:
                listViewFragment.order = UserSQL.ALPHA_ASC;
                listViewFragment.updateResult();
                item.setChecked(true);
                return true;
            case R.id.action_alpha_desc:
                listViewFragment.order = UserSQL.ALPHA_DESC;
                listViewFragment.updateResult();
                item.setChecked(true);
                return true;
            case R.id.action_date_asc:
                listViewFragment.order = UserSQL.DATE_ASC;
                listViewFragment.updateResult();
                item.setChecked(true);
                return true;
            case R.id.action_date_desc:
                listViewFragment.order = UserSQL.DATE_DESC;
                listViewFragment.updateResult();
                item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
