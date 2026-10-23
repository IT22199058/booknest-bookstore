package com.booknest.model;

public class PrintedBook extends Book {
    private final double weightGrams;

    public PrintedBook(String id, String title, String author, String category, double price, int stock, double weightGrams) {
        super(id, title, author, category, price, stock);
        this.weightGrams = weightGrams;
    }

    @Override public String getFormat() { return "PRINTED"; }

   
}
