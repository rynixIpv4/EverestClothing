package com.example.everestclothing.models;

public class Order {
    private long id;
    private long userId;
    private double total;
    private String status;
    private String shippingAddress;
    private String createdAt;

    public Order() {
    }

    public Order(long userId, double total, String status, String shippingAddress) {
        this.userId = userId;
        this.total = total;
        this.status = status;
        this.shippingAddress = shippingAddress;
    }

    public Order(long id, long userId, double total, String status, String shippingAddress, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.total = total;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
} 