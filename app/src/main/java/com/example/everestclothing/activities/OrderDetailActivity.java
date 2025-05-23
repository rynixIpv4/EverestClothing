package com.example.everestclothing.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everestclothing.R;
import com.example.everestclothing.adapters.OrderItemAdapter;
import com.example.everestclothing.database.DatabaseHelper;
import com.example.everestclothing.models.CartItem;
import com.example.everestclothing.models.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER_ID = "order_id";
    private static final String TAG = "OrderDetailActivity";

    private RecyclerView recyclerView;
    private TextView orderId, orderDate, orderStatus, orderTotal, shippingAddress;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Setup proper window decorations
        setupStatusBar();
        
        setContentView(R.layout.activity_order_detail);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        orderId = findViewById(R.id.orderId);
        orderDate = findViewById(R.id.orderDate);
        orderStatus = findViewById(R.id.orderStatus);
        orderTotal = findViewById(R.id.orderTotal);
        shippingAddress = findViewById(R.id.shippingAddress);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get order ID from intent
        long orderIdValue = getIntent().getLongExtra(EXTRA_ORDER_ID, -1);
        Log.d(TAG, "Received order ID: " + orderIdValue);
        
        if (orderIdValue != -1) {
            loadOrderDetails(orderIdValue);
        } else {
            Toast.makeText(this, "Invalid order ID", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if no valid order ID
        }
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

    private void loadOrderDetails(long orderId) {
        // Get the Order directly by ID
        Order order = dbHelper.getOrderById(orderId);
        
        if (order == null) {
            Log.e(TAG, "Order not found for ID: " + orderId);
            Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if order not found
            return;
        }
        
        Log.d(TAG, "Found order: " + order.getId() + ", status: " + order.getStatus());
        
        // Set order details in UI
        this.orderId.setText(String.valueOf(order.getId()));
        orderStatus.setText(order.getStatus());
        orderTotal.setText(String.format(Locale.getDefault(), "$%.2f", order.getTotal()));
        shippingAddress.setText(order.getShippingAddress());
        
        // Format date
        String createdAt = order.getCreatedAt();
        try {
            SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat toFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            Date date = fromFormat.parse(createdAt);
            if (date != null) {
                orderDate.setText(toFormat.format(date));
            } else {
                orderDate.setText(createdAt);
            }
        } catch (ParseException e) {
            orderDate.setText(createdAt);
        }
        
        // Load order items
        List<CartItem> orderItems = dbHelper.getOrderItems(orderId);
        Log.d(TAG, "Found " + orderItems.size() + " items for order");
        
        OrderItemAdapter adapter = new OrderItemAdapter(this, orderItems);
        recyclerView.setAdapter(adapter);
    }
} 