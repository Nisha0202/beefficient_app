package com.example.beefficientapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreatedTags extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String SELECTED_TAG_KEY = "selectedTag";

    private LinearLayout menuSection;
    private EditText editNewTag;
    private Button addNewTagButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_tags);

        menuSection = findViewById(R.id.menu_section);
        editNewTag = findViewById(R.id.newTag);
        addNewTagButton = findViewById(R.id.addNewTagButton);

        // Predefined Tags
        setupTagClickListener(findViewById(R.id.newTag));
        setupTagClickListener(findViewById(R.id.classesTag));
        setupTagClickListener(findViewById(R.id.exerciseTag));
        setupTagClickListener(findViewById(R.id.skincareTag));

        // Add new tag functionality
        addNewTagButton.setOnClickListener(v -> {
            String newTag = editNewTag.getText().toString().trim();
            if (!newTag.isEmpty()) {
                addNewTag(newTag);
                editNewTag.setText(""); // Clear the input field
            } else {
                Toast.makeText(this, "Enter a valid tag name", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setupTagClickListener(TextView tagView) {
        tagView.setOnClickListener(v -> {
            String tagName = tagView.getText().toString();
            saveSelectedTag(tagName);

            // Pass the selected tag back to CreateTaskActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_tag", tagName);
            setResult(RESULT_OK, resultIntent);
            Toast.makeText(this, "Tag Selected: " + tagName, Toast.LENGTH_SHORT).show();
            finish(); // Close this activity

        });
    }


    private void saveSelectedTag(String tagName) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_TAG_KEY, tagName);
        editor.apply();
    }

    private void addNewTag(String tagName) {

        // Divider
        View divider = new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
        ));
        divider.setBackgroundColor(Color.parseColor("#898989"));

        TextView newTagView = new TextView(this);
        newTagView.setText(tagName);
        newTagView.setTextSize(16);
        newTagView.setTextColor(getResources().getColor(android.R.color.black));
        newTagView.setPadding(0, 20, 0, 20);



        // Add to menuSection
        menuSection.addView(divider);
        menuSection.addView(newTagView);


        // Set click listener for the new tag
        setupTagClickListener(newTagView);

        Toast.makeText(this, "Tag Added: " + tagName, Toast.LENGTH_SHORT).show();
    }
}
