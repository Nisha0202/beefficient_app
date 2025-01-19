package com.example.beefficientapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreatedDateActivity extends AppCompatActivity {  // Ensure this class extends AppCompatActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.created_date);

        ImageView backButton = findViewById(R.id.backButton);

        // Set a click listener for the back button
        backButton.setOnClickListener(v -> finish());

        CalendarView calendarView = findViewById(R.id.calendarView);

        // Initialize SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Set the listener for date changes
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            editor.putString("selected_date", selectedDate);
            editor.apply(); // Save the date
            Toast.makeText(CreatedDateActivity.this, "Date saved: " + selectedDate, Toast.LENGTH_SHORT).show();


            // Pass the selected date back to the previous activity
            Intent intent = new Intent();
            intent.putExtra("selected_date", selectedDate);
            setResult(RESULT_OK, intent); // Set result for the previous activity

            // Navigate to the previous page
            finish();
        });
    }
}
