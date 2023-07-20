package com.example.attendancetracker;

import android.os.Parcel;
import android.os.Parcelable;

public class Subject implements Parcelable {
    private int subjectId;
    private String subjectName;

    public Subject(int subjectId, String subjectName) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }

     public int getId() {
        return subjectId;
    }
    protected Subject(Parcel in) {
        subjectId = in.readInt();
        subjectName = in.readString();
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    public int getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    @Override
    public String toString() {
        return subjectName; // Return the subject name for display in the spinner
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(subjectId);
        dest.writeString(subjectName);
    }
}

