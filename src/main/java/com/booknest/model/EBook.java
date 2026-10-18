package com.booknest.model;

public class EBook extends Book {
    private final double fileSizeMb;

    public EBook(String id, String title, String author, String category, double price, int stock, double fileSizeMb) {
        super(id, title, author, category, price, stock);
        this.fileSizeMb = fileSizeMb;
    }

    @Override public String getFormat() { return "EBOOK"; }

    /** Digital delivery is free. */
    @Override public double calculateDeliveryFee() { return 0; }

    @Override public String getDetails() { return "Digital download, " + fileSizeMb + " MB"; }
    @Override protected String getExtra() { return String.valueOf(fileSizeMb); }
}
