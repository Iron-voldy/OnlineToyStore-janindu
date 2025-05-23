package com.toystore.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

public class DirectoryInitializer {

    public static void initializeDirectories(ServletContext context) {
        // Main data directory
        String dataDir = context.getRealPath("/WEB-INF/data");
        createDirectory(dataDir);

        // Images directory
        String imagesDir = dataDir + File.separator + "images";
        createDirectory(imagesDir);

        // Temporary directory
        String tempDir = context.getRealPath("/WEB-INF/temp");
        createDirectory(tempDir);

        System.out.println("Application directories initialized successfully");
    }

    private static void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            System.out.println("Created directory: " + path + " - Success: " + created);
        }
    }
}