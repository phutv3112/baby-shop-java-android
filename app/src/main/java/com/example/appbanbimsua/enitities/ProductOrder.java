package com.example.appbanbimsua.enitities;

public class ProductOrder {
    private String productId;
    private int size;
    private String quantity;

    public String getProductId() {
        if (productId == null) {
            return "";
        }
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getQuantity() {
        if (quantity == null) {
            return "";
        }
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
