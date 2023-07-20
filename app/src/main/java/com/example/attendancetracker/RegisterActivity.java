package com.example.attendancetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText e1, e2;
    private Button b1;
    private String enteredUsername, enteredPassword;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        e1 = findViewById(R.id.ed1);
        e2 = findViewById(R.id.ed2);
        b1 = findViewById(R.id.b1);

        databaseHelper = new DatabaseHelper(RegisterActivity.this);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredUsername = e1.getText().toString();
                enteredPassword = e2.getText().toString();
                if(databaseHelper.validateLogin(enteredUsername,enteredPassword)){
                    Toast.makeText(RegisterActivity.this,"User Account Exists",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(enteredUsername)) {
                    Toast.makeText(RegisterActivity.this, "Enter student ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isStudentIdValid(enteredUsername)) {
                    Toast.makeText(RegisterActivity.this, "Enter a valid student ID in the format  Example(4MT20AI060)", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isPasswordValid(enteredPassword)) {
                    Toast.makeText(RegisterActivity.this, "Invalid password. Password should have a total of 8 characters including numbers (1-9), alphabets, and at least one uppercase letter.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(enteredPassword)) {
                    Toast.makeText(RegisterActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (databaseHelper.addUser(enteredUsername, enteredPassword)) {
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("username", enteredUsername);
                    intent.putExtra("password", enteredPassword);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isStudentIdValid(String studentId) {
        // Check if the student ID has a total of 11 characters
        if (studentId.length() != 10) {
            return false;
        }

        // Check if the student ID starts with "4MT"
        if (!studentId.startsWith("4MT")) {
            return false;
        }

        // Check if the next 2 characters are numbers (dummy value: "20")
        if (!studentId.substring(3, 5).equals("20")) {
            return false;
        }

        // Check if the next 2 characters are alphabets (dummy value: "AI")
        String alphabets = studentId.substring(5, 7);
        if (!alphabets.matches("[A-Z]+")) {
            return false;
        }

        // Check if the last 3 characters are numbers (dummy value: "060")
        if (!studentId.substring(8).matches("\\d+")) {
            return false;
        }

        return true;
    }




    private boolean isPasswordValid(String password) {
        // Check if the password has a total of 8 characters
        if (password.length() != 8) {
            return false;
        }

        // Check if the password contains at least one uppercase letter
        boolean hasUppercase = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
                break;
            }
        }
        if (!hasUppercase) {
            return false;
        }

        // Check if the password contains numbers (1-9) and alphabets
        boolean hasNumber = false;
        boolean hasLetter = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isDigit(c) && c >= '1' && c <= '9') {
                hasNumber = true;
            } else if (Character.isLetter(c)) {
                hasLetter = true;
            }
        }
        if (!hasNumber || !hasLetter) {
            return false;
        }

        return true;
    }




}

