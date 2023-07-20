package com.example.attendancetracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TimetableActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private EditText subjectEditText;
    private Button addButton, clearButton;
    private int columnCount = 7,currentPeriodIndex=0;


    private List<String> daysList;
    private List<String> periodsList;
    private String[][] timetableMatrix;
    private int currentDayIndex = 0;
    private int currentColumnIndex = 0;
    private String currentUsername;
    private int userId;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        Intent intent = getIntent();
        currentUsername = intent.getStringExtra("username");

        tableLayout = findViewById(R.id.tableLayout);
        subjectEditText = findViewById(R.id.subjectEditText);
        addButton = findViewById(R.id.addButton);
        clearButton = findViewById(R.id.clearb);

        daysList = new ArrayList<>();
        periodsList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);

        if (currentUsername != null) {
            userId = databaseHelper.getUserId(currentUsername);
        } else {
            Toast.makeText(this, "Username is null", Toast.LENGTH_SHORT).show();
        }

        daysList.add("Monday");
        daysList.add("Tuesday");
        daysList.add("Wednesday");
        daysList.add("Thursday");
        daysList.add("Friday");
        daysList.add("Saturday");

        periodsList.add("Period 1");
        periodsList.add("Period 2");
        periodsList.add("Period 3");
        periodsList.add("Period 4");
        periodsList.add("Period 5");
        periodsList.add("Period 6");
        periodsList.add("Period 7");
        periodsList.add("Period 8");
        periodsList.add("Period 9");

        timetableMatrix = new String[periodsList.size()][daysList.size()];

        loadTimetableData();

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTable();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = subjectEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(subject)) {
                    addSubjectToMatrix(subject);
                    updateTable();
                    subjectEditText.setText("");
                } else {
                    Toast.makeText(TimetableActivity.this, "Please enter a subject", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addSubjectToMatrix(String subject) {


        boolean cellOccupied = true;
        while (currentPeriodIndex < periodsList.size()) {
            while (currentDayIndex < daysList.size()) {
                if (timetableMatrix[currentPeriodIndex][currentDayIndex] == null) {
                    timetableMatrix[currentPeriodIndex][currentDayIndex] = subject;

                    String selectedDay = daysList.get(currentDayIndex);
                    String selectedPeriod = periodsList.get(currentPeriodIndex);
                    databaseHelper.insertTimetableData(userId, selectedDay, selectedPeriod, subject);

                    updateTable();

                    // Exit the loop since the subject is added to an available cell
                    return;
                }
                currentDayIndex++;
            }
            currentDayIndex = 0;
            currentPeriodIndex++;
        }

        // If all cells are occupied, display an error message
        Toast.makeText(this, "Timetable is fully occupied", Toast.LENGTH_SHORT).show();
    }



    private void updateTable() {
        tableLayout.removeAllViews(); // Clear the existing table

        // Create the header row
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        // Add empty cell for the top-left corner
        TextView emptyCell = createTableCell("");
        headerRow.addView(emptyCell);

        // Add day cells to the header row
        for (String day : daysList) {
            TextView dayCell = createTableCell(day);
            headerRow.addView(dayCell);
        }

        tableLayout.addView(headerRow);

        // Create rows for each period
        for (int i = 0; i < periodsList.size(); i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            // Add period cell
            TextView periodCell = createTableCell(periodsList.get(i));
            row.addView(periodCell);

            // Add subject cells for each day
            for (int j = 0; j < daysList.size(); j++) {
                String subject = timetableMatrix[i][j];
                TextView subjectCell = createTableCell(subject);
                row.addView(subjectCell);
            }

            tableLayout.addView(row);
        }
    }

    // Create a TextView for a table cell
    private TextView createTableCell(String text) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        textView.setPadding(8, 8, 8, 8);
        textView.setText(text);
        return textView;
    }

    // Load timetable data from the database
    private void loadTimetableData() {
        List<TimetableItem> timetableData = databaseHelper.getTimetableData(userId);

        if (timetableData != null) {
            for (TimetableItem item : timetableData) {
                String day = item.getDay();
                String period = item.getPeriod();
                String subject = item.getSubject();

                int dayIndex = daysList.indexOf(day);
                int periodIndex = periodsList.indexOf(period);

                if (dayIndex >= 0 && periodIndex >= 0) {
                    timetableMatrix[periodIndex][dayIndex] = subject;
                }
            }
        } else {
            Toast.makeText(this, "Failed to load timetable data", Toast.LENGTH_SHORT).show();
        }

        updateTable();
    }

    private void clearTable() {
        // Clear the timetable matrix
        for (int i = 0; i < periodsList.size(); i++) {
            for (int j = 0; j < daysList.size(); j++) {
                timetableMatrix[i][j] = null;
            }
        }

        // Clear the table layout
        tableLayout.removeAllViews();

        // Clear the database
        databaseHelper.clearTimetableData(userId);

        // Show a toast message
        Toast.makeText(this, "Timetable cleared", Toast.LENGTH_SHORT).show();
    }
}
