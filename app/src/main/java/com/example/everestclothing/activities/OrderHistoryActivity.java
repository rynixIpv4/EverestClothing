package com.example.everestclothing.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everestclothing.R;
import com.example.everestclothing.adapters.OrderAdapter;
import com.example.everestclothing.database.DatabaseHelper;
import com.example.everestclothing.models.Order;
import com.example.everestclothing.utils.SessionManager;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity implements OrderAdapter.OrderAdapterListener {

    private static final String TAG = "OrderHistoryActivity";
    private RecyclerView recyclerView;
    private TextView emptyOrdersMessage;
    private OrderAdapter adapter;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Setup proper window decorations
        setupStatusBar();
        
        setContentView(R.layout.activity_order_history);

        // Initialize helpers
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        
        // Initialize toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        
        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        emptyOrdersMessage = findViewById(R.id.emptyOrdersMessage);
        
        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Load orders
        loadOrders();
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
    
    private void loadOrders() {
        long userId = sessionManager.getUserId();
        Log.d(TAG, "Loading orders for user ID: " + userId);
        
        orderList = dbHelper.getUserOrders(userId);
        Log.d(TAG, "Found " + orderList.size() + " orders");
        
        if (orderList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyOrdersMessage.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyOrdersMessage.setVisibility(View.GONE);
            
            // Set up adapter
            adapter = new OrderAdapter(this, orderList, this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onViewDetails(Order order) {
        Log.d(TAG, "onViewDetails called for order ID: " + order.getId());
        
        // Launch OrderDetailActivity with the selected order
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, order.getId());
        
        Log.d(TAG, "Starting OrderDetailActivity with order ID: " + order.getId());
        startActivity(intent);
        
        // Add a toast for debugging
        Toast.makeText(this, "Viewing order #" + order.getId(), Toast.LENGTH_SHORT).show();
    }
} 