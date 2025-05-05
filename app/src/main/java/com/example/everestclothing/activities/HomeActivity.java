package com.example.everestclothing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.everestclothing.R;
import com.example.everestclothing.fragments.CartFragment;
import com.example.everestclothing.fragments.HomeFragment;
import com.example.everestclothing.fragments.ProfileFragment;
import com.example.everestclothing.fragments.SearchFragment;
import com.example.everestclothing.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private SessionManager sessionManager;
    private Toolbar toolbar;
    private ImageView toolbarLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize session manager
        sessionManager = new SessionManager(this);
        
        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
            return;
        }
        
        // Set up toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbarLogo = findViewById(R.id.toolbarLogo);
        setSupportActionBar(toolbar);
        
        // Remove default title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        
        // Initialize bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        
        // Set default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }
    
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = 
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    
                    int id = item.getItemId();
                    if (id == R.id.navigation_home) {
                        selectedFragment = new HomeFragment();
                    } else if (id == R.id.navigation_search) {
                        selectedFragment = new SearchFragment();
                    } else if (id == R.id.navigation_cart) {
                        selectedFragment = new CartFragment();
                    } else if (id == R.id.navigation_profile) {
                        selectedFragment = new ProfileFragment();
                    }
                    
                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, selectedFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                    }
                    
                    return true;
                }
            };
} 