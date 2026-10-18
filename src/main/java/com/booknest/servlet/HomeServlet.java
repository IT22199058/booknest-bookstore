package com.booknest.servlet;

import com.booknest.model.Book;
import com.booknest.repository.Repositories;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.TreeSet;

/** Landing page: hero, search box, categories and the newest books. */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        List<Book> all = Repositories.BOOKS.findAll();
        TreeSet<String> categories = new TreeSet<>();
        all.forEach(b -> categories.add(b.getCategory()));

        int from = Math.max(0, all.size() - 4);             // last 4 added = "new arrivals"
        req.setAttribute("featured", all.subList(from, all.size()));
        req.setAttribute("categories", categories);
        req.setAttribute("bookCount", all.size());
        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, res);
    }
}
