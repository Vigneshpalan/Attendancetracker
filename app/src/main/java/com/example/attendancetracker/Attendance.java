package com.example.attendancetracker;

public class Attendance {
    private int subjectId;
    private String date;
    private String status;

    public Attendance(int subjectId, String date, String status) {
        this.subjectId = subjectId;
        this.date = date;
        this.status = status;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}

