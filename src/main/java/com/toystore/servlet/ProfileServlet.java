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

        String contextPath = getServletContext().getRealPath("/");
        ToyController toyController = new ToyController(contextPath);

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
                email == null || email.trim().isEmpty() ||
                address == null || address.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty()) {

            request.setAttribute("error", "All fields are required");
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

        // Save the updated user
        String contextPath = getServletContext().getRealPath("/");
        UserController userController = new UserController(contextPath);
        boolean success = userController.updateUser(updatedUser);

        if (success) {
            // Update session with the new user information
            session.setAttribute("user", updatedUser);
            request.setAttribute("message", "Profile updated successfully");
        } else {
            request.setAttribute("error", "Failed to update profile");
        }

        // Get user's toys for display
        ToyController toyController = new ToyController(contextPath);
        List<Toy> userToys = toyController.getToysBySeller(currentUser.getId());
        request.setAttribute("userToys", userToys);

        // Forward back to the profile page
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
}