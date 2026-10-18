package com.booknest.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Redirects anonymous visitors to the login page. Browsing books and login/register stay public. */
@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        boolean isPublic = path.equals("/") || path.equals("/home") || path.equals("/login") || path.equals("/register")
                || path.equals("/error") || path.startsWith("/WEB-INF")
                || (path.equals("/books") && "GET".equals(req.getMethod())
                    && !"new".equals(req.getParameter("action")) && !"edit".equals(req.getParameter("action")));

        HttpSession session = req.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("user") != null;

        if (isPublic || loggedIn) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
