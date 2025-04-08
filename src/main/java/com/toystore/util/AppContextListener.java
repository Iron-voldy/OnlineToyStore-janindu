package com.toystore.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        // Set the ServletContext for file operations
        FileHandler.setServletContext(context);

        // Initialize required directories
        DirectoryInitializer.initializeDirectories(context);

        System.out.println("Application initialized: ServletContext set for file operations");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup resources if needed
        System.out.println("Application shutting down");
    }
}