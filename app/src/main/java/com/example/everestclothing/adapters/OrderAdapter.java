package com.example.everestclothing.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everestclothing.R;
import com.example.everestclothing.models.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private static final String TAG = "OrderAdapter";
    private Context context;
    private List<Order> orderList;
    private OrderAdapterListener listener;

    public interface OrderAdapterListener {
        void onViewDetails(Order order);
    }

    public OrderAdapter(Context context, List<Order> orderList, OrderAdapterListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
        Log.d(TAG, "OrderAdapter created with " + orderList.size() + " orders, listener: " + (listener != null));
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        
        holder.orderId.setText(String.valueOf(order.getId()));
        holder.orderStatus.setText(order.getStatus());
        holder.totalPrice.setText(String.format(Locale.getDefault(), "$%.2f", order.getTotal()));
        
        // Format the date
        String createdAt = order.getCreatedAt();
        try {
            SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat toFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            Date date = fromFormat.parse(createdAt);
            if (date != null) {
                holder.orderDate.setText(toFormat.format(date));
            } else {
                holder.orderDate.setText(createdAt);
            }
        } catch (ParseException e) {
            holder.orderDate.setText(createdAt);
        }
        
        holder.viewDetailsButton.setOnClickListener(view -> {
            Log.d(TAG, "View details button clicked for order ID: " + order.getId());
            if (listener != null) {
                listener.onViewDetails(order);
            } else {
                Log.e(TAG, "Listener is null, cannot handle view details click");
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId;
        TextView orderDate;
        TextView orderStatus;
        TextView totalPrice;
        Button viewDetailsButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            viewDetailsButton = itemView.findViewById(R.id.viewDetailsButton);
        }
    }
} 