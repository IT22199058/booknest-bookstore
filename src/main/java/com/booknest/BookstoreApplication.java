package com.booknest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.File;

@SpringBootApplication
public class BookstoreApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        // Always keep the data files inside the project folder, whatever the IDE working directory is
        File root = projectRoot();
        if (root != null) System.setProperty("bookstore.data", new File(root, "data").getAbsolutePath());
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BookstoreApplication.class);
    }

    /** Finds the project folder from the location of the compiled classes (target/classes). */
    public static File projectRoot() {
        try {
            File loc = new File(BookstoreApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (loc.getPath().replace('\\', '/').endsWith("target/classes")) {
                return loc.getParentFile().getParentFile();
            }
        } catch (Exception ignored) { }
        return null;
    }
}