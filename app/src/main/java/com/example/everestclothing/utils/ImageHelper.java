package com.example.everestclothing.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Helper class for handling image loading with multiple format support
 */
public class ImageHelper {
    private static final String TAG = "ImageHelper";

    /**
     * Loads a product image with flexible format support
     * 
     * @param context The context
     * @param imageView The ImageView to load the image into
     * @param imageName The base name of the image without extension
     */
    public static void loadProductImage(Context context, ImageView imageView, String imageName) {
        Log.d(TAG, "Loading product image: " + imageName);
        
        // Get the resource ID directly using the image name from the database
        int imageResId = context.getResources().getIdentifier(
                imageName, "drawable", context.getPackageName());
        
        Log.d(TAG, "Found image resource ID: " + imageResId + " for " + imageName);
        
        // Load image with Glide
        if (imageResId != 0) {
            Glide.with(context)
                    .load(imageResId)
                    .centerCrop()
                    .into(imageView);
        } else {
            Log.e(TAG, "Image resource not found for: " + imageName);
            // If not found, use default placeholder
            Glide.with(context)
                    .load(android.R.drawable.ic_menu_gallery)
                    .centerCrop()
                    .into(imageView);
        }
    }
    
    /**
     * Loads a full-screen product image with high quality and no cropping
     * 
     * @param context The context
     * @param imageView The ImageView to load the image into
     * @param imageName The base name of the image without extension
     */
    public static void loadFullScreenImage(Context context, ImageView imageView, String imageName) {
        Log.d(TAG, "Loading full screen image: " + imageName);
        
        // Get the resource ID directly using the image name from the database
        int imageResId = context.getResources().getIdentifier(
                imageName, "drawable", context.getPackageName());
        
        Log.d(TAG, "Found full screen image resource ID: " + imageResId + " for " + imageName);
        
        // Load image with Glide
        if (imageResId != 0) {
            Glide.with(context)
                    .load(imageResId)
                    .apply(new RequestOptions()
                            .fitCenter()
                            .override(2048, 2048) // Load higher resolution for zooming
                            .dontTransform())
                    .into(imageView);
        } else {
            Log.e(TAG, "Full screen image resource not found for: " + imageName);
            // If not found, use default placeholder
            Glide.with(context)
                    .load(android.R.drawable.ic_menu_gallery)
                    .fitCenter()
                    .into(imageView);
        }
    }
} 