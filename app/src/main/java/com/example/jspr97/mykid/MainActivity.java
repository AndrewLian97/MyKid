package com.example.jspr97.mykid;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private View coordinatorView;
    private boolean landscape;
    private ListViewFragment listViewFragment;

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

        Bundle bundle = new Bundle();
        bundle.putString("name", kidActivity.getName());
        bundle.putString("location", kidActivity.getLocation());
        bundle.putString("date", kidActivity.getDate());
        bundle.putString("time", kidActivity.getTime());
        bundle.putString("nameofreporter", kidActivity.getReporter());

        // if landscape mode, show beside
        if (landscape) {
            listDetailFragment = (ListDetailFragment) getSupportFragmentManager().findFragmentById(R.id.frameLayoutDetail);
            listDetailFragment.showDetails(bundle);
            findViewById(R.id.frameLayoutDetail).setVisibility(View.VISIBLE);

        } else {
            // display details in another screen
            Intent intent = new Intent(this, ViewActivity.class);
            startActivity(intent, bundle);
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
                listViewFragment.updateResult();

                Snackbar.make(coordinatorView,
                        "New activity added",
                        Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // delete option selected
            case R.id.action_delete:
                // DELETE HERE
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
