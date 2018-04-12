package com.example.jspr97.mykid;

public class KidActivity {
    // labels for table and columns
    public static final String TABLE_NAME = "KidActivities";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_REPORTER = "reporter";

    private int id;
    private String name;
    private String location;
    private String date;
    private String time;
    private String reporter;

    public KidActivity() {}

    public KidActivity(String name, String location, String date, String time, String reporter) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.time = time;
        this.reporter = reporter;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public String getLocation() { return location; }

    public String getDate() { return date; }

    public String getTime() { return time; }

    public String getReporter() { return reporter; }

    public void setId(int id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setLocation(String location) { this.location = location; }

    public void setDate(String date) { this.date = date; }

    public void setTime(String time) { this.time = time; }

    public void setReporter(String reporter) { this.reporter = reporter; }
}
