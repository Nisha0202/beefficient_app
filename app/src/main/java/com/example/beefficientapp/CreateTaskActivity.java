package com.example.beefficientapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
        taskButton.setOnClickListener(v -> {
            String taskTitle = title.getText().toString().trim();
            if (!taskTitle.isEmpty()) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("taskname", taskTitle);
                editor.apply();
                Toast.makeText(CreateTaskActivity.this, "Task Saved: " + taskTitle, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreateTaskActivity.this, "Please enter a task title", Toast.LENGTH_SHORT).show();
            }
        });

        boolean isNotificationEnabled = preferences.getBoolean(NOTIFICATION_TOGGLE_KEY, false);
        notificationToggle.setChecked(isNotificationEnabled);
        notificationToggle.setTrackTintList(ColorStateList.valueOf(isNotificationEnabled ? Color.GREEN : Color.GRAY));

        notificationToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            notificationToggle.setTrackTintList(ColorStateList.valueOf(isChecked ? Color.GREEN : Color.GRAY));
            Toast.makeText(CreateTaskActivity.this, isChecked ? "Notifications ON" : "Notifications OFF", Toast.LENGTH_LONG).show();
            editor.putBoolean(NOTIFICATION_TOGGLE_KEY, isChecked);
            editor.apply();
        });

        selectedDate.setOnClickListener(v -> {
            Intent intent = new Intent(CreateTaskActivity.this, CreatedDateActivity.class);
            startActivityForResult(intent, DATE_REQUEST_CODE);
        });

        selectedTime.setOnClickListener(v -> {
            Intent intent = new Intent(CreateTaskActivity.this, CreatedTimeActivity.class);
            startActivityForResult(intent, TIME_REQUEST_CODE);
        });

        selectedTags.setOnClickListener(v -> {
            Intent intent = new Intent(CreateTaskActivity.this, CreatedTags.class);
            startActivityForResult(intent, TAG_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DATE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.hasExtra("selected_date")) {
            String date = data.getStringExtra("selected_date");
            selectedDate.setText(date);
        } else if (requestCode == TIME_REQUEST_CODE && resultCode == RESULT_OK) {
            SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String savedTime = preferences.getString("selectedTime", "No Time Set");
            selectedTime.setText(savedTime);
        } else if (requestCode == TAG_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.hasExtra("selected_tag")) {
            String tag = data.getStringExtra("selected_tag");
            selectedTags.setText(tag);
        }
    }

    public void migrateDataToSQLite(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        String username = preferences.getString("username", "User");
        String date = preferences.getString("selected_date", "22/1/2025");
        String time = preferences.getString("selectedTime", "10:30");
        String taskName = preferences.getString("taskname", "New Task");
        boolean alarmStatus = preferences.getBoolean("notificationToggle", false);
        String tags = preferences.getString("selectedTag", "Presentation");

        DatabaseHelper dbHelper = new DatabaseHelper(context);

        boolean success = dbHelper.insertData(username, date, time, taskName, alarmStatus, tags);
        if (success) {
            Toast.makeText(context, "Task created successfully!", Toast.LENGTH_LONG).show();

            SharedPreferences.Editor editor = preferences.edit();
            Map<String, ?> allEntries = preferences.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                if (!entry.getKey().equals("username") && !entry.getKey().equals("isLoggedIn")) {
                    editor.remove(entry.getKey());
                }
            }
            editor.apply();

            if (alarmStatus) {
                setAlarm(context, date, time, taskName);
            }

        } else {
            Toast.makeText(context, "Task creation failed.", Toast.LENGTH_LONG).show();
        }
    }

    private void setAlarm(Context context, String date, String time, String taskName) {
        try {
            String dateTimeString = date + " " + time;
            SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy HH:mm", Locale.getDefault());
            Log.d("Alarm", "Parsing date: " + dateTimeString);
            Date alarmDate = dateFormat.parse(dateTimeString);
            Log.d("Alarm", "Parsed date: " + alarmDate);

            if (alarmDate != null) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(context, AlarmReceiver.class);
                intent.putExtra("taskName", taskName);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, taskName.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (alarmManager != null) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmDate.getTime(), pendingIntent);
                        Log.d("Alarm", "Alarm set for: " + alarmDate);
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmDate.getTime(), pendingIntent);
                        Log.d("Alarm", "Alarm set for: " + alarmDate);
                    }
                }
            }
        } catch (ParseException e) {
            Log.e("Alarm", "ParseException: " + e.getMessage());
            e.printStackTrace();
        }
    }
}