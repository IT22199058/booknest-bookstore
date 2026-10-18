package com.booknest.config;

import com.booknest.BookstoreApplication;
import com.booknest.servlet.*;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /** "/" is handled by Spring, which forwards internally to HomeServlet (the URL stays "/"). */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/home");
    }

    /** Tell Tomcat where the JSP files are (src/main/webapp), independent of the IDE working directory. */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> jspDocumentRoot() {
        return factory -> {
            File root = BookstoreApplication.projectRoot();
            if (root != null) {
                File webapp = new File(root, "src/main/webapp");
                if (webapp.isDirectory()) factory.setDocumentRoot(webapp);
            }
        };
    }

    // ---- explicit registration of our servlets and filter ----
    @Bean public ServletRegistrationBean<HomeServlet> homeServlet() {
        return new ServletRegistrationBean<>(new HomeServlet(), "/home");
    }
    @Bean public ServletRegistrationBean<LoginServlet> loginServlet() {
        return new ServletRegistrationBean<>(new LoginServlet(), "/login", "/logout");
    }
    @Bean public ServletRegistrationBean<UserServlet> userServlet() {
        return new ServletRegistrationBean<>(new UserServlet(), "/register", "/users");
    }
    @Bean public ServletRegistrationBean<BookServlet> bookServlet() {
        return new ServletRegistrationBean<>(new BookServlet(), "/books");
    }
    @Bean public ServletRegistrationBean<OrderServlet> orderServlet() {
        return new ServletRegistrationBean<>(new OrderServlet(), "/orders");
    }
    @Bean public FilterRegistrationBean<AuthFilter> authFilter() {
        FilterRegistrationBean<AuthFilter> bean = new FilterRegistrationBean<>(new AuthFilter());
        bean.addUrlPatterns("/*");
        return bean;
    }
}
