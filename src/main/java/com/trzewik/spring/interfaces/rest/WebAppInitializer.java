package com.trzewik.spring.interfaces.rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class WebAppInitializer implements WebApplicationInitializer  {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(RestConfiguration.class);
        context.setServletContext(servletContext);

        ServletRegistration.Dynamic appServlet = servletContext.addServlet(
            "dispatcher",
            new DispatcherServlet(context)
        );
        appServlet.setLoadOnStartup(1);
        appServlet.addMapping("/");
    }

    /** to jest gowno
     * extends AbstractAnnotationConfigDispatcherServletInitializer
     *     @Override
     *     protected Class<?>[] getRootConfigClasses() {
     *         return null;
     *     }
     *
     *     @Override
     *     protected Class<?>[] getServletConfigClasses() {
     *         return new Class[] { RestConfiguration.class };
     *     }
     *
     *     @Override
     *     protected String[] getServletMappings() {
     *         return new String[] { "/" };
     *     }
     */
}
