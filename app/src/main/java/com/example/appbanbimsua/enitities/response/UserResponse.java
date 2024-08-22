package com.example.appbanbimsua.enitities.response;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class UserResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("address")
    private String address;

    @SerializedName("phone")
    private String phone;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("roles")
    private List<String> roles;

    // Getters and setters with null checks
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName != null ? fullName : "";
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email != null ? email : "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address != null ? address : "";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone != null ? phone : "";
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar != null ? avatar : "";
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getRoles() {
        return roles != null ? roles : new ArrayList<>();
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id + "," +
                "\"fullName\":\"" + getFullName() + "\"," +
                "\"email\":\"" + getEmail() + "\"," +
                "\"address\":\"" + getAddress() + "\"," +
                "\"phone\":\"" + getPhone() + "\"," +
                "\"avatar\":\"" + getAvatar() + "\"," +
                "\"roles\":" + getRoles() +
                "}";
    }
}
