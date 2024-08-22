package com.example.appbanbimsua.enitities;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class ProductDetail implements Serializable {
    private String id;
    private String name;
    private String slug;
    private int price;
    private int views;
    private int quantity;
    private int totalSold;
    private List<String> productImages;
    private List<String> feedbackImages;
    private int promotionPrice;
    private String couponCode;
    private String description;
    private Brand brand;
    private List<Comment> comments;

    public String getId() {
        if (id == null) {
            return "";
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        if (slug == null) {
            return "";
        }
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(int totalSold) {
        this.totalSold = totalSold;
    }

    public List<String> getProductImages() {
        if (productImages == null) {
            return Collections.singletonList("");
        }
        return productImages;
    }

    public void setProductImages(List<String> productImages) {
        this.productImages = productImages;
    }

    public List<String> getFeedbackImages() {
        if (feedbackImages == null) {
            return Collections.singletonList("");
        }
        return feedbackImages;
    }

    public void setFeedbackImages(List<String> feedbackImages) {
        this.feedbackImages = feedbackImages;
    }

    public int getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(int promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public String getCouponCode() {
        if (couponCode == null) {
            return "";
        }
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDescription() {
        if (description == null) {
            return "";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


}
