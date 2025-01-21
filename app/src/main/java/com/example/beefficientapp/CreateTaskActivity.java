package com.example.beefficientapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.beefficientapp.DatabaseHelper;

import java.util.Map;


public class CreateTaskActivity extends AppCompatActivity {


    private static final int DATE_REQUEST_CODE = 1;
    private static final int TIME_REQUEST_CODE = 2;
    private static final int TAG_REQUEST_CODE = 3;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String NOTIFICATION_TOGGLE_KEY = "notificationToggle";

    private TextView selectedDate, selectedTime, selectedTags;
    private Switch notificationToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);

        ImageView backButton = findViewById(R.id.backButton);

        // Back button functionality
        backButton.setOnClickListener(v -> finish());

        Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(v -> {
            migrateDataToSQLite(CreateTaskActivity.this);
            // Navigate to MainTaskActivity
            Intent intent = new Intent(CreateTaskActivity.this, MainActivity.class);
            startActivity(intent);
            // Finish the current activity
            finish();
        });

        selectedDate = findViewById(R.id.selDate);
        selectedTime = findViewById(R.id.selTime);
        selectedTags = findViewById(R.id.selTags);
        notificationToggle = findViewById(R.id.notification_toggle);

        // Load the saved time from SharedPreferences and display it
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String savedTime = preferences.getString("selectedTime", "No Time Set");
        selectedTime.setText(savedTime);


        EditText title = findViewById(R.id.title);

// Load saved task name and set it to EditText
        String savedTaskName = preferences.getString("taskname", "New Task");



        title.setText(savedTaskName);
        Button taskButton = findViewById(R.id.taskButton);

        // Set click listener for the task button
        taskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the title EditText
                String taskTitle = title.getText().toString().trim();

                if (!taskTitle.isEmpty()) {
                    // Save the updated text only when focus is lost
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("taskname", taskTitle);
                    editor.apply();

                    Toast.makeText(CreateTaskActivity.this, "Task Saved: " + taskTitle, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateTaskActivity.this, "Please enter a task title", Toast.LENGTH_SHORT).show();
                }
            }
        });





// Load the saved toggle state from SharedPreferences
        boolean isNotificationEnabled = preferences.getBoolean(NOTIFICATION_TOGGLE_KEY, false);
        notificationToggle.setChecked(isNotificationEnabled);

// Set the initial track color based on the saved state
        if (isNotificationEnabled) {
            notificationToggle.setTrackTintList(ColorStateList.valueOf(Color.GREEN));
        } else {
            notificationToggle.setTrackTintList(ColorStateList.valueOf(Color.GRAY));
        }

// Listener for the toggle switch
        notificationToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the toggle state in SharedPreferences
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(NOTIFICATION_TOGGLE_KEY, isChecked);
            editor.apply();

            // Change track color based on toggle state
            if (isChecked) {
                notificationToggle.setTrackTintList(ColorStateList.valueOf(Color.GREEN));
                Toast.makeText(CreateTaskActivity.this, "Notifications ON", Toast.LENGTH_LONG).show();
            } else {
                notificationToggle.setTrackTintList(ColorStateList.valueOf(Color.GRAY));
                Toast.makeText(CreateTaskActivity.this, "Notifications OFF", Toast.LENGTH_LONG).show();
            }
        });



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

        // Navigate to the tags
        selectedTags.setOnClickListener(v -> {
            Intent intent = new Intent(CreateTaskActivity.this, CreatedTags.class);
            startActivityForResult(intent, TAG_REQUEST_CODE);
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
        } else if (requestCode == TAG_REQUEST_CODE && resultCode == RESULT_OK) { // Updated to TAG_REQUEST_CODE
            if (data != null && data.hasExtra("selected_tag")) {
                // Retrieve the selected tag
                String tag = data.getStringExtra("selected_tag");
                // Update the TextView
                selectedTags.setText(tag);
            }
        }
    }


    public void migrateDataToSQLite(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Retrieve data
        String username = preferences.getString("username", "User");
        String date = preferences.getString("selected_date", "22/1/2025");
        String time = preferences.getString("selectedTime", "10.30");
        String taskName = preferences.getString("taskname", "New Task");
        boolean alarmStatus = preferences.getBoolean("notificationToggle", false);
        String tags = preferences.getString("selectedTag", "Presentation");

        // Initialize SQLite helper
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        // Save data to SQLite
        boolean success = dbHelper.insertData(username, date, time, taskName, alarmStatus, tags);
        if (success) {
            // Show success message
            Toast.makeText(context, "Task created successfully!", Toast.LENGTH_LONG).show();

            // Clear all SharedPreferences except username
            SharedPreferences.Editor editor = preferences.edit();
            Map<String, ?> allEntries = preferences.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                if (!entry.getKey().equals("username") && !entry.getKey().equals("isLoggedIn")) {
                    editor.remove(entry.getKey());
                }
            }
            editor.apply(); // Apply changes
        } else {
            Toast.makeText(context, "Task creation failed.", Toast.LENGTH_LONG).show();
        }
    }



}
