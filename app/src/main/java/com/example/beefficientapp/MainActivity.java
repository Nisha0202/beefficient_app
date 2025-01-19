package com.example.beefficientapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the floating action button by its ID
        Button fab = findViewById(R.id.fab);

        // Set a click listener on the button
        fab.setOnClickListener(v -> {
            // Navigate to CreateActivity
            Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
            startActivity(intent);
        });

        // Highlight today's day
        highlightToday();
    }

    private void highlightToday() {
        // Array of TextView IDs for days of the week
        int[] dayIds = {
                R.id.day_sun,
                R.id.day_mon,
                R.id.day_tue,
                R.id.day_wed,
                R.id.day_thu,
                R.id.day_fri,
                R.id.day_sat
        };

        // Get the current day of the week (1 = Sunday, 2 = Monday, ..., 7 = Saturday)
        int todayIndex = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;

        // Get the TextView for today's day and set it to bold
        TextView todayTextView = findViewById(dayIds[todayIndex]);
        todayTextView.setTypeface(todayTextView.getTypeface(), Typeface.BOLD);
    }
}
