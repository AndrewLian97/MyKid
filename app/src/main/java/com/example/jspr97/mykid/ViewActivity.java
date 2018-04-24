package com.example.jspr97.mykid;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CustomPagerAdapter adapter;
    private int activity_id;
    private boolean updated = false;
    private ListDetailFragment detailFragment;
    private GalleryFragment galleryFragment;
    private File imageFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        activity_id = getIntent().getExtras().getInt(KidActivity.KEY_ID);

        // setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Activity Details");

        // setup viewpager and tab layout
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        adapter = new CustomPagerAdapter(getSupportFragmentManager());

        // show activity details in fragment
        detailFragment = new ListDetailFragment();
        detailFragment.setArguments(getIntent().getExtras());

        galleryFragment = new GalleryFragment();
        galleryFragment.setArguments(getIntent().getExtras());

        adapter.addFragment(detailFragment, "details");
        adapter.addFragment(galleryFragment, "media");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_add_image:
                // add images to activity
                takePhoto();
                return true;
            case R.id.action_edit:
                // go to edit screen
                Intent intent = new Intent(this, InputActivity.class);
                intent.putExtra(KidActivity.KEY_ID, activity_id);
                startActivityForResult(intent, REQUEST_CODE);
                return true;
            case R.id.action_delete:
                // delete current kid activity
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteActivity();
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
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // activity updated
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                updated = true;
                Bundle bundle = new Bundle();
                bundle.putInt(KidActivity.KEY_ID, activity_id);
                ListDetailFragment detailFragment = (ListDetailFragment) adapter.getItem(0);
                detailFragment.showDetails(bundle);
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                UserSQL db = new UserSQL(this);
                KidActivity kidActivity = db.getKidActivity(activity_id);
                kidActivity.setImagePath(imageFile.getAbsolutePath());
                db.update(kidActivity);
                galleryFragment.update();
            }
        }
    }

    private void deleteActivity() {
        // delete current activity and return to MainActivity
        UserSQL db = new UserSQL(this);
        db.delete(activity_id);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (updated)
            setResult(RESULT_CANCELED);
        finish();
    }

    private void takePhoto() {
        // access camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            imageFile = null;
            try {
                imageFile = createImageFile();
            } catch (IOException ex) {
                Log.i("FILE", "Error creating file");
            }
            // if the file is successfully created
            if (imageFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // create unique file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg", storageDir);

        return image;
    }
}
