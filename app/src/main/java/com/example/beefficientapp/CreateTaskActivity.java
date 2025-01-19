package com.example.beefficientapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CreateTaskActivity extends AppCompatActivity {

    private static final int DATE_REQUEST_CODE = 1;
    private static final int TIME_REQUEST_CODE = 2;
    private TextView selectedDate, selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);

        selectedDate = findViewById(R.id.selDate);
        selectedTime = findViewById(R.id.selTime);

        // Load the saved time from SharedPreferences and display it
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String savedTime = preferences.getString("selectedTime", "No Time Set");
        selectedTime.setText(savedTime); // Set the saved time in the TextView

        // Navigate to the calendar
        selectedDate.setOnClickListener(v -> {
            Intent intent = new Intent(CreateTaskActivity.this, CreatedDateActivity.class);
            startActivityForResult(intent, DATE_REQUEST_CODE); // Start activity for result
        });

        // Navigate to the clock
        selectedTime.setOnClickListener(v -> {
            Intent intent = new Intent(CreateTaskActivity.this, CreatedTimeActivity.class);
            startActivityForResult(intent, TIME_REQUEST_CODE); // Start activity for result
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DATE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("selected_date")) {
                // Retrieve the selected date
                String date = data.getStringExtra("selected_date");
                // Update the TextView
                selectedDate.setText(date);
            }
        } else if (requestCode == TIME_REQUEST_CODE && resultCode == RESULT_OK) {
            // Reload the saved time from SharedPreferences
            SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String savedTime = preferences.getString("selectedTime", "No Time Set");
            selectedTime.setText(savedTime); // Update the TextView with the saved time
        }
    }
}
