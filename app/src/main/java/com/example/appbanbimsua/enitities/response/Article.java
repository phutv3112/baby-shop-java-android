package com.example.appbanbimsua.enitities.response;

import com.example.appbanbimsua.enitities.Comment;

import java.io.Serializable;
import java.util.List;

public class Article implements Serializable {
    private int id;
    private String title;
    private String content;
    private String description;
    private String slug;
    private String thumbnail;
    private String createdAt;
    private String modifiedAt;
    private String createdBy;
    private String modifiedBy;
    private long publishedAt;
    private int status;
    private List<Comment> comments;

    // Constructor
    public Article(int id, String title, String content, String description, String slug, String thumbnail, 
                   String createdAt, String modifiedAt, String createdBy, String modifiedBy, 
                   long publishedAt, int status, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.description = description;
        this.slug = slug;
        this.thumbnail = thumbnail;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.publishedAt = publishedAt;
        this.status = status;
        this.comments = comments;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public long getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(long publishedAt) {
        this.publishedAt = publishedAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}