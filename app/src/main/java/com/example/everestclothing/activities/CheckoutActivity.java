package com.example.everestclothing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everestclothing.R;
import com.example.everestclothing.adapters.CheckoutAdapter;
import com.example.everestclothing.database.DatabaseHelper;
import com.example.everestclothing.models.CartItem;
import com.example.everestclothing.models.Order;
import com.example.everestclothing.models.User;
import com.example.everestclothing.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private TextInputEditText fullNameEditText;
    private TextInputEditText addressEditText;
    private TextInputEditText phoneEditText;
    private RecyclerView recyclerView;
    private TextView totalPrice;
    private Button placeOrderButton;
    private ImageButton backButton;
    
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private List<CartItem> cartItems;
    private double totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize helpers
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        
        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Please login to checkout", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CheckoutActivity.this, LoginActivity.class));
            finish();
            return;
        }
        
        // Initialize views
        fullNameEditText = findViewById(R.id.fullNameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        recyclerView = findViewById(R.id.recyclerView);
        totalPrice = findViewById(R.id.totalPrice);
        placeOrderButton = findViewById(R.id.placeOrderButton);
        backButton = findViewById(R.id.backButton);
        
        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Load cart items
        loadCartItems();
        
        // Set up user information if available
        setupUserInfo();
        
        // Set click listeners
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
        
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    private void loadCartItems() {
        long userId = sessionManager.getUserId();
        cartItems = dbHelper.getCartItems(userId);
        
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Set up adapter
        CheckoutAdapter adapter = new CheckoutAdapter(this, cartItems);
        recyclerView.setAdapter(adapter);
        
        // Calculate total price
        calculateTotal();
    }
    
    private void setupUserInfo() {
        long userId = sessionManager.getUserId();
        User user = dbHelper.getUser(userId);
        
        if (user != null) {
            if (!TextUtils.isEmpty(user.getFullName())) {
                fullNameEditText.setText(user.getFullName());
            }
            
            if (!TextUtils.isEmpty(user.getAddress())) {
                addressEditText.setText(user.getAddress());
            }
            
            if (!TextUtils.isEmpty(user.getPhone())) {
                phoneEditText.setText(user.getPhone());
            }
        }
    }
    
    private void calculateTotal() {
        totalAmount = 0;
        for (CartItem item : cartItems) {
            totalAmount += item.getSubtotal();
        }
        totalPrice.setText(String.format(Locale.getDefault(), "$%.2f", totalAmount));
    }
    
    private void placeOrder() {
        String fullName = fullNameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        
        // Validate inputs
        if (TextUtils.isEmpty(fullName)) {
            fullNameEditText.setError("Full name is required");
            return;
        }
        
        if (TextUtils.isEmpty(address)) {
            addressEditText.setError("Address is required");
            return;
        }
        
        if (TextUtils.isEmpty(phone)) {
            phoneEditText.setError("Phone number is required");
            return;
        }
        
        // Update user information
        updateUserInfo(fullName, address, phone);
        
        // Create order
        long userId = sessionManager.getUserId();
        Order order = new Order(userId, totalAmount, "Processing", address);
        
        // Save order to database
        long orderId = dbHelper.createOrder(order, cartItems);
        
        if (orderId != -1) {
            Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void updateUserInfo(String fullName, String address, String phone) {
        long userId = sessionManager.getUserId();
        User user = dbHelper.getUser(userId);
        
        if (user != null) {
            user.setFullName(fullName);
            user.setAddress(address);
            user.setPhone(phone);
            dbHelper.updateUser(user);
        }
    }
} 