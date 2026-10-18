package com.booknest.repository;

import com.booknest.model.Order;

public class OrderRepository extends FileRepository<Order> {
    public OrderRepository() { super("orders.txt"); }

    @Override
    protected Order fromLine(String line) {
        String[] p = line.split("\\|", -1);   // id|userId|username|bookId|bookTitle|qty|total|status|date
        return new Order(p[0], p[1], p[2], p[3], p[4], Integer.parseInt(p[5]),
                Double.parseDouble(p[6]), p[7], p[8]);
    }

    @Override protected String toLine(Order o) { return o.toLine(); }
    @Override protected String idOf(Order o) { return o.getId(); }
}
