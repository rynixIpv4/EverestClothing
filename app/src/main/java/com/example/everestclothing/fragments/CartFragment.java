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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everestclothing.R;
import com.example.everestclothing.activities.CheckoutActivity;
import com.example.everestclothing.adapters.CartAdapter;
import com.example.everestclothing.database.DatabaseHelper;
import com.example.everestclothing.models.CartItem;
import com.example.everestclothing.utils.SessionManager;

import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment implements CartAdapter.CartAdapterListener {

    private RecyclerView recyclerView;
    private TextView emptyCartMessage;
    private TextView totalPrice;
    private Button checkoutButton;
    private CartAdapter adapter;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private List<CartItem> cartItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize helpers
        dbHelper = new DatabaseHelper(getContext());
        sessionManager = new SessionManager(getContext());
        
        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerView);
        emptyCartMessage = view.findViewById(R.id.emptyCartMessage);
        totalPrice = view.findViewById(R.id.totalPrice);
        checkoutButton = view.findViewById(R.id.checkoutButton);
        
        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Load cart items
        loadCartItems();
        
        // Set click listener for checkout button
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItems != null && !cartItems.isEmpty()) {
                    startActivity(new Intent(getContext(), CheckoutActivity.class));
                }
            }
        });
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload cart items when fragment resumes (e.g., after checkout)
        loadCartItems();
    }
    
    private void loadCartItems() {
        long userId = sessionManager.getUserId();
        cartItems = dbHelper.getCartItems(userId);
        
        if (cartItems.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyCartMessage.setVisibility(View.VISIBLE);
            checkoutButton.setEnabled(false);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyCartMessage.setVisibility(View.GONE);
            checkoutButton.setEnabled(true);
            
            // Set up adapter
            adapter = new CartAdapter(getContext(), cartItems, this);
            recyclerView.setAdapter(adapter);
            
            // Calculate total price
            updateTotalPrice();
        }
    }
    
    private void updateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }
        totalPrice.setText(String.format(Locale.getDefault(), "$%.2f", total));
    }

    @Override
    public void onCartUpdated() {
        // Update total price when cart is updated
        updateTotalPrice();
    }

    @Override
    public void onItemRemoved() {
        // Check if cart is empty after item removal
        if (cartItems.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyCartMessage.setVisibility(View.VISIBLE);
            checkoutButton.setEnabled(false);
        }
        
        // Update total price
        updateTotalPrice();
    }
} 