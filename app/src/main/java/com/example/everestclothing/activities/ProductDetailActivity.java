package com.example.everestclothing.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.everestclothing.R;
import com.example.everestclothing.database.DatabaseHelper;
import com.example.everestclothing.models.Product;
import com.example.everestclothing.utils.ImageHelper;
import com.example.everestclothing.utils.SessionManager;

import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private static final String TAG = "ProductDetailActivity";
    private ImageView productImage;
    private TextView productName;
    private TextView productPrice;
    private TextView productDescription;
    private TextView quantityText;
    private Button decreaseButton;
    private Button increaseButton;
    private Button addToCartButton;
    private RadioGroup sizeRadioGroup;
    
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private Product product;
    private int quantity = 1;
    private String selectedSize = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Setup transparent status bar - using a simpler approach to avoid crashes
        setupStatusBar();
        
        setContentView(R.layout.activity_product_detail);

        // Initialize helpers
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        
        try {
            // Initialize views
            productImage = findViewById(R.id.productImage);
            productName = findViewById(R.id.productName);
            productPrice = findViewById(R.id.productPrice);
            productDescription = findViewById(R.id.productDescription);
            quantityText = findViewById(R.id.quantityText);
            decreaseButton = findViewById(R.id.decreaseButton);
            increaseButton = findViewById(R.id.increaseButton);
            addToCartButton = findViewById(R.id.addToCartButton);
            sizeRadioGroup = findViewById(R.id.sizeRadioGroup);
            
            // Get product ID from intent
            long productId = getIntent().getLongExtra("product_id", -1);
            
            if (productId == -1) {
                Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            
            // Load product details
            product = dbHelper.getProduct(productId);
            
            if (product == null) {
                Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            
            // Set product details
            productName.setText(product.getName());
            productPrice.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));
            productDescription.setText(product.getDescription());
            
            // Load product image using ImageHelper
            ImageHelper.loadProductImage(this, productImage, product.getImageUrl());
            
            // Set up size options
            setupSizeOptions();
            
            // Set up direct click listeners for buttons
            decreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantity > 1) {
                        quantity--;
                        quantityText.setText(String.valueOf(quantity));
                    }
                }
            });
            
            increaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity++;
                    quantityText.setText(String.valueOf(quantity));
                }
            });
            
            addToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToCart();
                }
            });
            
            // Set up product image click listener for full-screen view
            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFullScreenImage();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage());
            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    /**
     * Opens the product image in full screen
     */
    private void openFullScreenImage() {
        try {
            if (product != null) {
                Intent intent = new Intent(this, FullScreenImageActivity.class);
                intent.putExtra("image_url", product.getImageUrl());
                intent.putExtra("product_name", product.getName());
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error opening full screen image: " + e.getMessage());
            Toast.makeText(this, "Could not open image", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Setup transparent status bar with a simpler approach to avoid crashes
     */
    private void setupStatusBar() {
        try {
            // Make status bar transparent
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // For lighter background, use dark status bar icons
                    View decorView = getWindow().getDecorView();
                    int flags = decorView.getSystemUiVisibility();
                    flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    decorView.setSystemUiVisibility(flags);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting up status bar: " + e.getMessage());
        }
    }
    
    private void setupSizeOptions() {
        // Clear any existing radio buttons
        sizeRadioGroup.removeAllViews();
        
        // Get available sizes for the product
        String[] sizes = product.getSizesArray();
        
        // Create radio buttons for each size
        for (String size : sizes) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(size);
            radioButton.setTextSize(16);
            radioButton.setPadding(32, 16, 32, 16);
            
            // Add to radio group
            sizeRadioGroup.addView(radioButton);
        }
        
        // Set a listener for size selection
        sizeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton != null) {
                    selectedSize = radioButton.getText().toString();
                }
            }
        });
        
        // Select first size by default if available
        if (sizes.length > 0) {
            ((RadioButton)sizeRadioGroup.getChildAt(0)).setChecked(true);
            selectedSize = sizes[0];
        }
    }
    
    private void addToCart() {
        if (selectedSize.isEmpty()) {
            Toast.makeText(this, R.string.please_select_size, Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            // Redirect to login activity
            Toast.makeText(this, "Please login to add items to cart", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        
        long userId = sessionManager.getUserId();
        long productId = product.getId();
        
        // Add to cart with selected size
        long result = dbHelper.addToCart(userId, productId, quantity, selectedSize);
        
        if (result != -1) {
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
        }
    }
} 