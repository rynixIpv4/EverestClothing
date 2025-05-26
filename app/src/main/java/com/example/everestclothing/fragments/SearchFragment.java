package com.example.everestclothing.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everestclothing.R;
import com.example.everestclothing.adapters.ProductAdapter;
import com.example.everestclothing.database.DatabaseHelper;
import com.example.everestclothing.models.Product;

import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private TextView emptyView;
    private ProductAdapter adapter;
    private DatabaseHelper dbHelper;
    private CardView searchContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize database helper
        dbHelper = new DatabaseHelper(getContext());
        
        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);
        emptyView = view.findViewById(R.id.emptyView);
        searchContainer = view.findViewById(R.id.searchContainer);
        
        // Set up RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        
        // Set up adapter with empty list
        adapter = new ProductAdapter(getContext(), dbHelper.searchProducts(""));
        recyclerView.setAdapter(adapter);
        
        // Make the entire search container clickable
        searchContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.requestFocus();
                searchView.setIconified(false);  // Expand the SearchView
            }
        });
        
        // Set up search listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    performSearch(newText);
                }
                return true;
            }
        });
        
        return view;
    }
    
    private void performSearch(String query) {
        List<Product> searchResults = dbHelper.searchProducts(query);
        
        if (searchResults.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            adapter.updateList(searchResults);
        }
    }
} 