package com.booknest.model;

public class AdminUser extends User {
    public AdminUser(String id, String username, String passwordHash, String fullName, String email, String phone) {
        super(id, username, passwordHash, fullName, email, phone);
    }
    @Override public String getRole() { return "ADMIN"; }
    @Override public double getDiscountRate() { return 0.0; }
    @Override public boolean isAdmin() { return true; }
}
