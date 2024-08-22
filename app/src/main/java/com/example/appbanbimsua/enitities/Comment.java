package com.example.appbanbimsua.enitities;

import java.io.Serializable;
import java.security.Timestamp;

public class Comment implements Serializable {
    private long id;
    private String productId;
    private String content;
    private long userId;
    private String full_name;
//    private String product;
    private String createdAt;

    public Comment(long id, String productId, String content, long userId, String full_name, String createdAt) {
        this.id = id;
        this.productId = productId;
        this.content = content;
        this.userId = userId;
        this.full_name = full_name;
        this.createdAt = createdAt;
    }

    public Comment() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}
