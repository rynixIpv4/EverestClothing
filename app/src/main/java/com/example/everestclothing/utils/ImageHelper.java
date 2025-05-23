package com.example.everestclothing.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Helper class for handling image loading with multiple format support
 */
public class ImageHelper {

    /**
     * Loads a product image with flexible format support
     * Attempts to load the image with various extensions (.jpeg, .jpg, .png, and fallback to XML)
     * 
     * @param context The context
     * @param imageView The ImageView to load the image into
     * @param imageName The base name of the image without extension
     */
    public static void loadProductImage(Context context, ImageView imageView, String imageName) {
        // First try to load the image with the same name as provided in the database
        int imageResId = context.getResources().getIdentifier(
                imageName, "drawable", context.getPackageName());
                
        // If the exact name isn't found, try various extensions
        if (imageResId == 0) {
            // Try with product name exactly as in the image file name
            String[] possibleNames = {
                imageName + ".jpeg", 
                imageName + ".jpg", 
                imageName + ".png"
            };
            
            for (String name : possibleNames) {
                imageResId = context.getResources().getIdentifier(
                        name.replace(".", "_"), "drawable", context.getPackageName());
                if (imageResId != 0) break;
            }
            
            // Try with actual product name capitalized version (Urban Puffer Jacket)
            if (imageResId == 0) {
                for (String name : new String[]{
                        "Urban Puffer Jacket", "Distressed Cargo Jeans", "Graphic Streetwear Tee",
                        "Oversized Button-Up", "Utility Jumpsuit", "High-Top Sneakers", "Combat Boots",
                        "Vintage Washed Hoodie", "Statement Neck Scarf", "Skater Shorts", "Oversized Hoodie",
                        "Tactical Cargo Pants", "Urban Art Tee", "Color Block Jacket", "Graffiti Print Sweatshirt",
                        "Baggy Ripped Jeans", "Retro Puffer Vest", "Beanie with Patch", 
                        "Chunky Platform Sneakers", "Relaxed Skate Shorts"}) {
                    
                    // Remove spaces and special characters for resource name
                    String cleanName = name.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
                    
                    imageResId = context.getResources().getIdentifier(
                            cleanName, "drawable", context.getPackageName());
                    
                    if (imageResId != 0) break;
                }
            }
        }
        
        // Load the image using Glide
        if (imageResId != 0) {
            Glide.with(context)
                    .load(imageResId)
                    .centerCrop()
                    .into(imageView);
        } else {
            // If all attempts fail, use a placeholder
            Glide.with(context)
                    .load(android.R.drawable.ic_menu_gallery)
                    .centerCrop()
                    .into(imageView);
        }
    }
} 