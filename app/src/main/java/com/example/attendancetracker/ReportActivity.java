package com.example.attendancetracker;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    private Spinner subjectSpinner;
    private TextView overallPercentageTextView;

    private int userId;
    private DatabaseHelper databaseHelper;

    String currentUsername;

    private BarChart barChart;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        subjectSpinner = findViewById(R.id.subjectSpinner);
        overallPercentageTextView = findViewById(R.id.attendanceEditText);

        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);

        Intent intent = getIntent();
        currentUsername = intent.getStringExtra("username");

        if (currentUsername != null) {
            databaseHelper = new DatabaseHelper(this);
            userId = databaseHelper.getUserId(currentUsername);
        } else {
            Toast.makeText(this, "Username is null", Toast.LENGTH_SHORT).show();
        }


        List<Subject> subjectList = databaseHelper.getAllSubjects(userId);


        List<String> subjectNames = new ArrayList<>();
        for (Subject subject : subjectList) {
            subjectNames.add(subject.getSubjectName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(spinnerAdapter);

        subjectSpinner.setSelection(0);


        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Subject selectedSubject = subjectList.get(position);


                List<Attendance> attendanceList = databaseHelper.getAttendanceList(userId, String.valueOf(selectedSubject.getSubjectId()));

                displayAttendanceData(attendanceList);


            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected
            }
        });
    }
    private void displayAttendanceData(List<Attendance> attendanceList) {

        int presentCount = 0;
        int absentCount = 0;
        int medicalLeaveCount = 0;

        for (Attendance attendance : attendanceList) {
            switch (attendance.getStatus()) {
                case "Present":
                    presentCount++;
                    break;
                case "Absent":
                    absentCount++;
                    break;
                case "Medical Leave":
                    medicalLeaveCount++;
                    break;
            }
        }

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> barLabels = new ArrayList<>();

        barEntries.add(new BarEntry(0, presentCount));
        barEntries.add(new BarEntry(1, absentCount));
        barEntries.add(new BarEntry(2, medicalLeaveCount));

        barLabels.add("Present");
        barLabels.add("Absent");
        barLabels.add("Medical Leave");

        BarDataSet barDataSet = new BarDataSet(barEntries, "(P/A/M)");
        barDataSet.setColors(Color.GREEN, Color.RED, Color.YELLOW);

        BarData barData = new BarData(barDataSet);

        // Set up bar chart
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<Integer> pieColors = new ArrayList<>();
        ArrayList<String> pieLabels = new ArrayList<>();

        float presentPercentage = (float) (presentCount +medicalLeaveCount )/ (presentCount+medicalLeaveCount+absentCount) * 100;

        if (presentPercentage<= 75) {
            Toast.makeText(ReportActivity.this, "Your overall attendance is less than 75%", Toast.LENGTH_SHORT).show();
        }
        // Set overall attendance text to present percentage
        overallPercentageTextView.setText("Overall Attendance: " + presentPercentage + "%");
        pieEntries.add(new PieEntry(presentPercentage, "Present"));
        pieEntries.add(new PieEntry(absentCount, "Absent"));
        pieEntries.add(new PieEntry(medicalLeaveCount, "Medical Leave"));

        pieColors.add(Color.GREEN);
        pieColors.add(Color.RED);
        pieColors.add(Color.YELLOW);

        pieLabels.add("Present");
        pieLabels.add("Absent");
        pieLabels.add("Medical Leave");

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Attendance");
        pieDataSet.setColors(pieColors);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.WHITE);


        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelTextSize(11f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setDrawEntryLabels(true);


        pieChart.setCenterTextSize(16f);



        pieChart.setUsePercentValues(true);
        pieChart.animateY(1000);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setEntryLabelTextSize(10f);


        pieChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
        pieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        pieChart.getLegend().setDrawInside(false);
        pieChart.getLegend().setEnabled(true);
        pieChart.getLegend().setTextSize(11f);
        pieChart.getLegend().setTextColor(Color.BLACK);
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.getLegend().setForm(Legend.LegendForm.CIRCLE);
        pieChart.getLegend().setFormSize(12f);
        pieChart.getLegend().setXEntrySpace(18f);
        pieChart.getLegend().setYEntrySpace(18f);
        pieChart.getLegend().setFormToTextSpace(10f);
        pieChart.setRotationEnabled(true);
        pieChart.setRotationAngle(90);
        pieChart.invalidate();
        pieChart.setRotationEnabled(true);
        pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setEntryLabelColor(Color.BLACK);

        pieChart.invalidate();
    }





}
