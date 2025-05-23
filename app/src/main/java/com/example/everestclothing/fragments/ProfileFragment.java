package com.example.everestclothing.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.everestclothing.R;
import com.example.everestclothing.activities.LoginActivity;
import com.example.everestclothing.activities.OrderHistoryActivity;
import com.example.everestclothing.activities.UpdateProfileActivity;
import com.example.everestclothing.database.DatabaseHelper;
import com.example.everestclothing.models.User;
import com.example.everestclothing.utils.SessionManager;

public class ProfileFragment extends Fragment {

    private TextView usernameText;
    private TextView emailText;
    private TextView addressText;
    private TextView phoneText;
    private Button ordersButton;
    private Button updateProfileButton;
    private Button logoutButton;
    private SessionManager sessionManager;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize session manager and database helper
        sessionManager = new SessionManager(getContext());
        dbHelper = new DatabaseHelper(getContext());
        
        // Initialize views
        usernameText = view.findViewById(R.id.usernameText);
        emailText = view.findViewById(R.id.emailText);
        addressText = view.findViewById(R.id.addressText);
        phoneText = view.findViewById(R.id.phoneText);
        ordersButton = view.findViewById(R.id.ordersButton);
        updateProfileButton = view.findViewById(R.id.updateProfileButton);
        logoutButton = view.findViewById(R.id.logoutButton);
        
        // Load and display user information
        loadUserProfile();
        
        // Set click listeners
        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), OrderHistoryActivity.class));
            }
        });
        
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UpdateProfileActivity.class));
            }
        });
        
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log out user
                sessionManager.logoutUser();
                
                // Redirect to login activity
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Reload user profile when returning to this fragment
        loadUserProfile();
    }
    
    private void loadUserProfile() {
        // Get current user from database to ensure we have the latest data
        User user = dbHelper.getUser(sessionManager.getUserId());
        
        if (user != null) {
            usernameText.setText(user.getUsername());
            emailText.setText(user.getEmail());
            
            // Set address and phone number if available
            if (user.getAddress() != null && !user.getAddress().isEmpty()) {
                addressText.setVisibility(View.VISIBLE);
                addressText.setText("Address: " + user.getAddress());
            } else {
                addressText.setVisibility(View.GONE);
            }
            
            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                phoneText.setVisibility(View.VISIBLE);
                phoneText.setText("Phone: " + user.getPhone());
            } else {
                phoneText.setVisibility(View.GONE);
            }
        }
    }
} 