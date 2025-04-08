package com.toystore.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.toystore.model.User;

/**
 * Servlet that handles file uploads for toy images.
 */
@WebServlet("/upload")
public class FileUploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif"};

    /**
     * Handles POST requests - processes file uploads
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Check if user is logged in
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }

        // Check if the request is multipart content
        if (!ServletFileUpload.isMultipartContent(request)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not a multipart request");
            return;
        }

        // Configure file upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MAX_FILE_SIZE);

        // Create a temporary directory for file uploads if it doesn't exist
        String tempDir = getServletContext().getRealPath("/WEB-INF/temp");
        File tempDirFile = new File(tempDir);
        if (!tempDirFile.exists()) {
            tempDirFile.mkdirs();
        }
        factory.setRepository(tempDirFile);

        // Create the file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);

        try {
            // Parse the request
            List<FileItem> items = upload.parseRequest(request);

            for (FileItem item : items) {
                if (!item.isFormField() && item.getName() != null && !item.getName().isEmpty()) {
                    // Validate file extension
                    String extension = FilenameUtils.getExtension(item.getName()).toLowerCase();
                    boolean isValidExtension = false;

                    for (String allowedExt : ALLOWED_EXTENSIONS) {
                        if (allowedExt.equals(extension)) {
                            isValidExtension = true;
                            break;
                        }
                    }

                    if (!isValidExtension) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                "Invalid file extension. Allowed: jpg, jpeg, png, gif");
                        return;
                    }

                    // Generate a unique filename to avoid collisions
                    String uniqueFileName = UUID.randomUUID().toString() + "." + extension;

                    // Create the images directory if it doesn't exist
                    String uploadDir = getServletContext().getRealPath("/WEB-INF/data/images");
                    File uploadDirFile = new File(uploadDir);
                    if (!uploadDirFile.exists()) {
                        uploadDirFile.mkdirs();
                    }

                    // Save the file
                    File uploadedFile = new File(uploadDirFile, uniqueFileName);
                    item.write(uploadedFile);

                    // Return the path to the uploaded file
                    response.setContentType("text/plain");
                    response.getWriter().write("images/" + uniqueFileName);
                    return;
                }
            }

            // If no file was found in the request
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No file uploaded");

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error processing upload: " + e.getMessage());
            e.printStackTrace();
        }
    }
}