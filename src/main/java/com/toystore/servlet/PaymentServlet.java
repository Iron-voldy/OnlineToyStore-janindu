package com.toystore.servlet;

import com.toystore.controller.PaymentController;
import com.toystore.controller.ToyController;
import com.toystore.model.Payment;
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

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
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

        String toyId = request.getParameter("id");
        String action = request.getParameter("action");
        String contextPath = getServletContext().getRealPath("/");

        // Show payment history if no toy ID is provided or action is "history"
        if ((toyId == null || toyId.trim().isEmpty()) || "history".equals(action)) {
            PaymentController paymentController = new PaymentController(contextPath);
            List<Payment> payments;

            // If admin user, show all payments. Otherwise, show only user's payments
            if (user.isAdmin() && "all".equals(request.getParameter("view"))) {
                payments = paymentController.getAllPayments();
            } else {
                payments = paymentController.getPaymentsByUser(user.getId());
            }

            // Get toy details for each payment
            ToyController toyController = new ToyController(contextPath);
            for (Payment payment : payments) {
                Toy toy = toyController.getToyById(payment.getToyId());
                if (toy != null) {
                    request.setAttribute("toy_" + payment.getId(), toy);
                }
            }

            request.setAttribute("payments", payments);
            request.getRequestDispatcher("payment-history.jsp").forward(request, response);
            return;
        }

        // Show payment page for a specific toy
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
        String cardNumber = request.getParameter("cardNumber");
        String cardName = request.getParameter("cardName");
        String expiryDate = request.getParameter("expiryDate");
        String cvv = request.getParameter("cvv");

        // Validate parameters
        if (toyId == null || toyId.trim().isEmpty() ||
                quantity == null || quantity.trim().isEmpty() ||
                cardNumber == null || cardNumber.trim().isEmpty() ||
                cardName == null || cardName.trim().isEmpty() ||
                expiryDate == null || expiryDate.trim().isEmpty() ||
                cvv == null || cvv.trim().isEmpty()) {

            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher("payment.jsp").forward(request, response);
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

            // Process payment
            PaymentController paymentController = new PaymentController(contextPath);
            Payment payment = paymentController.processPayment(user, toy, qty, cardNumber);

            if (payment != null) {
                // Set success message and payment details
                request.setAttribute("success", "Payment successful! Your toy will be shipped soon.");
                request.setAttribute("payment", payment);
                request.setAttribute("toy", toy);
                request.getRequestDispatcher("payment-success.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Payment processing failed. Please try again.");
                request.setAttribute("toy", toy);
                request.getRequestDispatcher("payment.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid quantity");
            request.setAttribute("toy", toy);
            request.getRequestDispatcher("payment.jsp").forward(request, response);
        }
    }
}