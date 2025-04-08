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
 * Servlet that handles payment processing for toy purchases.
 */
@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles GET requests - displays the payment page for a specific toy
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

        // Forward to the payment page
        request.getRequestDispatcher("payment.jsp").forward(request, response);
    }

    /**
     * Handles POST requests - processes the payment form submission
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
        String quantity = request.getParameter("quantity");

        // Validate parameters
        if (toyId == null || toyId.trim().isEmpty() ||
                quantity == null || quantity.trim().isEmpty()) {
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

        try {
            int qty = Integer.parseInt(quantity);

            // Check if enough quantity is available
            if (qty <= 0 || qty > toy.getQuantity()) {
                request.setAttribute("error", "Invalid quantity");
                request.setAttribute("toy", toy);
                request.getRequestDispatcher("payment.jsp").forward(request, response);
                return;
            }

            // Update toy quantity (simulating a purchase)
            toy.setQuantity(toy.getQuantity() - qty);
            toyController.updateToy(toy);

            // Set success message
            request.setAttribute("success", "Payment successful! Your toy will be shipped soon.");
            request.getRequestDispatcher("payment-success.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid quantity");
            request.setAttribute("toy", toy);
            request.getRequestDispatcher("payment.jsp").forward(request, response);
        }
    }
}