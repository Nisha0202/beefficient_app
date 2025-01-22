package com.example.beefficientapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText emailEditText, usernameEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize UI elements
        emailEditText = findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        rememberMeCheckBox = findViewById(R.id.rememberMe);
        signUpButton = findViewById(R.id.signupButton);

        // Set up listener for "Log In" text
        TextView loginText = findViewById(R.id.loginText);
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Set up listener for the Sign Up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    // Handle the sign-up logic
    private void signUp() {
        String email = emailEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Check for empty fields
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError("Username is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        // You can add further validation such as checking for valid email format, password strength, etc.

        // If everything is valid, proceed with the sign-up process
        // For demonstration, we'll assume sign-up is successful
        // For example, saving data to Firebase, database, etc.

        // Display success message (could be replaced by actual sign-up logic)
        Toast.makeText(SignUpActivity.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();

        // Navigate to a new activity after successful sign-up (e.g., a welcome screen or main activity)
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class); // Replace with actual welcome or main activity
        startActivity(intent);

        // Optionally, finish the current activity so the user cannot go back to sign up
        finish();
    }
}
