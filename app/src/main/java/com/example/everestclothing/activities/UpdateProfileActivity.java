package com.example.everestclothing.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.everestclothing.R;
import com.example.everestclothing.database.DatabaseHelper;
import com.example.everestclothing.models.User;
import com.example.everestclothing.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class UpdateProfileActivity extends AppCompatActivity {

    private TextInputLayout fullNameLayout, addressLayout, phoneLayout;
    private TextInputEditText fullNameEditText, addressEditText, phoneEditText;
    private Button updateButton;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Setup proper window decorations
        setupStatusBar();
        
        setContentView(R.layout.activity_update_profile);

        // Initialize toolbar with back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Initialize database and session manager
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Initialize views
        fullNameLayout = findViewById(R.id.fullNameLayout);
        addressLayout = findViewById(R.id.addressLayout);
        phoneLayout = findViewById(R.id.phoneLayout);
        
        fullNameEditText = findViewById(R.id.fullNameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        updateButton = findViewById(R.id.updateButton);

        // Load current user data
        loadUserData();

        // Set update button click listener
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }
    
    /**
     * Set up transparent status bar
     */
    private void setupStatusBar() {
        // Make status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            
            // Enable edge-to-edge
            WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
            
            // Light status bar for visibility
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
                controller.setAppearanceLightStatusBars(true);
            }
            
            // Make sure we draw behind the status bar
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadUserData() {
        // Get current user
        long userId = sessionManager.getUserId();
        User user = dbHelper.getUser(userId);

        if (user != null) {
            // Set current values in the form
            fullNameEditText.setText(user.getFullName() != null ? user.getFullName() : "");
            addressEditText.setText(user.getAddress() != null ? user.getAddress() : "");
            phoneEditText.setText(user.getPhone() != null ? user.getPhone() : "");
        }
    }

    private void updateProfile() {
        // Get values from form
        String fullName = fullNameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(fullName)) {
            fullNameLayout.setError("Full name is required");
            return;
        } else {
            fullNameLayout.setError(null);
        }

        if (TextUtils.isEmpty(address)) {
            addressLayout.setError("Address is required");
            return;
        } else {
            addressLayout.setError(null);
        }

        if (TextUtils.isEmpty(phone)) {
            phoneLayout.setError("Phone number is required");
            return;
        } else {
            phoneLayout.setError(null);
        }

        // Get current user
        long userId = sessionManager.getUserId();
        User user = dbHelper.getUser(userId);

        if (user != null) {
            // Update user information
            user.setFullName(fullName);
            user.setAddress(address);
            user.setPhone(phone);

            // Save to database
            int result = dbHelper.updateUser(user);

            if (result > 0) {
                // Update session with new profile information
                sessionManager.updateUserProfile(fullName, address, phone);
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                finish(); // Go back to profile fragment
            } else {
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        }
    }
} 