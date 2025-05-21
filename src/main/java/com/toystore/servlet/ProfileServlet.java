package com.toystore.servlet;

import com.toystore.controller.ToyController;
import com.toystore.controller.UserController;
import com.toystore.model.Toy;
import com.toystore.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserController userController;
    private ToyController toyController;

    @Override
    public void init() throws ServletException {
        String contextPath = getServletContext().getRealPath("/");
        userController = new UserController(contextPath);
        toyController = new ToyController(contextPath);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Check if user is logged in
        if (user == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // Get toys listed by the user
        List<Toy> userToys = toyController.getToysBySeller(user.getId());
        request.setAttribute("userToys", userToys);

        // Forward to the profile page
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        // Check if user is logged in
        if (currentUser == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // Get updated user information
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate required fields
        if (fullName == null || fullName.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Name and email are required");
            request.getRequestDispatcher("profile.jsp").forward(request, response);
            return;
        }

        // Update user information
        User updatedUser = new User(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getPassword(),  // Keep the current password if not changing
                email,
                fullName,
                address,
                phone,
                currentUser.getUserType()
        );

        // Update password if provided
        if (password != null && !password.trim().isEmpty()) {
            if (!password.equals(confirmPassword)) {
                request.setAttribute("error", "Passwords do not match");
                request.getRequestDispatcher("profile.jsp").forward(request, response);
                return;
            }
            updatedUser.setPassword(password);
        }

        try {
            // Save the updated user
            userController.updateUser(updatedUser);

            // Update session with the new user information
            session.setAttribute("user", updatedUser);
            request.setAttribute("message", "Profile updated successfully");
        } catch (IOException e) {
            request.setAttribute("error", "Failed to update profile: " + e.getMessage());
        }

        // Get user's toys for display
        List<Toy> userToys = toyController.getToysBySeller(currentUser.getId());
        request.setAttribute("userToys", userToys);

        // Forward back to the profile page
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
}