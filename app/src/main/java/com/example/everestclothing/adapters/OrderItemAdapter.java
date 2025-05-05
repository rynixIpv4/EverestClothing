package com.example.everestclothing.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everestclothing.R;
import com.example.everestclothing.models.CartItem;

import java.util.List;
import java.util.Locale;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private Context context;
    private List<CartItem> itemList;

    public OrderItemAdapter(Context context, List<CartItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        CartItem item = itemList.get(position);
        
        holder.productName.setText(item.getProductName());
        holder.productSize.setText("Size: " + item.getSize());
        holder.productQuantity.setText("Qty: " + item.getQuantity());
        holder.productPrice.setText(String.format(Locale.getDefault(), "$%.2f", (item.getPrice() * item.getQuantity())));
        
        // Set product image
        try {
            String imageName = item.getImageUrl();
            int resourceId = context.getResources().getIdentifier(
                    imageName, "drawable", context.getPackageName());
            
            if (resourceId != 0) {
                holder.productImage.setImageResource(resourceId);
            } else {
                // Set a placeholder image if the resource is not found
                holder.productImage.setImageResource(R.drawable.placeholder_image);
            }
        } catch (Resources.NotFoundException e) {
            // Handle the exception by setting a placeholder image
            holder.productImage.setImageResource(R.drawable.placeholder_image);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productSize;
        TextView productQuantity;
        TextView productPrice;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productSize = itemView.findViewById(R.id.productSize);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }
} 