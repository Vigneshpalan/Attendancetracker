package com.example.attendancetracker;

public class TimetableItem {
    private int id;
    private String day;
    private String period;
    private String subject;
    private String location;
    public TimetableItem(int id, String day, String period, String subject,String location) {
        this.id = id;
        this.day = day;
        this.period = period;
        this.location=location;
        this.subject = subject;
    }


    public int getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public String getPeriod() {
        return period;
    }

    public String getSubject() {
        return subject;
    }

    public String getLocation() {
        return location;
    }
}
