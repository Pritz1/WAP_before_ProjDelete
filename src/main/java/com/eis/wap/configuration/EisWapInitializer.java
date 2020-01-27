package com.eis.wap.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
 
public class EisWapInitializer implements WebApplicationInitializer {
 
    public void onStartup(ServletContext servletContext) throws ServletException {
 
        AnnotationConfigWebApplicationContext container = new AnnotationConfigWebApplicationContext();
        container.register(EisWapConfiguration.class);
        container.setServletContext(servletContext);
        Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(container));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");
    }
}