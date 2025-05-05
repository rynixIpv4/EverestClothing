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
import com.example.everestclothing.utils.SessionManager;

public class ProfileFragment extends Fragment {

    private TextView usernameText;
    private TextView emailText;
    private Button ordersButton;
    private Button updateProfileButton;
    private Button logoutButton;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize session manager
        sessionManager = new SessionManager(getContext());
        
        // Initialize views
        usernameText = view.findViewById(R.id.usernameText);
        emailText = view.findViewById(R.id.emailText);
        ordersButton = view.findViewById(R.id.ordersButton);
        updateProfileButton = view.findViewById(R.id.updateProfileButton);
        logoutButton = view.findViewById(R.id.logoutButton);
        
        // Set user information
        usernameText.setText(sessionManager.getUsername());
        emailText.setText(sessionManager.getUserEmail());
        
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
                // TODO: Implement update profile functionality
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
} 