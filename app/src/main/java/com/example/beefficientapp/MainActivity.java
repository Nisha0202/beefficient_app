package com.example.beefficientapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;



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
        loadTasks(this);
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
        todayTextView.setTextColor(Color.parseColor("#232323"));
    }

    private void loadTasks(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        // Fetch data from SQLite
        List<Task> tasks = dbHelper.getAllTasks(); // Assumes you have a method in DatabaseHelper
        LinearLayout taskContainer = findViewById(R.id.taskContainer);

        // Clear previous views
        taskContainer.removeAllViews();

        // Populate the layout dynamically
        for (Task task : tasks) {
            // Create a View for each task
            View taskView = getLayoutInflater().inflate(R.layout.task_item, null);

            TextView taskName = taskView.findViewById(R.id.taskName);
            TextView taskDate = taskView.findViewById(R.id.taskDate);
            TextView taskTime = taskView.findViewById(R.id.taskTime);
            TextView taskTags = taskView.findViewById(R.id.taskTags);

            // Set task data
            taskName.setText(task.getTaskName());
            taskDate.setText(task.getDate());
            taskTime.setText(task.getTime());
            taskTags.setText(task.getTags());

            // Add to container
            taskContainer.addView(taskView);
        }
    }

}
