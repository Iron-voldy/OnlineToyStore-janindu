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
import java.util.UUID;

/**
 * Servlet that handles requests for selling toys (adding, updating, removing).
 */
@WebServlet("/sell")
public class SellToyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles GET requests - displays the sell page or edit form
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

        String action = request.getParameter("action");
        String toyId = request.getParameter("id");

        if ("edit".equals(action) && toyId != null) {
            // Edit existing toy
            String contextPath = getServletContext().getRealPath("/");
            ToyController toyController = new ToyController(contextPath);
            Toy toy = toyController.getToyById(toyId);

            // Check if toy exists and belongs to the current user
            if (toy != null && toy.getSeller().equals(user.getId())) {
                request.setAttribute("toy", toy);
                request.setAttribute("editing", true);
            }
        } else if ("delete".equals(action) && toyId != null) {
            // Delete toy
            String contextPath = getServletContext().getRealPath("/");
            ToyController toyController = new ToyController(contextPath);
            Toy toy = toyController.getToyById(toyId);

            // Check if toy exists and belongs to the current user
            if (toy != null && toy.getSeller().equals(user.getId())) {
                toyController.removeToy(toyId);
                response.sendRedirect("profile");
                return;
            }
        }

        // Show user's toys in the sell page
        String contextPath = getServletContext().getRealPath("/");
        ToyController toyController = new ToyController(contextPath);
        request.setAttribute("userToys", toyController.getToysBySeller(user.getId()));

        // Forward to the sell page
        request.getRequestDispatcher("sell.jsp").forward(request, response);
    }

    /**
     * Handles POST requests - processes the form submission for adding/updating toys
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

        // Get toy information from the form
        String toyId = request.getParameter("id");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");
        String ageRangeStr = request.getParameter("ageRange");
        String category = request.getParameter("category");
        String quantityStr = request.getParameter("quantity");
        String imageUrl = request.getParameter("imageUrl");

        // Validate required fields
        if (name == null || name.trim().isEmpty() ||
                priceStr == null || priceStr.trim().isEmpty() ||
                ageRangeStr == null || ageRangeStr.trim().isEmpty() ||
                category == null || category.trim().isEmpty() ||
                quantityStr == null || quantityStr.trim().isEmpty()) {

            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher("sell.jsp").forward(request, response);
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int ageRange = Integer.parseInt(ageRangeStr);
            int quantity = Integer.parseInt(quantityStr);

            // Create or update toy
            Toy toy;
            boolean isNewToy = toyId == null || toyId.trim().isEmpty();

            if (isNewToy) {
                toy = new Toy();
                toy.setId(UUID.randomUUID().toString());
                toy.setSeller(user.getId());
            } else {
                String contextPath = getServletContext().getRealPath("/");
                ToyController toyController = new ToyController(contextPath);
                toy = toyController.getToyById(toyId);

                // Check if toy exists and belongs to the current user
                if (toy == null || !toy.getSeller().equals(user.getId())) {
                    response.sendRedirect("sell");
                    return;
                }
            }

            // Set toy properties
            toy.setName(name);
            toy.setDescription(description);
            toy.setPrice(price);
            toy.setAgeRange(ageRange);
            toy.setCategory(category);
            toy.setQuantity(quantity);

            // Set image URL if provided, or use a default image
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                // If it doesn't start with 'images/', assume it's a path that needs to be prefixed
                if (!imageUrl.startsWith("images/") && !imageUrl.startsWith("/api/placeholder")) {
                    imageUrl = "images/" + imageUrl;
                }
                toy.setImageUrl(imageUrl);
            } else {
                toy.setImageUrl("/api/placeholder/300/300");
            }

            // Save the toy
            String contextPath = getServletContext().getRealPath("/");
            ToyController toyController = new ToyController(contextPath);

            boolean success;
            if (isNewToy) {
                success = toyController.addToy(toy);
            } else {
                success = toyController.updateToy(toy);
            }

            if (success) {
                response.sendRedirect("profile");
            } else {
                request.setAttribute("error", "Failed to save toy");
                request.getRequestDispatcher("sell.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid number format");
            request.getRequestDispatcher("sell.jsp").forward(request, response);
        }
    }
}