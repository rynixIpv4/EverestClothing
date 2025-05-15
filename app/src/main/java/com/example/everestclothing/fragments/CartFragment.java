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
import com.example.everestclothing.activities.LoginActivity;
import com.example.everestclothing.adapters.CartAdapter;
import com.example.everestclothing.database.DatabaseHelper;
import com.example.everestclothing.models.CartItem;
import com.example.everestclothing.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment implements CartAdapter.CartAdapterListener {

    private RecyclerView recyclerView;
    private TextView emptyCartMessage;
    private TextView totalPrice;
    private Button checkoutButton;
    private Button loginButton;
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
        loginButton = view.findViewById(R.id.loginButton);
        
        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            // Show login message and button
            recyclerView.setVisibility(View.GONE);
            emptyCartMessage.setText(R.string.please_login_to_view_cart);
            emptyCartMessage.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
            
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            });
        } else {
            // User is logged in, load cart items
            checkoutButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
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
        }
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check login status and reload cart items when fragment resumes
        if (sessionManager.isLoggedIn()) {
            checkoutButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            loadCartItems();
        } else {
            // Show login message and button
            recyclerView.setVisibility(View.GONE);
            emptyCartMessage.setText(R.string.please_login_to_view_cart);
            emptyCartMessage.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }
    
    private void loadCartItems() {
        long userId = sessionManager.getUserId();
        cartItems = dbHelper.getCartItems(userId);
        
        if (cartItems.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyCartMessage.setText(R.string.your_cart_is_empty);
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
            emptyCartMessage.setText(R.string.your_cart_is_empty);
            emptyCartMessage.setVisibility(View.VISIBLE);
            checkoutButton.setEnabled(false);
        }
        
        // Update total price
        updateTotalPrice();
    }
} 