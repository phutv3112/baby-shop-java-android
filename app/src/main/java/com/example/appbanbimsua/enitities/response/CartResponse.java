package com.example.appbanbimsua.enitities.response;

import com.example.appbanbimsua.enitities.ProductCart;

import java.util.List;

public class CartResponse {
    private List<ProductCart> products;
    private String status;
    private String message;

    // Getters and Setters


    public List<ProductCart> getProducts() {
        return products;
    }

    public void setProducts(List<ProductCart> products) {
        this.products = products;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
