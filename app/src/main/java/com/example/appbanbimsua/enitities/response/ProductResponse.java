package com.example.appbanbimsua.enitities.response;

import com.example.appbanbimsua.enitities.Product;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductResponse {
    @SerializedName("products")
    private List<Product> product;
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

    // Getter và Setter cho `product`
    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    // Getter và Setter cho `status`
    public String getStatus() {
        if (status == null) {
            return "";
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getter và Setter cho `message`
    public String getMessage() {
        if (message == null) {
            return "";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
