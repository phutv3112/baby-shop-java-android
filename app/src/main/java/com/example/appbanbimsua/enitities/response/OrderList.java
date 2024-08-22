package com.example.appbanbimsua.enitities.response;

import com.google.gson.annotations.SerializedName;

public class OrderList {
    @SerializedName("billCode")
    private String billCode;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("totalPrice")
    private long totalPrice;

    @SerializedName("status")
    private String status;

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
