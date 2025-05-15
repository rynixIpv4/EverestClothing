package com.example.everestclothing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.view.WindowManager;
import android.view.WindowInsets;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.everestclothing.R;
import com.example.everestclothing.fragments.CartFragment;
import com.example.everestclothing.fragments.HomeFragment;
import com.example.everestclothing.fragments.ProfileFragment;
import com.example.everestclothing.fragments.SearchFragment;
import com.example.everestclothing.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.appbar.AppBarLayout;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private SessionManager sessionManager;
    private Toolbar toolbar;
    private ImageView toolbarLogo;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Apply full screen layout with status bar visible
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
        } else {
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }
        
        setContentView(R.layout.activity_home);

        // Initialize session manager
        sessionManager = new SessionManager(this);
        
        // Set up toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbarLogo = findViewById(R.id.toolbarLogo);
        appBarLayout = findViewById(R.id.appBarLayout);
        setSupportActionBar(toolbar);
        
        // Apply window insets to properly handle status bar
        ViewCompat.setOnApplyWindowInsetsListener(appBarLayout, (v, insets) -> {
            int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            appBarLayout.setPadding(0, statusBarHeight, 0, 0);
            return insets;
        });
        
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
                        // Check if user is logged in for profile
                        if (!sessionManager.isLoggedIn()) {
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            return false;
                        }
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