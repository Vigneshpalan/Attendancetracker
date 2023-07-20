package com.example.attendancetracker;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    private CardView cardAttendance, cardTimeTable, cardReport,  cardSubjects,cardlogout,cardAB;
    public String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();

        currentUsername = intent.getStringExtra("username");

cardAB=findViewById(R.id.l1);

        cardAttendance = findViewById(R.id.a);
        cardTimeTable = findViewById(R.id.tb);
        cardReport = findViewById(R.id.r);

        cardSubjects = findViewById(R.id.s);
        cardlogout=findViewById(R.id.l);

        cardAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateCardClick(cardAttendance);
                Intent intent = new Intent(HomeActivity.this,AttendanceActivity.class);
                intent.putExtra("username", currentUsername);
                startActivity(intent);
            }
        });
        cardlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateCardClick(cardAttendance);

                    showLogoutConfirmationDialog();
            }
        });
        cardAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAboutAppDialog();
            }
        });
        cardReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateCardClick(cardReport);

                Intent intent = new Intent(HomeActivity.this,ReportActivity.class);
                intent.putExtra("username", currentUsername);
                startActivity(intent);
            }
        });

        cardTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateCardClick(cardTimeTable);
                Intent intent = new Intent(HomeActivity.this,TimetableActivity.class);
                intent.putExtra("username", currentUsername);
                startActivity(intent);
            }
        });



        cardSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateCardClick(cardSubjects);
                Intent intent = new Intent(HomeActivity.this, SubjectActivity.class);
                intent.putExtra("username", currentUsername);
                startActivity(intent);
            }
        });
    }
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void logout() {

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        Toast.makeText(HomeActivity.this, "Logout Successful", Toast.LENGTH_LONG).show();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void animateCardClick(CardView cardView) {
        ObjectAnimator scale = ObjectAnimator.ofPropertyValuesHolder(cardView,
                PropertyValuesHolder.ofFloat("scaleX", 0.95f),
                PropertyValuesHolder.ofFloat("scaleY", 0.95f));
        scale.setDuration(100);
        scale.setRepeatCount(1);
        scale.setRepeatMode(ObjectAnimator.REVERSE);
        scale.start();
    }
    private void showAboutAppDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About ATRACKER");

        final TextView textViewAppInfo = new TextView(this);
        textViewAppInfo.setTextColor(Color.parseColor("#FF000000"));
        textViewAppInfo.setText("\n\nThank you for using ATTRACKER! We are excited to have you on board!\n\nATRACKER is a versatile app that helps you to track your attendance  .\n\nIf you have any questions or need assistance, feel free to reach out to our support team.\n\nEnjoy using ATRACKER!.\n\ndeveloped by Vignesh ");
        textViewAppInfo.setPadding(16, 0, 16, 0);
        builder.setView(textViewAppInfo);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
