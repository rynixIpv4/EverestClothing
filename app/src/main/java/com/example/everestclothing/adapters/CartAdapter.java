package com.example.everestclothing.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.everestclothing.R;
import com.example.everestclothing.database.DatabaseHelper;
import com.example.everestclothing.models.CartItem;
import com.example.everestclothing.utils.ImageHelper;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private DatabaseHelper dbHelper;
    private CartAdapterListener listener;

    public interface CartAdapterListener {
        void onCartUpdated();
        void onItemRemoved();
    }

    public CartAdapter(Context context, List<CartItem> cartItems, CartAdapterListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        
        holder.productName.setText(cartItem.getProductName());
        holder.productPrice.setText(String.format(Locale.getDefault(), "$%.2f", cartItem.getPrice()));
        holder.productSize.setText(String.format(Locale.getDefault(), "%s: %s", context.getString(R.string.size), cartItem.getSize()));
        holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));
        holder.subtotalPrice.setText(String.format(Locale.getDefault(), "$%.2f", cartItem.getSubtotal()));
        
        // Load product image using the ImageHelper
        ImageHelper.loadProductImage(context, holder.productImage, cartItem.getImageUrl());
        
        // Set click listeners for quantity manipulation
        holder.decreaseButton.setOnClickListener(view -> {
            int quantity = cartItem.getQuantity();
            if (quantity > 1) {
                quantity--;
                cartItem.setQuantity(quantity);
                dbHelper.updateCartItemQuantity(cartItem.getId(), quantity);
                holder.quantityText.setText(String.valueOf(quantity));
                holder.subtotalPrice.setText(String.format(Locale.getDefault(), "$%.2f", cartItem.getSubtotal()));
                if (listener != null) {
                    listener.onCartUpdated();
                }
            }
        });
        
        holder.increaseButton.setOnClickListener(view -> {
            int quantity = cartItem.getQuantity();
            quantity++;
            cartItem.setQuantity(quantity);
            dbHelper.updateCartItemQuantity(cartItem.getId(), quantity);
            holder.quantityText.setText(String.valueOf(quantity));
            holder.subtotalPrice.setText(String.format(Locale.getDefault(), "$%.2f", cartItem.getSubtotal()));
            if (listener != null) {
                listener.onCartUpdated();
            }
        });
        
        holder.removeButton.setOnClickListener(view -> {
            dbHelper.deleteCartItem(cartItem.getId());
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
            if (listener != null) {
                listener.onItemRemoved();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateList(List<CartItem> newList) {
        this.cartItems = newList;
        notifyDataSetChanged();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productSize;
        TextView quantityText;
        TextView subtotalPrice;
        Button decreaseButton;
        Button increaseButton;
        ImageButton removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productSize = itemView.findViewById(R.id.productSize);
            quantityText = itemView.findViewById(R.id.quantityText);
            subtotalPrice = itemView.findViewById(R.id.subtotalPrice);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
} 