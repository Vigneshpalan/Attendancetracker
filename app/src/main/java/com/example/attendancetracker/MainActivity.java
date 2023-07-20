package com.example.attendancetracker;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText e3, e4;
    private Button b2, reg;
    private String enteredUsername, enteredPassword;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e3 = findViewById(R.id.ed3);
        e4 = findViewById(R.id.ed4);
        b2 = findViewById(R.id.login);
        reg = findViewById(R.id.reg);

        databaseHelper = new DatabaseHelper(MainActivity.this);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredUsername = e3.getText().toString();
                enteredPassword = e4.getText().toString();

                if (TextUtils.isEmpty(enteredUsername) || TextUtils.isEmpty(enteredPassword)) {
                    Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                    return;
                }

                if (databaseHelper.validateLogin(enteredUsername, enteredPassword)) {


                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.putExtra("username", enteredUsername);
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    startActivity(intent);


                } else {
                    Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
