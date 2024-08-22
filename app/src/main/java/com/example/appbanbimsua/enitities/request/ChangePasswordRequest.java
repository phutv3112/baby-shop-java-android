package com.example.appbanbimsua.enitities.request;

public class ChangePasswordRequest {
    private String old_password;
    private String new_password;

    // Constructor, getters, and setters
    public ChangePasswordRequest(String oldPassword, String newPassword) {
        this.old_password = oldPassword;
        this.new_password = newPassword;
    }

    public String getOldPassword() {
        return old_password;
    }

    public void setOldPassword(String oldPassword) {
        this.old_password = oldPassword;
    }

    public String getNewPassword() {
        return new_password;
    }

    public void setNewPassword(String newPassword) {
        this.new_password = newPassword;
    }
}
