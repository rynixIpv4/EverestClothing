package com.example.everestclothing.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.everestclothing.R;
import com.example.everestclothing.models.CartItem;
import com.example.everestclothing.utils.ImageHelper;

import java.util.List;
import java.util.Locale;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {

    private Context context;
    private List<CartItem> cartItems;

    public CheckoutAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkout, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        
        holder.productName.setText(cartItem.getProductName());
        holder.priceAndQuantity.setText(String.format(Locale.getDefault(), "$%.2f x %d", 
                cartItem.getPrice(), cartItem.getQuantity()));
        holder.subtotalPrice.setText(String.format(Locale.getDefault(), "$%.2f", cartItem.getSubtotal()));
        
        // Load product image using the ImageHelper
        ImageHelper.loadProductImage(context, holder.productImage, cartItem.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CheckoutViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView priceAndQuantity;
        TextView subtotalPrice;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            priceAndQuantity = itemView.findViewById(R.id.priceAndQuantity);
            subtotalPrice = itemView.findViewById(R.id.subtotalPrice);
        }
    }
} 