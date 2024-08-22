package com.example.appbanbimsua.enitities.response;

import com.google.gson.annotations.SerializedName;

public class OrderDetailResponse {
    @SerializedName("billCode")
    private String billCode;

    @SerializedName("prices")
    private int prices;

    @SerializedName("receiverName")
    private String receiverName;

    @SerializedName("receiverAddress")
    private String receiverAddress;

    @SerializedName("receiverPhone")
    private String receiverPhone;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("note")
    private String note;

    @SerializedName("productId")
    private String productId;

    // Getters and setters
    public String getBillCode() {
        return billCode != null ? billCode : "";
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public int getPrices() {
        return prices;
    }

    public void setPrices(int prices) {
        this.prices = prices;
    }

    public String getReceiverName() {
        return receiverName != null ? receiverName : "";
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress != null ? receiverAddress : "";
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverPhone() {
        return receiverPhone != null ? receiverPhone : "";
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note != null ? note : "";
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getProductId() {
        return productId != null ? productId : "";
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
