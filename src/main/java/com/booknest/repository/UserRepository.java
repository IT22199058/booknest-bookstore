package com.booknest.repository;

import com.booknest.model.AdminUser;
import com.booknest.model.CustomerUser;
import com.booknest.model.User;
import java.util.List;
import java.util.Optional;

public class UserRepository extends FileRepository<User> {
    public UserRepository() { super("users.txt"); }

    @Override
    protected User fromLine(String line) {
        String[] p = line.split("\\|", -1);   // role|id|username|hash|fullName|email|phone
        return "ADMIN".equals(p[0])
                ? new AdminUser(p[1], p[2], p[3], p[4], p[5], p[6])
                : new CustomerUser(p[1], p[2], p[3], p[4], p[5], p[6]);
    }

    @Override protected String toLine(User u) { return u.toLine(); }
    @Override protected String idOf(User u) { return u.getId(); }

    public Optional<User> findByUsername(String username) {
        return findAll().stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    public List<User> search(String q) {
        String s = q.toLowerCase();
        return findAll().stream().filter(u -> u.getUsername().toLowerCase().contains(s)
                || u.getFullName().toLowerCase().contains(s) || u.getId().toLowerCase().contains(s)).toList();
    }
}
