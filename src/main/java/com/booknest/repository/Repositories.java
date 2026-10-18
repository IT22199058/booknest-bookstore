package com.booknest.repository;

import com.booknest.model.*;
import com.booknest.util.Text;

/** Single shared instances + first-run sample data. */
public final class Repositories {
    public static final UserRepository USERS;
    public static final BookRepository BOOKS;
    public static final OrderRepository ORDERS;

    static {
        USERS = new UserRepository();
        BOOKS = new BookRepository();
        ORDERS = new OrderRepository();
        seed();
    }

    private Repositories() {}

    private static void seed() {
        if (USERS.findAll().isEmpty()) {
            USERS.create(new AdminUser("U0001", "admin", Text.hash("admin123"), "Store Admin", "admin@booknest.lk", "0112345678"));
            USERS.create(new CustomerUser("U0002", "kasun", Text.hash("kasun123"), "Kasun Perera", "kasun@mail.com", "0771234567"));
        }
        if (BOOKS.findAll().isEmpty()) {
            BOOKS.create(new PrintedBook("B0001", "Clean Code", "Robert C. Martin", "Programming", 6500, 12, 720));
            BOOKS.create(new PrintedBook("B0002", "Head First Java", "Kathy Sierra", "Programming", 7200, 8, 950));
            BOOKS.create(new EBook("B0003", "Effective Java", "Joshua Bloch", "Programming", 3900, 100, 6.5));
            BOOKS.create(new PrintedBook("B0004", "Madol Duwa", "Martin Wickramasinghe", "Fiction", 1200, 25, 300));
            BOOKS.create(new EBook("B0005", "Atomic Habits", "James Clear", "Self-Help", 2500, 100, 3.2));
            BOOKS.create(new PrintedBook("B0006", "The Hobbit", "J.R.R. Tolkien", "Fantasy", 3400, 15, 400));
        }
    }
}
