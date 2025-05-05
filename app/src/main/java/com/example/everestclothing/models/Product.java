package com.example.everestclothing.models;

public class Product {
    private long id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String category;
    private String availableSizes; // Comma-separated sizes e.g. "S,M,L,XL"

    public Product() {
    }

    public Product(long id, String name, String description, double price, String imageUrl, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.availableSizes = "S,M,L,XL"; // Default sizes
    }

    public Product(long id, String name, String description, double price, String imageUrl, String category, String availableSizes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.availableSizes = availableSizes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getAvailableSizes() {
        return availableSizes;
    }
    
    public void setAvailableSizes(String availableSizes) {
        this.availableSizes = availableSizes;
    }
    
    // Returns an array of available sizes
    public String[] getSizesArray() {
        if (availableSizes == null || availableSizes.isEmpty()) {
            return new String[]{"S", "M", "L", "XL"};
        }
        return availableSizes.split(",");
    }
} 