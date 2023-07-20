package com.example.attendancetracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceActivity extends AppCompatActivity {
    private Subject selectedSubject;
    private TextView subjectTextView;
    private CalendarView calendarView;
    private TextView attendanceStatusTextView;
    private DatabaseHelper databaseHelper;
    private Spinner subjectSpinner;
    private List<Subject> subjectList;

    String currentUsername;
    Integer u;
    private static final int REQUEST_CODE_ADD_SUBJECT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        Intent intent = getIntent();
        currentUsername = intent.getStringExtra("username");

        if (currentUsername != null) {
            databaseHelper = new DatabaseHelper(this);
            u = databaseHelper.getUserId(currentUsername);
        } else {
            Toast.makeText(this, "Username is null", Toast.LENGTH_SHORT).show();
        }

        subjectSpinner = findViewById(R.id.subjectSpinner);
        subjectTextView = findViewById(R.id.subjectTextView);
        calendarView = findViewById(R.id.calendarView);
        attendanceStatusTextView = findViewById(R.id.attendanceStatusTextView);

        subjectList = new ArrayList<>();
        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(adapter);

        setSubjectList(databaseHelper.getAllSubjects(u));




        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubject = (Subject) parent.getItemAtPosition(position);
                subjectTextView.setText(selectedSubject.getSubjectName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSubject = null;
                subjectTextView.setText("");
            }
        });

        if (getIntent().hasExtra("subject")) {
            selectedSubject = getIntent().getParcelableExtra("subject");
            subjectTextView.setText(selectedSubject.getSubjectName());
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, (month + 1), year);
                if (selectedSubject != null) {
                    showAttendanceDialog(selectedDate);
                } else {
                    Toast.makeText(AttendanceActivity.this, "Selected subject is null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setSubjectList(List<Subject> subjects) {
        if (subjectList == null) {
            subjectList = new ArrayList<>();
        } else {
            subjectList.clear();
        }
        subjectList.addAll(subjects);

        ArrayAdapter<Subject> adapter = (ArrayAdapter<Subject>) subjectSpinner.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void showAttendanceDialog(final String date) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attendance for " + date);

        String[] attendanceOptions = {"Present", "Absent", "Medical Leave"};
        builder.setItems(attendanceOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String attendanceStatus = attendanceOptions[which];
                boolean success = databaseHelper.addAttendance(String.valueOf(selectedSubject.getSubjectId()), date, attendanceStatus, u);
                if (success) {
                    Toast.makeText(AttendanceActivity.this,"Attendance added successfully", Toast.LENGTH_SHORT).show();
                    updateAttendanceStatus(date);
                } else {
                    Toast.makeText(AttendanceActivity.this, "Failed to add attendance", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateAttendanceStatus(String date) {
        String attendanceStatus = databaseHelper.getAttendanceStatus(selectedSubject.getSubjectId(), date, String.valueOf(u));
        attendanceStatusTextView.setText("Attendance status for " + date + ": " + attendanceStatus);
    }




    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_SUBJECT && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            Subject addedSubject = data.getParcelableExtra("addedSubject");
            if (addedSubject != null) {
                int position = subjectList.indexOf(addedSubject);
                subjectSpinner.setSelection(position);

                String currentDate = getCurrentDate();
                showAttendanceDialog(currentDate);
            }
        }
    }
}
