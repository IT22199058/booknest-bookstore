package com.booknest.servlet;

import com.booknest.model.*;
import com.booknest.repository.Repositories;
import com.booknest.util.Text;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;
import java.util.TreeSet;

/**
 * Book management servlet.
 *
 * CRUD operations (each has its own method below):
 *   CREATE -> createBook()   : add a new book          (admin only)
 *   READ   -> listBooks()    : list / search books     (everyone)
 *             showEditForm() : load one book to edit   (admin only)
 *   UPDATE -> updateBook()   : save changes to a book  (admin only)
 *   DELETE -> deleteBook()   : remove a book           (admin only)
 *
 * doGet() and doPost() only decide WHICH method to call.
 */
@WebServlet("/books")
public class BookServlet extends HttpServlet {

    private static final String FORM_PAGE = "/WEB-INF/views/book-form.jsp";
    private static final String LIST_PAGE = "/WEB-INF/views/books.jsp";

    // =====================================================================
    //  REQUEST ROUTING
    // =====================================================================

    /** Handles GET requests (opening pages). Chooses the method using the "action" parameter. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("new".equals(action)) {
            showAddForm(req, res);          // empty form for a new book
        } else if ("edit".equals(action)) {
            showEditForm(req, res);         // form filled with an existing book
        } else {
            listBooks(req, res);            // default: show all books (READ)
        }
    }

    /** Handles POST requests (submitting forms). Only admins may add, edit or delete books. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (!isAdmin(req)) {
            res.sendError(403);             // 403 = Forbidden
            return;
        }

        String action = req.getParameter("action");
        String id = req.getParameter("id");

        if ("delete".equals(action)) {
            deleteBook(req, res);                       // DELETE
        } else if (id == null || id.isBlank()) {
            createBook(req, res);                       // no id in the form -> new book -> CREATE
        } else {
            updateBook(req, res, id);                   // id present -> existing book -> UPDATE
        }
    }

    // =====================================================================
    //  READ
    // =====================================================================

    /** READ (all): shows the book list. Supports search by title/author and filter by category. */
    private void listBooks(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String q = req.getParameter("q");                   // search text (may be null)
        String category = req.getParameter("category");     // category filter (may be null)

        // Collect all distinct categories for the dropdown (TreeSet = sorted, no duplicates)
        TreeSet<String> categories = new TreeSet<>();
        Repositories.BOOKS.findAll().forEach(b -> categories.add(b.getCategory()));

        // Ask the repository to read books.txt and filter the result
        req.setAttribute("books", Repositories.BOOKS.search(q, category));
        req.setAttribute("categories", categories);
        req.setAttribute("q", q == null ? "" : q);
        req.setAttribute("category", category == null ? "" : category);

        req.getRequestDispatcher(LIST_PAGE).forward(req, res);
    }

    /** READ (one): loads a single book by id and shows it in the edit form. */
    private void showEditForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (!isAdmin(req)) {
            res.sendError(403);
            return;
        }

        Optional<Book> book = Repositories.BOOKS.findById(String.valueOf(req.getParameter("id")));
        if (book.isEmpty()) {
            Text.flash(req, "danger", "Book not found.");
            res.sendRedirect(req.getContextPath() + "/books");
            return;
        }

        req.setAttribute("book", book.get());               // the form pre-fills its fields from this
        req.getRequestDispatcher(FORM_PAGE).forward(req, res);
    }

    // =====================================================================
    //  CREATE
    // =====================================================================

    /** Shows the empty "Add Book" form. */
    private void showAddForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (!isAdmin(req)) {
            res.sendError(403);
            return;
        }
        req.getRequestDispatcher(FORM_PAGE).forward(req, res);
    }

    /** CREATE: reads the form, builds a new book and appends it to books.txt. */
    private void createBook(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String newId = Repositories.BOOKS.nextId("B");      // e.g. B0007
            Book book = readBookFromForm(req, newId);           // validates the input too

            Repositories.BOOKS.create(book);                    // appends one line to books.txt

            Text.flash(req, "success", "Book added.");
            res.sendRedirect(req.getContextPath() + "/books");
        } catch (IllegalArgumentException | NullPointerException e) {
            showInputError(req, res, e, "/books?action=new");   // go back to the empty form
        }
    }

    // =====================================================================
    //  UPDATE
    // =====================================================================

    /** UPDATE: reads the form and replaces the existing book (same id) in books.txt. */
    private void updateBook(HttpServletRequest req, HttpServletResponse res, String id) throws IOException {
        try {
            Book book = readBookFromForm(req, id);              // keeps the same id

            boolean updated = Repositories.BOOKS.update(book);  // rewrites books.txt with the change
            if (updated) {
                Text.flash(req, "success", "Book updated.");
            } else {
                Text.flash(req, "danger", "Book not found.");
            }
            res.sendRedirect(req.getContextPath() + "/books");
        } catch (IllegalArgumentException | NullPointerException e) {
            showInputError(req, res, e, "/books?action=edit&id=" + id);   // go back to the edit form
        }
    }

    // =====================================================================
    //  DELETE
    // =====================================================================

    /** DELETE: removes the book with the given id from books.txt. */
    private void deleteBook(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String id = String.valueOf(req.getParameter("id"));

        boolean removed = Repositories.BOOKS.delete(id);        // rewrites books.txt without this book
        if (removed) {
            Text.flash(req, "success", "Book deleted.");
        } else {
            Text.flash(req, "danger", "Book not found.");
        }
        res.sendRedirect(req.getContextPath() + "/books");
    }

    // =====================================================================
    //  HELPER METHODS
    // =====================================================================

    /**
     * Builds a Book object from the submitted form.
     * Throws IllegalArgumentException (or NumberFormatException) if the input is invalid.
     *
     * POLYMORPHISM: the return type is Book, but the real object is a PrintedBook or an EBook.
     */
    private Book readBookFromForm(HttpServletRequest req, String id) {
        String title = req.getParameter("title").trim();
        String author = req.getParameter("author").trim();
        String category = req.getParameter("category").trim();

        if (title.isEmpty() || author.isEmpty() || category.isEmpty()) {
            throw new IllegalArgumentException("Title, author and category are required.");
        }

        double price = Double.parseDouble(req.getParameter("price"));   // NumberFormatException if not a number
        int stock = Integer.parseInt(req.getParameter("stock"));
        double extra = Double.parseDouble(req.getParameter("extra"));   // weight (printed) or file size MB (e-book)
        if (extra < 0) {
            throw new IllegalArgumentException("Weight / file size cannot be negative.");
        }
        // Book.setPrice() and Book.setStock() also reject negative values (ENCAPSULATION)

        if ("EBOOK".equals(req.getParameter("type"))) {
            return new EBook(id, title, author, category, price, stock, extra);
        }
        return new PrintedBook(id, title, author, category, price, stock, extra);
    }

    /** Shows an error message and sends the user back to the given page. */
    private void showInputError(HttpServletRequest req, HttpServletResponse res, Exception e, String backPath)
            throws IOException {
        String message;
        if (e instanceof NumberFormatException) {
            message = "Price, stock and weight/size must be valid numbers.";
        } else if (e.getMessage() == null) {
            message = "Invalid input.";
        } else {
            message = e.getMessage();
        }
        Text.flash(req, "danger", message);
        res.sendRedirect(req.getContextPath() + backPath);
    }

    /** True if the logged-in user is an admin. */
    private boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null && session.getAttribute("user") instanceof User u && u.isAdmin();
    }
}
