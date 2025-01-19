package com.example.beefficientapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreatedTimeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_time);

        TimePicker timePicker = findViewById(R.id.timePicker);
        Button btnSetTime = findViewById(R.id.btnSetTime);
        ImageView backButton = findViewById(R.id.backButton);

        // Back button functionality
        backButton.setOnClickListener(v -> finish());

        // Set OnClickListener for the Set button
        btnSetTime.setOnClickListener(v -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            @SuppressLint("DefaultLocale") String selectedTime = String.format("%02d:%02d", hour, minute);

            // Save selected time in SharedPreferences
            saveSelectedTime(selectedTime);

            // Return the selected time to the calling activity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_time", selectedTime);
            setResult(RESULT_OK, resultIntent);

            Toast.makeText(this, "Time Set: " + selectedTime, Toast.LENGTH_SHORT).show();

            // Close the activity
            finish();
        });
    }

    private void saveSelectedTime(String time) {
        getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                .edit()
                .putString("selectedTime", time)
                .apply();
    }
}
