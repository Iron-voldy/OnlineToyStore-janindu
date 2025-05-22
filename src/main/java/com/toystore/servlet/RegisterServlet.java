package com.toystore.servlet;

import com.toystore.model.User;
import com.toystore.controller.UserController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.io.File;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserController userController;

    @Override
    public void init() throws ServletException {
        String contextPath = getServletContext().getRealPath("/");
        System.out.println("RegisterServlet initializing...");
        System.out.println("Context path: " + contextPath);

        // Verify the context path exists and is accessible
        File contextDir = new File(contextPath);
        System.out.println("Context directory exists: " + contextDir.exists());
        System.out.println("Context directory writable: " + contextDir.canWrite());

        // Initialize UserController with absolute path
        userController = new UserController(contextPath);
        System.out.println("RegisterServlet initialization complete");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to registration page
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            System.out.println("Processing registration request...");

            // Get form data
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String email = request.getParameter("email");
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            System.out.println("Received registration request for username: " + username);

            // Validate form data
            if (username == null || username.trim().isEmpty() ||
                    password == null || password.trim().isEmpty() ||
                    email == null || email.trim().isEmpty() ||
                    fullName == null || fullName.trim().isEmpty()) {
                System.out.println("Registration failed: Required fields are empty");
                request.setAttribute("error", "Required fields cannot be empty");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                System.out.println("Registration failed: Passwords do not match");
                request.setAttribute("error", "Passwords do not match");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            // Check if username already exists
            if (userController.getUserByUsername(username) != null) {
                System.out.println("Registration failed: Username already exists: " + username);
                request.setAttribute("error", "Username already exists");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            // Create new user
            User newUser = new User();
            newUser.setId(UUID.randomUUID().toString());
            newUser.setUsername(username);
            newUser.setPassword(password); // In a real application, you should hash the password
            newUser.setEmail(email);
            newUser.setFullName(fullName);
            newUser.setPhone(phone);
            newUser.setAddress(address);
            newUser.setUserType("regular"); // Set as regular user by default

            // Save user
            System.out.println("Attempting to save new user: " + username);
            userController.addUser(newUser);
            System.out.println("Successfully saved user: " + username);

            // Set success message and redirect to login
            request.getSession().setAttribute("message", "Registration successful! Please log in.");
            System.out.println("Registration successful for user: " + username);
            response.sendRedirect("index.jsp");
        } catch (Exception e) {
            System.err.println("Error in RegisterServlet.doPost: " + e.getMessage());
            e.printStackTrace();

            // Log additional context information
            System.err.println("Servlet Context Path: " + getServletContext().getRealPath("/"));
            System.err.println("UserController state: " + (userController != null ? "initialized" : "null"));

            request.setAttribute("error", "Error creating account: " + e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}