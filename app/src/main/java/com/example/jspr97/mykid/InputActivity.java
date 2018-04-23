package com.example.jspr97.mykid;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import java.util.Calendar;

public class InputActivity extends AppCompatActivity {

    private EditText activityName, location, date, time, reporter;
    private String activityNameString, locationString, dateString, timeString, reporterString;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private String callingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        // check if current session is to create or update activity
        callingActivity = getCallingActivity().getClassName();

        // setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar3);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        if (callingActivity.equals(MainActivity.class.getName()))
            getSupportActionBar().setTitle("New Activity");
        else if (callingActivity.equals(ViewActivity.class.getName()))
            getSupportActionBar().setTitle("Edit Details");

        // set references
        activityName = findViewById(R.id.activityName);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        reporter = findViewById(R.id.reporter);

        // popup dialog when field is focused or clicked
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDateDialog();
            }
        });
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (focused)
                    displayDateDialog();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                date.setText(String.format("%02d/%02d/%d", d, m+1, y));
                date.setError(null);
            }
        };
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayTimeDialog();
            }
        });
        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (focused)
                    displayTimeDialog();
            }
        });
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                // change to 12 hour format
                String period;
                if (hour < 12 || hour == 0)
                    period = "AM";
                else
                    period = "PM";
                if (hour == 0 || hour == 12)
                    hour = 12;
                else
                    hour %= 12;
                time.setText(String.format("%02d:%02d %s", hour, min, period));
            }
        };

        // if editing mode, retrieve activity details
        if (callingActivity.equals(ViewActivity.class.getName())) {
            int id = getIntent().getIntExtra(KidActivity.KEY_ID, 0);
            KidActivity kidActivity = new UserSQL(this).getKidActivity(id);
            activityNameString = kidActivity.getName();
            locationString = kidActivity.getLocation();
            dateString = kidActivity.getDate();
            timeString = kidActivity.getTime();
            reporterString = kidActivity.getReporter();
        }

        // show existing activity details in text box for editing
        activityName.setText(activityNameString);
        location.setText(locationString);
        date.setText(dateString);
        time.setText(timeString);
        reporter.setText(reporterString);
    }

    private void displayDateDialog() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        // display date picker popup
        DatePickerDialog dialog = new DatePickerDialog(this,dateSetListener, year, month, day);
        dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        if (!dialog.isShowing())
            dialog.show();
    }

    private void displayTimeDialog() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        // display time picker popup
        TimePickerDialog dialog = new TimePickerDialog(this, timeSetListener, hour, min, false);
        if (!dialog.isShowing())
            dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (activityName.getText().length() > 0 ||
                location.getText().length() > 0 ||
                date.getText().length() > 0 ||
                time.getText().length() > 0 ||
                reporter.getText().length() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Discard changes?")
                    .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.input_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                // remove extra whitespaces
                activityNameString = activityName.getText().toString().trim();
                locationString = (location.getText().length() == 0)? "-" : location.getText().toString().trim();
                dateString = date.getText().toString();
                timeString = (time.getText().length() == 0)? "-" : time.getText().toString();
                reporterString = reporter.getText().toString().trim();

                // validate input
                boolean check = true;
                String emptyError = getString(R.string.empty_error);

                if (reporterString.length() == 0) {
                    reporter.requestFocus();
                    reporter.setError(emptyError);
                    check = false;
                }
                // reporter name alphabets only
                else if (!reporterString.matches("[a-zA-Z ]+")) {
                    reporter.requestFocus();
                    reporter.setError(getString(R.string.invalid_error));
                    check = false;
                }

                if (dateString.length() == 0) {
                    date.setError(emptyError);
                    check = false;
                }
                if (activityNameString.length() == 0) {
                    activityName.requestFocus();
                    activityName.setError(emptyError);
                    check = false;
                }

                // send input to confirm dialog
                if (check) {
                    Bundle bundle = new Bundle();
                    bundle.putString(KidActivity.KEY_NAME, activityNameString);
                    bundle.putString(KidActivity.KEY_LOCATION, locationString);
                    bundle.putString(KidActivity.KEY_DATE, dateString);
                    bundle.putString(KidActivity.KEY_TIME, timeString);
                    bundle.putString(KidActivity.KEY_REPORTER, reporterString);

                    ConfirmInputDialog dialog = new ConfirmInputDialog();
                    dialog.setArguments(bundle);
                    dialog.setOnClickDialogListener(new ConfirmInputDialog.onClickDialogListener() {
                        @Override
                        public void onClickPositive() {
                            KidActivity newActivity = new KidActivity(activityNameString,
                                    locationString,
                                    dateString,
                                    timeString,
                                    reporterString);
                            if (callingActivity.equals(MainActivity.class.getName()))
                                addActivity(newActivity);
                            else if (callingActivity.equals(ViewActivity.class.getName()))
                                updateActivity(newActivity);

                            // return to previous activity
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                    dialog.show(getSupportFragmentManager(), "confirm");
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addActivity(KidActivity kidActivity) {
        // insert to database
        UserSQL db = new UserSQL(this);
        db.insert(kidActivity);
    }

    private void updateActivity(KidActivity kidActivity) {
        // update activity in database
        int id = getIntent().getIntExtra(KidActivity.KEY_ID, 0);
        kidActivity.setId(id);
        UserSQL db = new UserSQL(this);
        db.update(kidActivity);
    }
}
