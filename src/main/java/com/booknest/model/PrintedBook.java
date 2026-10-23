package com.booknest.model;

public class PrintedBook extends Book {
    private final double weightGrams;

    public PrintedBook(String id, String title, String author, String category, double price, int stock, double weightGrams) {
        super(id, title, author, category, price, stock);
        this.weightGrams = weightGrams;
    }

    @Override public String getFormat() { return "PRINTED"; }

    /** Rs. 350 base + Rs. 100 for every 500 g. */
    @Override public double calculateDeliveryFee() { return 350 + Math.floor(weightGrams / 500) * 100; }

    @Override public String getDetails() { return "Printed, " + (int) weightGrams + " g"; }
    @Override protected String getExtra() { return String.valueOf(weightGrams); }
}
