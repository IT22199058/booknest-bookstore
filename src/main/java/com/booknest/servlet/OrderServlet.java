package com.booknest.servlet;

import com.booknest.model.*;
import com.booknest.repository.Repositories;
import com.booknest.util.Text;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Order management: Create (place order), Read (my orders / all orders),
 * Update (cancel or change status), Delete (admin removes a record).
 */
@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private User me(HttpServletRequest req) { return (User) req.getSession().getAttribute("user"); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User me = me(req);
        if ("new".equals(req.getParameter("action"))) {
            Optional<Book> b = Repositories.BOOKS.findById(String.valueOf(req.getParameter("bookId")));
            if (b.isEmpty()) {
                Text.flash(req, "danger", "Book not found.");
                res.sendRedirect(req.getContextPath() + "/books");
                return;
            }
            req.setAttribute("book", b.get());
            req.getRequestDispatcher("/WEB-INF/views/order-form.jsp").forward(req, res);
            return;
        }
        List<Order> orders = Repositories.ORDERS.findAll().stream()
                .filter(o -> me.isAdmin() || o.getUserId().equals(me.getId())).toList();
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User me = me(req);
        String action = req.getParameter("action");
        String id = req.getParameter("id");
        String back = req.getContextPath() + "/orders";

        switch (String.valueOf(action)) {
            case "create" -> {
                Optional<Book> ob = Repositories.BOOKS.findById(String.valueOf(req.getParameter("bookId")));
                int qty;
                try { qty = Integer.parseInt(req.getParameter("quantity")); } catch (NumberFormatException e) { qty = 0; }
                if (ob.isEmpty() || qty < 1 || qty > ob.get().getStock()) {
                    Text.flash(req, "danger", "Invalid quantity or not enough stock.");
                    res.sendRedirect(req.getContextPath() + "/books");
                    return;
                }
                Book b = ob.get();
                // Polymorphism: delivery fee and discount depend on the concrete book / user type
                double subtotal = b.getPrice() * qty;
                double total = subtotal - subtotal * me.getDiscountRate() + b.calculateDeliveryFee();
                b.setStock(b.getStock() - qty);
                Repositories.BOOKS.update(b);
                Repositories.ORDERS.create(new Order(Repositories.ORDERS.nextId("O"), me.getId(), me.getUsername(),
                        b.getId(), b.getTitle(), qty, total, "PLACED", LocalDate.now().toString()));
                Text.flash(req, "success", "Order placed successfully.");
            }
            case "cancel" -> {
                Optional<Order> oo = Repositories.ORDERS.findById(String.valueOf(id));
                if (oo.isPresent() && (me.isAdmin() || oo.get().getUserId().equals(me.getId()))
                        && "PLACED".equals(oo.get().getStatus())) {
                    Order o = oo.get();
                    o.setStatus("CANCELLED");
                    Repositories.ORDERS.update(o);
                    Repositories.BOOKS.findById(o.getBookId()).ifPresent(b -> {   // put stock back
                        b.setStock(b.getStock() + o.getQuantity());
                        Repositories.BOOKS.update(b);
                    });
                    Text.flash(req, "success", "Order cancelled.");
                } else {
                    Text.flash(req, "danger", "Only your own PLACED orders can be cancelled.");
                }
            }
            case "status" -> {
                if (!me.isAdmin()) { res.sendError(403); return; }
                String st = String.valueOf(req.getParameter("status"));
                Optional<Order> oo = Repositories.ORDERS.findById(String.valueOf(id));
                if (oo.isPresent() && List.of("PLACED", "SHIPPED", "DELIVERED").contains(st)
                        && !"CANCELLED".equals(oo.get().getStatus())) {
                    oo.get().setStatus(st);
                    Repositories.ORDERS.update(oo.get());
                    Text.flash(req, "success", "Order status updated.");
                } else {
                    Text.flash(req, "danger", "Could not update status.");
                }
            }
            case "delete" -> {
                if (!me.isAdmin()) { res.sendError(403); return; }
                Repositories.ORDERS.delete(String.valueOf(id));
                Text.flash(req, "success", "Order record deleted.");
            }
            default -> res.sendError(400);
        }
        if (!res.isCommitted()) res.sendRedirect(back);
    }
}
