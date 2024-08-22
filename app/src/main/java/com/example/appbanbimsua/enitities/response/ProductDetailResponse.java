package com.example.appbanbimsua.enitities.response;

import com.example.appbanbimsua.enitities.ProductDetail;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.List;

public class ProductDetailResponse implements Serializable {
    private ProductDetail product;
    private List<Double> sizeUs;
    private List<Integer> sizeVn;
    private List<Double> sizeCm;
    private List<Integer> availableSizes;
    private boolean canBuy;

    public ProductDetail getProduct() {
        return product;
    }

    public void setProduct(ProductDetail product) {
        this.product = product;
    }

    public List<Double> getSizeUs() {
        return sizeUs;
    }

    public void setSizeUs(List<Double> sizeUs) {
        this.sizeUs = sizeUs;
    }

    public List<Integer> getSizeVn() {
        return sizeVn;
    }

    public void setSizeVn(List<Integer> sizeVn) {
        this.sizeVn = sizeVn;
    }

    public List<Double> getSizeCm() {
        return sizeCm;
    }

    public void setSizeCm(List<Double> sizeCm) {
        this.sizeCm = sizeCm;
    }

    public List<Integer> getAvailableSizes() {
        return availableSizes;
    }

    public void setAvailableSizes(List<Integer> availableSizes) {
        this.availableSizes = availableSizes;
    }

    public boolean isCanBuy() {
        return canBuy;
    }

    public void setCanBuy(boolean canBuy) {
        this.canBuy = canBuy;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

}
