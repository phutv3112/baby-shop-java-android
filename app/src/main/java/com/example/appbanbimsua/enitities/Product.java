package com.example.appbanbimsua.enitities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("slug")
    private String slug;
    @SerializedName("price")
    private double price;
    @SerializedName("views")
    private int views;
    @SerializedName("images")
    private String images;
    @SerializedName("totalSold")
    private int totalSold;
    @SerializedName("promotionPrice")
    private double promotionPrice;

    public Product() {
    }

    public Product(String id, String name, String slug, double price, int views, String images, int totalSold, double promotionPrice) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.price = price;
        this.views = views;
        this.images = images;
        this.totalSold = totalSold;
        this.promotionPrice = promotionPrice;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public double getPrice() {
        return price;
    }

    public int getViews() {
        return views;
    }

    public String getImages() {
        return images;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public double getPromotionPrice() {
        return promotionPrice;
    }
    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", price=" + price +
                ", views=" + views +
                ", images='" + images + '\'' +
                ", totalSold=" + totalSold +
                ", promotionPrice=" + promotionPrice +
                '}';
    }
}