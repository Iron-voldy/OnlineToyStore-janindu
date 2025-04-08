package com.toystore.servlet;

import com.toystore.controller.ToyController;
import com.toystore.model.Toy;
import com.toystore.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet that handles feedback submission for toys.
 */
@WebServlet("/feedback")
public class FeedbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles GET requests - displays the feedback form for a specific toy
     */
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

        String toyId = request.getParameter("id");

        // Check if toy ID is provided
        if (toyId == null || toyId.trim().isEmpty()) {
            response.sendRedirect("home");
            return;
        }

        // Get the toy information
        String contextPath = getServletContext().getRealPath("/");
        ToyController toyController = new ToyController(contextPath);
        Toy toy = toyController.getToyById(toyId);

        // Check if toy exists
        if (toy == null) {
            response.sendRedirect("home");
            return;
        }

        // Set the toy as an attribute
        request.setAttribute("toy", toy);

        // Forward to the feedback page
        request.getRequestDispatcher("feedback.jsp").forward(request, response);
    }

    /**
     * Handles POST requests - processes the feedback form submission
     * Note: In a real application, this would save the feedback to a file or database
     * For this project, we'll just display a success message
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Check if user is logged in
        if (user == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String toyId = request.getParameter("toyId");
        String rating = request.getParameter("rating");
        String comment = request.getParameter("comment");

        // Validate parameters
        if (toyId == null || toyId.trim().isEmpty() ||
                rating == null || rating.trim().isEmpty()) {
            response.sendRedirect("home");
            return;
        }

        // Get the toy information
        String contextPath = getServletContext().getRealPath("/");
        ToyController toyController = new ToyController(contextPath);
        Toy toy = toyController.getToyById(toyId);

        // Check if toy exists
        if (toy == null) {
            response.sendRedirect("home");
            return;
        }

        // Set success message (in a real application, we would save the feedback)
        request.setAttribute("success", "Thank you for your feedback!");
        request.setAttribute("toy", toy);

        // Forward back to the feedback page with success message
        request.getRequestDispatcher("feedback.jsp").forward(request, response);
    }
}