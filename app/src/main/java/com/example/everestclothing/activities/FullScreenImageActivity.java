package com.example.everestclothing.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.example.everestclothing.R;
import com.example.everestclothing.utils.ImageHelper;

public class FullScreenImageActivity extends AppCompatActivity {

    private ImageView fullScreenImage;
    private MaterialButton closeButton;
    private TextView productTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Make the activity fullscreen
        setupImmersiveMode();
        
        setContentView(R.layout.activity_full_screen_image);
        
        // Initialize views
        fullScreenImage = findViewById(R.id.fullScreenImage);
        closeButton = findViewById(R.id.closeButton);
        productTitle = findViewById(R.id.productTitle);
        
        // Get data from intent
        String imageUrl = getIntent().getStringExtra("image_url");
        String productName = getIntent().getStringExtra("product_name");
        
        // Set product title
        if (productName != null && !productName.isEmpty()) {
            productTitle.setText(productName);
        } else {
            productTitle.setVisibility(View.GONE);
        }
        
        // Load image
        if (imageUrl != null && !imageUrl.isEmpty()) {
            ImageHelper.loadFullScreenImage(this, fullScreenImage, imageUrl);
        }
        
        // Set close button click listener
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Just finish this activity to go back to the ProductDetailActivity
                finish();
            }
        });
    }

    /**
     * Setup fullscreen immersive mode
     */
    private void setupImmersiveMode() {
        // Make status and navigation bars transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            
            // Set the content to appear behind the system bars
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                getWindow().setDecorFitsSystemWindows(false);
            } else {
                // For older versions
                int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(flags);
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // Re-apply immersive mode when window focus changes
        if (hasFocus) {
            setupImmersiveMode();
        }
    }
} 