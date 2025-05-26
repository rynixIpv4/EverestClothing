package com.example.everestclothing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everestclothing.R;
import com.example.everestclothing.database.DatabaseHelper;
import com.example.everestclothing.models.User;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private TextView loginLink, termsLink;
    private CheckBox gdprCheckbox;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Initialize views
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);
        gdprCheckbox = findViewById(R.id.gdprCheckbox);
        termsLink = findViewById(R.id.termsLink);

        // Set click listeners
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to login activity
            }
        });
        
        termsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTermsAndPrivacyDialog();
            }
        });
    }

    private void showTermsAndPrivacyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.terms_and_conditions);
        builder.setMessage("These are the Terms and Conditions for Everest Clothing App.\n\n" +
                "1. User accounts must provide accurate information.\n" +
                "2. Users are responsible for maintaining the confidentiality of their account.\n" +
                "3. Products displayed may vary in actual appearance.\n" +
                "4. Shipping information provided must be accurate.\n" +
                "5. Returns and refunds are subject to our return policy.\n\n" +
                "Privacy Policy:\n\n" +
                "1. We collect personal information for order processing and account management.\n" +
                "2. Your data is stored securely and not shared with third parties except for order fulfillment.\n" +
                "3. We use cookies to enhance your shopping experience.\n" +
                "4. You have the right to access and request deletion of your data.");
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError("Username is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return;
        }
        
        // Validate GDPR consent
        if (!gdprCheckbox.isChecked()) {
            Toast.makeText(this, R.string.gdpr_required, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if username already exists
        if (dbHelper.getUserByUsername(username) != null) {
            usernameEditText.setError("Username already exists");
            return;
        }

        // Check if email already exists
        if (dbHelper.getUserByEmail(email) != null) {
            emailEditText.setError("Email already exists");
            return;
        }

        // Create new user
        User user = new User(username, email, password);
        long id = dbHelper.addUser(user);

        if (id != -1) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
        }
    }
} 