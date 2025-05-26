package com.example.everestclothing.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everestclothing.R;
import com.example.everestclothing.adapters.ProductAdapter;
import com.example.everestclothing.database.DatabaseHelper;
import com.example.everestclothing.models.Product;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private DatabaseHelper dbHelper;
    private DrawerLayout drawerLayout;
    private final Map<Integer, String> categoryMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize database helper
        dbHelper = new DatabaseHelper(getContext());
        
        // Initialize UI components
        drawerLayout = view.findViewById(R.id.drawerLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        
        // Set up filter FAB
        FloatingActionButton filterFab = view.findViewById(R.id.filterFab);
        filterFab.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        
        // Set up category chips
        setupCategoryChips(view);
        
        // Load all products initially
        loadProducts("All");
        
        return view;
    }
    
    private void setupCategoryChips(View view) {
        // Map chip IDs to category names
        categoryMap.put(R.id.chipAll, "All");
        categoryMap.put(R.id.chipTshirts, "Streetwear");
        categoryMap.put(R.id.chipHoodies, "Streetwear");
        categoryMap.put(R.id.chipJeans, "Streetwear");
        categoryMap.put(R.id.chipJackets, "Streetwear");
        categoryMap.put(R.id.chipAccessories, "Accessories");
        categoryMap.put(R.id.chipFootwear, "Footwear");
        
        ChipGroup chipGroup = view.findViewById(R.id.categoryChipGroup);
        
        // Update chip text to reflect streetwear categories
        Chip chipTshirts = view.findViewById(R.id.chipTshirts);
        chipTshirts.setText("Tees & Tops");
        
        Chip chipHoodies = view.findViewById(R.id.chipHoodies);
        chipHoodies.setText("Hoodies & Sweats");
        
        Chip chipJeans = view.findViewById(R.id.chipJeans);
        chipJeans.setText("Pants & Shorts");
        
        Chip chipJackets = view.findViewById(R.id.chipJackets);
        chipJackets.setText("Jackets & Outerwear");
        
        // Set click listeners for all chips
        for (int chipId : categoryMap.keySet()) {
            Chip chip = view.findViewById(chipId);
            chip.setOnClickListener(v -> {
                String category = categoryMap.get(chipId);
                List<Product> filteredProducts;
                
                if (category.equals("All")) {
                    filteredProducts = dbHelper.getAllProducts();
                } else if (category.equals("Streetwear")) {
                    // Filter streetwear products based on the specific subcategory
                    String searchPattern = "";
                    if (chipId == R.id.chipTshirts) {
                        searchPattern = "Tee|Top";
                    } else if (chipId == R.id.chipHoodies) {
                        searchPattern = "Hoodie|Sweatshirt";
                    } else if (chipId == R.id.chipJeans) {
                        searchPattern = "Jeans|Pants|Shorts";
                    } else if (chipId == R.id.chipJackets) {
                        searchPattern = "Jacket|Vest";
                    }
                    
                    // Get all streetwear products first
                    List<Product> streetwearProducts = dbHelper.getProductsByCategory("Streetwear");
                    // Then filter by name pattern
                    filteredProducts = new ArrayList<>();
                    for (Product product : streetwearProducts) {
                        if (product.getName().toLowerCase().matches(".*(" + searchPattern.toLowerCase() + ").*")) {
                            filteredProducts.add(product);
                        }
                    }
                } else {
                    // For non-streetwear categories (Accessories, Footwear)
                    filteredProducts = dbHelper.getProductsByCategory(category);
                }
                
                // Update the RecyclerView with filtered products
                adapter = new ProductAdapter(getContext(), filteredProducts);
                recyclerView.setAdapter(adapter);
                
                // Close the drawer after selection
                drawerLayout.closeDrawer(GravityCompat.START);
            });
        }
    }
    
    private void loadProducts(String category) {
        List<Product> productList;
        if (category.equals("All")) {
            productList = dbHelper.getAllProducts();
        } else {
            productList = dbHelper.getProductsByCategory(category);
        }
        adapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(adapter);
    }
    
    private void loadProductsByNameContaining(String namePattern) {
        List<Product> productList = dbHelper.searchProducts(namePattern);
        adapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(adapter);
    }
} 