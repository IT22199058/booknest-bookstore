package com.booknest.model;

import com.booknest.util.Text;

/** Abstract base class for all users (Encapsulation + Abstraction). */
public abstract class User {
    private final String id;
    private final String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private String phone;

    protected User(String id, String username, String passwordHash, String fullName, String email, String phone) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    // ---- abstract behaviour, implemented differently by subclasses (Polymorphism) ----
    public abstract String getRole();
    public abstract double getDiscountRate();
    public abstract boolean isAdmin();

    // ---- getters / setters (Encapsulation / Information hiding) ----
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public boolean checkPassword(String rawPassword) {
        return passwordHash.equals(Text.hash(rawPassword));
    }

    /** Line stored in users.txt */
    public String toLine() {
        return String.join("|", getRole(), id, Text.clean(username), passwordHash,
                Text.clean(fullName), Text.clean(email), Text.clean(phone));
    }
}
