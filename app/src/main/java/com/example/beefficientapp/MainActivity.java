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
import android.widget.ScrollView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LinearLayout taskContainer;
    private LinearLayout noDataImageView; // Declare the ImageView
    private ScrollView scroll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        taskContainer = findViewById(R.id.taskContainer);
        noDataImageView = findViewById(R.id.noDataImageView);
        scroll = findViewById(R.id.scroll);


        // Get the floating action button by its ID
        Button fab = findViewById(R.id.fab);

        // Set a click listener on the button
        fab.setOnClickListener(v -> {
            // Navigate to CreateActivity
            Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
            startActivity(intent);
        });

        // Highlight today's day and load today's tasks
        highlightToday();
        loadTasksForToday();

//        // Set up day click listeners
//        setupDayClickListeners();
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

        Button dateMon = findViewById(R.id.date_mon);
        dateMon.setOnClickListener(v -> {
            String selectedDate = "20/1/2025"; // Replace with dynamically computed date
            loadTasksByDate(MainActivity.this, selectedDate);
        });
        Button dateTue = findViewById(R.id.date_tue);
        dateTue.setOnClickListener(v -> {
            String selectedDate = "21/1/2025"; // Replace with dynamically computed date
            loadTasksByDate(MainActivity.this, selectedDate);
        });
        Button dateWed = findViewById(R.id.date_wed);
        dateWed.setOnClickListener(v -> {
         String selectedDate = "22/1/2025"; // Replace with dynamically computed date
//            loadTasksForToday();
            loadTasksByDate(MainActivity.this, selectedDate);
        });
        Button dateThu = findViewById(R.id.date_thu);
        dateThu.setOnClickListener(v -> {
            String selectedDate = "23/1/2025"; // Replace with dynamically computed date
            loadTasksByDate(MainActivity.this, selectedDate);
        });
        Button dateFri = findViewById(R.id.date_fri);
        dateFri.setOnClickListener(v -> {
            String selectedDate = "24/1/2025"; // Replace with dynamically computed date
            loadTasksByDate(MainActivity.this, selectedDate);
        });
        Button dateSat = findViewById(R.id.date_sat);
        dateSat.setOnClickListener(v -> {
            String selectedDate = "25/1/2025"; // Replace with dynamically computed date
            loadTasksByDate(MainActivity.this, selectedDate);
        });

    }

    private void loadTasksForToday() {
        // Get today's date in the format "yyyy-MM-dd"
        String todayDate = new SimpleDateFormat("d/M/yyyy", Locale.getDefault())
                .format(Calendar.getInstance().getTime());

        loadTasksByDate(this, todayDate);
    }

    private void loadTasksByDate(Context context, String date) {
        // Fetch data from SQLite
        List<Task> tasks = dbHelper.getTasksByDate(date);
        taskContainer.removeAllViews();

        // Check if loading today's tasks
        String todayDate = new SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());

        if (date.equals(todayDate)) {
            // Add a title for "Today's Task"
            TextView titleTextView = new TextView(context);
            titleTextView.setText("Today's Task");
            titleTextView.setTextSize(22);
            titleTextView.setTypeface(null, Typeface.BOLD);
            titleTextView.setTextColor(Color.parseColor("#000000"));
            titleTextView.setPadding(0, 0, 16, 16);

            // Add the title at the top of the container
            taskContainer.addView(titleTextView);
        }

        // Check if tasks are empty
        if (tasks.isEmpty()) {
            // Show the no data image and hide the task container
            noDataImageView.setVisibility(View.VISIBLE);
            taskContainer.setVisibility(View.GONE);
            scroll.setVisibility(View.GONE);
        } else {
            // Hide the no data image and show the task container
            noDataImageView.setVisibility(View.GONE);
            taskContainer.setVisibility(View.VISIBLE);
            scroll.setVisibility(View.VISIBLE);
            // Populate the layout dynamically
            for (Task task : tasks) {
                // Create a View for each task
                View taskView = getLayoutInflater().inflate(R.layout.task_item, null);
                TextView taskName = taskView.findViewById(R.id.taskName);
                TextView taskTime = taskView.findViewById(R.id.taskTime);
                TextView taskTags = taskView.findViewById(R.id.taskTags);

                // Set task data
                taskName.setText(task.getTaskName());
                taskTime.setText(task.getTime());
                taskTags.setText(task.getTags());

                // Add to container
                taskContainer.addView(taskView);
            }
        }
    }

//    private void setupDayClickListeners() {
//        int[] dayIds = {
//                R.id.day_sun,
//                R.id.day_mon,
//                R.id.day_tue,
//                R.id.day_wed,
//                R.id.day_thu,
//                R.id.day_fri,
//                R.id.day_sat
//        };
//
//        for (int i = 0; i < dayIds.length; i++) {
//            final int index = i;
//            findViewById(dayIds[i]).setOnClickListener(v -> {
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.DAY_OF_WEEK, index + 1);
//                String selectedDate = new SimpleDateFormat("d-M-yyyy", Locale.getDefault()).format(calendar.getTime());
//                loadTasksByDate(MainActivity.this, selectedDate);
//            });
//        }
//    }
}