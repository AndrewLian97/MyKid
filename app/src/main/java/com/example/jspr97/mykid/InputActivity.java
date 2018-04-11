package com.example.jspr97.mykid;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import java.util.Calendar;

public class InputActivity extends AppCompatActivity {

    private EditText activityName, location, date, time, reporter;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

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
    }

    private void displayDateDialog() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        // display date picker popup
        DatePickerDialog dialog = new DatePickerDialog(this,dateSetListener, year, month, day);
        dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        dialog.show();
    }

    private void displayTimeDialog() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        // display time picker popup
        TimePickerDialog dialog = new TimePickerDialog(this, timeSetListener, hour, min, false);
        dialog.show();
    }

    public void onClickSave(View view) {
        // validate input
        boolean check = true;
        String emptyError = getString(R.string.empty_error);
        if (reporter.getText().length() == 0) {
            reporter.requestFocus();
            reporter.setError(emptyError);
            check = false;
        }
        // reporter name alphabets only
        else if (!reporter.getText().toString().matches("[a-zA-Z ]+")) {
            reporter.requestFocus();
            reporter.setError(getString(R.string.invalid_error));
            check = false;
        }

        if (date.getText().length() == 0) {
            date.setError(emptyError);
            check = false;
        }
        if (activityName.getText().length() == 0) {
            activityName.requestFocus();
            activityName.setError(emptyError);
            check = false;
        }

        // send input to ConfirmActivity
        if (check) {
            String activityNameString = activityName.getText().toString();
            String locationString = (location.getText().length() == 0)? "-" : location.getText().toString();
            String dateString = date.getText().toString();
            String timeString = (time.getText().length() == 0)? "-" : time.getText().toString();
            String reporterString = reporter.getText().toString();

            // send bundle and display dialog for confirmation
            Bundle bundle = new Bundle();
            bundle.putString(ConfirmInputDialog.ACTIVITY_NAME, activityNameString);
            bundle.putString(ConfirmInputDialog.LOCATION, locationString);
            bundle.putString(ConfirmInputDialog.DATE, dateString);
            bundle.putString(ConfirmInputDialog.TIME, timeString);
            bundle.putString(ConfirmInputDialog.REPORTER, reporterString);
            showDialog(bundle);
        }
    }

    public void showDialog(Bundle bundle) {
        ConfirmInputDialog dialog = new ConfirmInputDialog();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "confirm");
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
