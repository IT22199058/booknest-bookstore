package com.booknest.repository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Generic file-based repository (Abstraction + Inheritance).
 * Each subclass only says how to turn an object into a text line and back.
 * All CRUD operations read/write a plain .txt file.
 */
public abstract class FileRepository<T> {
    private final Path file;

    protected FileRepository(String fileName) {
        try {
            Path dir = Paths.get(System.getProperty("bookstore.data", "data"));
            Files.createDirectories(dir);
            this.file = dir.resolve(fileName);
            if (!Files.exists(file)) Files.createFile(file);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    protected abstract T fromLine(String line);
    protected abstract String toLine(T item);
    protected abstract String idOf(T item);

    /** READ all */
    public synchronized List<T> findAll() {
        try {
            List<T> items = new ArrayList<>();
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                try { items.add(fromLine(line)); } catch (RuntimeException ignored) { /* skip corrupt line */ }
            }
            return items;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** READ one */
    public synchronized Optional<T> findById(String id) {
        return findAll().stream().filter(i -> idOf(i).equals(id)).findFirst();
    }

    /** CREATE (append a line) */
    public synchronized void create(T item) {
        try {
            Files.writeString(file, toLine(item) + System.lineSeparator(), StandardCharsets.UTF_8,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** UPDATE (rewrite file with the changed record) */
    public synchronized boolean update(T item) {
        List<T> all = findAll();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (idOf(all.get(i)).equals(idOf(item))) { all.set(i, item); found = true; }
        }
        if (found) writeAll(all);
        return found;
    }

    /** DELETE (rewrite file without the record) */
    public synchronized boolean delete(String id) {
        List<T> all = findAll();
        boolean removed = all.removeIf(i -> idOf(i).equals(id));
        if (removed) writeAll(all);
        return removed;
    }

    public synchronized String nextId(String prefix) {
        int max = 0;
        for (T item : findAll()) {
            try { max = Math.max(max, Integer.parseInt(idOf(item).substring(prefix.length()))); }
            catch (RuntimeException ignored) { }
        }
        return prefix + String.format("%04d", max + 1);
    }

    private void writeAll(List<T> items) {
        try {
            List<String> lines = new ArrayList<>();
            for (T i : items) lines.add(toLine(i));
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
