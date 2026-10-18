package com.booknest.model;

import com.booknest.util.Text;

public class Order {
    private final String id;
    private final String userId;
    private final String username;
    private final String bookId;
    private final String bookTitle;
    private final int quantity;
    private final double total;
    private String status;   // PLACED, SHIPPED, DELIVERED, CANCELLED
    private final String date;

    public Order(String id, String userId, String username, String bookId, String bookTitle,
                 int quantity, double total, String status, String date) {
        this.id = id; this.userId = userId; this.username = username; this.bookId = bookId;
        this.bookTitle = bookTitle; this.quantity = quantity; this.total = total;
        this.status = status; this.date = date;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getBookId() { return bookId; }
    public String getBookTitle() { return bookTitle; }
    public int getQuantity() { return quantity; }
    public double getTotal() { return total; }
    public String getStatus() { return status; }
    public String getDate() { return date; }
    public void setStatus(String status) { this.status = status; }

    public String toLine() {
        return String.join("|", id, userId, Text.clean(username), bookId, Text.clean(bookTitle),
                String.valueOf(quantity), String.valueOf(total), status, date);
    }
}
