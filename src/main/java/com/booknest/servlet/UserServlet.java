package com.booknest.servlet;

import com.booknest.model.CustomerUser;
import com.booknest.model.User;
import com.booknest.repository.Repositories;
import com.booknest.util.Text;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

/**
 * User management: Create (register), Read (list/search/profile), Update (edit profile), Delete (admin).
 */
@WebServlet({"/register", "/users"})
public class UserServlet extends HttpServlet {
    private static final String FORM = "/WEB-INF/views/user-form.jsp";

    private User current(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s == null ? null : (User) s.getAttribute("user");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if ("/register".equals(req.getServletPath())) {
            req.getRequestDispatcher(FORM).forward(req, res);
            return;
        }
        User me = current(req);
        String action = req.getParameter("action");

        if ("edit".equals(action)) {
            String id = req.getParameter("id");
            Optional<User> target = Repositories.USERS.findById(id == null ? "" : id);
            if (target.isEmpty() || (!me.isAdmin() && !me.getId().equals(id))) {
                Text.flash(req, "danger", "User not found or access denied.");
                res.sendRedirect(req.getContextPath() + "/books");
                return;
            }
            req.setAttribute("target", target.get());
            req.getRequestDispatcher(FORM).forward(req, res);
            return;
        }

        if (!me.isAdmin()) {   // customers go straight to their own profile
            res.sendRedirect(req.getContextPath() + "/users?action=edit&id=" + me.getId());
            return;
        }
        String q = req.getParameter("q");
        req.setAttribute("q", q == null ? "" : q);
        req.setAttribute("users", q == null || q.isBlank() ? Repositories.USERS.findAll() : Repositories.USERS.search(q));
        req.getRequestDispatcher("/WEB-INF/views/users.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if ("/register".equals(req.getServletPath())) { register(req, res); return; }

        User me = current(req);
        String action = req.getParameter("action");
        String id = req.getParameter("id");

        if ("delete".equals(action)) {
            if (!me.isAdmin()) { res.sendError(403); return; }
            if (me.getId().equals(id)) {
                Text.flash(req, "danger", "You cannot delete your own account.");
            } else {
                Repositories.USERS.delete(id);
                Text.flash(req, "success", "User deleted.");
            }
            res.sendRedirect(req.getContextPath() + "/users");
            return;
        }

        // update
        Optional<User> target = Repositories.USERS.findById(id == null ? "" : id);
        if (target.isEmpty() || (!me.isAdmin() && !me.getId().equals(id))) { res.sendError(403); return; }
        User u = target.get();
        String err = validateProfile(req);
        String pw = req.getParameter("password");
        if (err == null && pw != null && !pw.isEmpty() && pw.length() < 6) err = "Password must be at least 6 characters.";
        if (err != null) {
            req.setAttribute("error", err);
            req.setAttribute("target", u);
            req.getRequestDispatcher(FORM).forward(req, res);
            return;
        }
        u.setFullName(req.getParameter("fullName").trim());
        u.setEmail(req.getParameter("email").trim());
        u.setPhone(req.getParameter("phone") == null ? "" : req.getParameter("phone").trim());
        if (pw != null && !pw.isEmpty()) u.setPasswordHash(Text.hash(pw));
        Repositories.USERS.update(u);
        if (me.getId().equals(u.getId())) req.getSession().setAttribute("user", u);
        Text.flash(req, "success", "Profile updated.");
        res.sendRedirect(req.getContextPath() + (me.isAdmin() ? "/users" : "/books"));
    }

    private void register(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String username = req.getParameter("username") == null ? "" : req.getParameter("username").trim();
        String pw = req.getParameter("password") == null ? "" : req.getParameter("password");
        String err = validateProfile(req);
        if (err == null && !username.matches("[A-Za-z0-9_]{3,20}")) err = "Username must be 3-20 letters, digits or _.";
        if (err == null && pw.length() < 6) err = "Password must be at least 6 characters.";
        if (err == null && Repositories.USERS.findByUsername(username).isPresent()) err = "Username already taken.";
        if (err != null) {
            req.setAttribute("error", err);
            req.getRequestDispatcher(FORM).forward(req, res);
            return;
        }
        User u = new CustomerUser(Repositories.USERS.nextId("U"), username, Text.hash(pw),
                req.getParameter("fullName").trim(), req.getParameter("email").trim(),
                req.getParameter("phone") == null ? "" : req.getParameter("phone").trim());
        Repositories.USERS.create(u);
        Text.flash(req, "success", "Account created. Please log in.");
        res.sendRedirect(req.getContextPath() + "/login");
    }

    private String validateProfile(HttpServletRequest req) {
        String name = req.getParameter("fullName");
        String email = req.getParameter("email");
        if (name == null || name.isBlank()) return "Full name is required.";
        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) return "Enter a valid email address.";
        return null;
    }
}
