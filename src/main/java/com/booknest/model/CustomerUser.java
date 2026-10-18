package com.booknest.model;

public class CustomerUser extends User {
    public CustomerUser(String id, String username, String passwordHash, String fullName, String email, String phone) {
        super(id, username, passwordHash, fullName, email, phone);
    }
    @Override public String getRole() { return "CUSTOMER"; }
    /** Registered customers get 5% off. */
    @Override public double getDiscountRate() { return 0.05; }
    @Override public boolean isAdmin() { return false; }
}
