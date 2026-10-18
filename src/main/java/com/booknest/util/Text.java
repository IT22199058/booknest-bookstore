package com.booknest.util;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/** Small helpers: sanitising text for the pipe-separated files, password hashing, flash messages. */
public final class Text {
    private Text() {}

    /** Removes characters that would break the "a|b|c" file format. */
    public static String clean(String s) {
        return s == null ? "" : s.replace("|", "/").replace("\r", " ").replace("\n", " ").trim();
    }

    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            StringBuilder sb = new StringBuilder();
            for (byte b : md.digest(password.getBytes(StandardCharsets.UTF_8))) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void flash(HttpServletRequest req, String type, String msg) {
        req.getSession().setAttribute("msgType", type);
        req.getSession().setAttribute("msg", msg);
    }
}
