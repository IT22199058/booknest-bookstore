package com.booknest.repository;

import com.booknest.model.Book;
import com.booknest.model.EBook;
import com.booknest.model.PrintedBook;
import java.util.List;

public class BookRepository extends FileRepository<Book> {
    public BookRepository() { super("books.txt"); }

    @Override
    protected Book fromLine(String line) {
        String[] p = line.split("\\|", -1);   // type|id|title|author|category|price|stock|extra
        double price = Double.parseDouble(p[5]);
        int stock = Integer.parseInt(p[6]);
        double extra = Double.parseDouble(p[7]);
        return "EBOOK".equals(p[0])
                ? new EBook(p[1], p[2], p[3], p[4], price, stock, extra)
                : new PrintedBook(p[1], p[2], p[3], p[4], price, stock, extra);
    }

    @Override protected String toLine(Book b) { return b.toLine(); }
    @Override protected String idOf(Book b) { return b.getId(); }

    public List<Book> search(String q, String category) {
        String s = q == null ? "" : q.toLowerCase();
        return findAll().stream()
                .filter(b -> s.isEmpty() || b.getTitle().toLowerCase().contains(s)
                        || b.getAuthor().toLowerCase().contains(s))
                .filter(b -> category == null || category.isEmpty() || b.getCategory().equalsIgnoreCase(category))
                .toList();
    }
}
