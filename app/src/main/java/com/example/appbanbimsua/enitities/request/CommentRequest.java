package com.example.appbanbimsua.enitities.request;

public class CommentRequest {
    private String productId;
    private String content;
    private String userId;

    // Constructor, getters, and setters
    public CommentRequest(String productId, String content, String userId) {
        this.productId = productId;
        this.content = content;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
