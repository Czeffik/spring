package com.trzewik.spring.interfaces.rest;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Slf4j
public class EmbeddedJetty implements AutoCloseable {
    private Server server;

    public EmbeddedJetty(int port) throws Exception {
        log.debug("Starting server at port {}", port);
        server = new Server(port);

        server.setHandler(
            servletContextHandler(
                webApplicationContext()
            )
        );

        addRuntimeShutdownHook();

        server.start();
        log.info("Server started at port {}", port);
        server.join();
    }

    private ServletContextHandler servletContextHandler(WebApplicationContext context) {
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setErrorHandler(null);

        handler.setContextPath("/");

        // Spring
        DispatcherServlet servlet = new DispatcherServlet(context);
        ServletHolder holder = new ServletHolder("dispatcher", servlet);
        handler.addServlet(holder, "/");
        handler.addEventListener(new ContextLoaderListener(context));

        return handler;
    }

    private WebApplicationContext webApplicationContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation("com.trzewik.spring.interfaces.rest");
        return context;
    }

    private void stop() {
        if (server.isStarted()) {
            server.setStopAtShutdown(true);
            try {
                server.stop();
            } catch (Exception exc) {
                log.error("Error while stopping jetty server: " + exc.getMessage(), exc);
            }
        }
    }

    private void addRuntimeShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    @Override
    public void close() throws Exception {
        stop();
    }
}
