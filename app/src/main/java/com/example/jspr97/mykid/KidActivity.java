package com.example.jspr97.mykid;

public class KidActivity {
    private String name;
    private String location;
    private String date;
    private String time;
    private String reporter;

    public KidActivity(String name, String location, String date, String time, String reporter) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.time = time;
        this.reporter = reporter;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getReporter() {
        return reporter;
    }
}
