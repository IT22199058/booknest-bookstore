package com.booknest.model;

import com.booknest.util.Text;

/** Abstract base class for every kind of book. */
public abstract class Book {
    private final String id;
    private String title;
    private String author;
    private String category;
    private double price;
    private int stock;

    protected Book(String id, String title, String author, String category, double price, int stock) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        setPrice(price);
        setStock(stock);
    }

    // ---- Abstraction: each book type defines these itself (Polymorphism) ----
    public abstract String getFormat();
    public abstract double calculateDeliveryFee();
    public abstract String getDetails();
    protected abstract String getExtra();

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public boolean isInStock() { return stock > 0; }
    /** Weight in grams (printed) or file size in MB (e-book); used by the edit form. */
    public double getExtraValue() { return Double.parseDouble(getExtra()); }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        this.price = price;
    }

    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative.");
        this.stock = stock;
    }

    public String toLine() {
        return String.join("|", getFormat(), id, Text.clean(title), Text.clean(author),
                Text.clean(category), String.valueOf(price), String.valueOf(stock), getExtra());
    }
}
