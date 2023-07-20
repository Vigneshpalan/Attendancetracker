package com.example.attendancetracker;

import android.content.DialogInterface;

import android.os.Bundle;
import android.view.View;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SubjectActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SubjectAdapter subjectAdapter;
    private List<Subject> subjectList;
    private DatabaseHelper databaseHelper;
    private Spinner subjectSpinner;
    private FloatingActionButton fabAddSubject;
    private FloatingActionButton fabDeleteSubject;
    String currentUsername;
    Integer u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        Intent intent = getIntent();
        currentUsername = intent.getStringExtra("username");

        if (currentUsername != null) {
            databaseHelper = new DatabaseHelper(this);
            u = databaseHelper.getUserId(currentUsername);
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            // Handle the case when the username is null
            Toast.makeText(this, "Username is null", Toast.LENGTH_SHORT).show();
        }


        subjectList = new ArrayList<>();
        subjectAdapter = new SubjectAdapter(this, subjectList);
        recyclerView.setAdapter(subjectAdapter);

        subjectSpinner = findViewById(R.id.subjectSpinner);
        ArrayAdapter<Subject> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(spinnerAdapter);

        loadSubjects();

        fabAddSubject = findViewById(R.id.fabAddSubject);
        fabAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSubjectDialog();
            }
        });

        fabDeleteSubject = findViewById(R.id.fabDeleteSubject);
        fabDeleteSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedSubject();
            }
        });
    }

    private void loadSubjects() {
        subjectList.clear();
        subjectList.addAll(databaseHelper.getAllSubjects(u));
        subjectAdapter.notifyDataSetChanged();

        ArrayAdapter<Subject> spinnerAdapter = (ArrayAdapter<Subject>) subjectSpinner.getAdapter();
        spinnerAdapter.notifyDataSetChanged();

        if (subjectList.isEmpty()) {
            Toast.makeText(this, "No subjects available", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddSubjectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Subject");

        final EditText editTextSubject = new EditText(this);
        editTextSubject.setHint("Enter subject name");
        builder.setView(editTextSubject);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String subjectName = editTextSubject.getText().toString().trim();

                if (!subjectName.isEmpty()) {

                    boolean success = databaseHelper.addSubject(subjectName,u);

                    if (success) {
                        Toast.makeText(SubjectActivity.this, "Subject added successfully", Toast.LENGTH_SHORT).show();
                        loadSubjects();

                        // Set the spinner selection to the added subject
                        Subject addedSubject = subjectList.get(subjectList.size() - 1);
                        int position = subjectList.indexOf(addedSubject);
                        subjectSpinner.setSelection(position);
                    } else {
                        Toast.makeText(SubjectActivity.this, "Failed to add subject", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SubjectActivity.this, "Please enter subject name", Toast.LENGTH_SHORT).show();
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

    private void deleteSelectedSubject() {
        final Subject selectedSubject = (Subject) subjectSpinner.getSelectedItem();

        if (selectedSubject != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Delete");
            builder.setMessage("Are you sure you want to delete this subject?");

            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean success = databaseHelper.deleteSubject(selectedSubject.getId());

                    if (success) {
                        Toast.makeText(SubjectActivity.this, "Subject deleted successfully", Toast.LENGTH_SHORT).show();
                        loadSubjects();
                    } else {
                        Toast.makeText(SubjectActivity.this, "Failed to delete subject", Toast.LENGTH_SHORT).show();
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
        } else {
            Toast.makeText(SubjectActivity.this, "No subject selected", Toast.LENGTH_SHORT).show();
        }
    }
}

