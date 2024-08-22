package com.example.appbanbimsua.enitities.request;


import com.example.appbanbimsua.enitities.ProductOrder;

import java.util.List;

public class OrderRequest {
    private List<ProductOrder> products;
    private String receiver_name;
    private String receiver_phone;
    private String receiver_address;
    private String coupon_code;
    private int total_price;
    private int product_price;
    private String note;
    private int userId;

    public OrderRequest() {
    }

    public OrderRequest(List<ProductOrder> products, String receiver_name, String receiver_phone, String receiver_address, String coupon_code, int total_price, int product_price, String note, int userId) {
        this.products = products;
        this.receiver_name = receiver_name;
        this.receiver_phone = receiver_phone;
        this.receiver_address = receiver_address;
        this.coupon_code = coupon_code;
        this.total_price = total_price;
        this.product_price = product_price;
        this.note = note;
        this.userId = userId;
    }

    public List<ProductOrder> getProducts() {
        return products;
    }

    public void setProducts(List<ProductOrder> products) {
        this.products = products;
    }

    public String getReceiver_name() {
        if (receiver_name == null) {
            return "";
        }
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getReceiver_phone() {
        if (receiver_phone == null) {
            return "";
        }
        return receiver_phone;
    }

    public void setReceiver_phone(String receiver_phone) {
        this.receiver_phone = receiver_phone;
    }

    public String getReceiver_address() {
        if (receiver_address == null) {
            return "";
        }
        return receiver_address;
    }

    public void setReceiver_address(String receiver_address) {
        this.receiver_address = receiver_address;
    }

    public String getCoupon_code() {
        if (coupon_code == null) {
            return "";
        }
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public String getNote() {
        if (note == null) {
            return "";
        }
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
