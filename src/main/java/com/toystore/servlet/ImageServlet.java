package com.toystore.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Servlet to serve image files from the application data directory.
 * This allows images to be stored within the application but still accessible to users.
 */
@WebServlet("/images/*")
public class ImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles GET requests - serves the requested image file
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Remove leading slash
        if (pathInfo.startsWith("/")) {
            pathInfo = pathInfo.substring(1);
        }

        // Construct the path to the image file
        String imagePath = getServletContext().getRealPath("/WEB-INF/data/images/") + File.separator + pathInfo;
        File imageFile = new File(imagePath);

        // Check if the file exists and is a file (not a directory)
        if (!imageFile.exists() || !imageFile.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Set the content type based on the file extension
        String contentType = getContentTypeFromFileName(pathInfo);
        response.setContentType(contentType);

        // Set content length
        response.setContentLength((int) imageFile.length());

        // Copy the file to the response output stream
        try (FileInputStream in = new FileInputStream(imageFile);
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * Determines the content type based on the file extension
     * @param fileName The file name
     * @return The content type
     */
    private String getContentTypeFromFileName(String fileName) {
        if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.toLowerCase().endsWith(".png")) {
            return "image/png";
        } else if (fileName.toLowerCase().endsWith(".gif")) {
            return "image/gif";
        } else {
            return "application/octet-stream";
        }
    }
}