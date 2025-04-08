<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.toystore.model.User" %>
<%@ page import="com.toystore.model.Payment" %>
<%@ page import="com.toystore.model.Toy" %>

<%
    // Get the user from the session
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // Get success message
    String successMessage = (String) request.getAttribute("success");
    if (successMessage == null) {
        successMessage = "Thank you for your purchase!";
    }

    // Get payment and toy details
    Payment payment = (Payment) request.getAttribute("payment");
    Toy toy = (Toy) request.getAttribute("toy");

    // Format for displaying date
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ToyLand - Payment Successful</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 min-h-screen">
    <!-- Header -->
    <header class="bg-purple-600 text-white shadow-md">
        <div class="container mx-auto px-4 py-4">
            <div class="flex justify-between items-center">
                <div class="flex items-center">
                    <h1 class="text-2xl font-bold">ToyLand</h1>
                </div>

                <div class="flex items-center space-x-4">
                    <a href="home" class="hover:text-purple-200 transition-colors duration-300">Home</a>
                    <a href="profile" class="hover:text-purple-200 transition-colors duration-300">Profile</a>
                    <a href="logout" class="bg-purple-800 hover:bg-purple-900 px-4 py-2 rounded-md transition-colors duration-300">Logout</a>
                </div>
            </div>
        </div>
    </header>

    <!-- Main Content -->
    <main class="container mx-auto px-4 py-12">
        <div class="max-w-lg mx-auto bg-white rounded-lg shadow-md p-8 text-center">
            <div class="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-6">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                </svg>
            </div>

            <h2 class="text-3xl font-bold text-gray-800 mb-4">Payment Successful!</h2>

            <p class="text-gray-600 mb-6"><%= successMessage %></p>

            <% if (payment != null) { %>
                <div class="bg-gray-100 p-4 rounded-md mb-6 text-left">
                    <h3 class="font-semibold text-gray-800 mb-3">Order Details</h3>

                    <div class="mb-2">
                        <span class="text-gray-600">Transaction ID:</span>
                        <span class="text-gray-800 ml-2"><%= payment.getTransactionId() %></span>
                    </div>

                    <div class="mb-2">
                        <span class="text-gray-600">Date:</span>
                        <span class="text-gray-800 ml-2"><%= dateFormat.format(payment.getPaymentDate()) %></span>
                    </div>

                    <div class="mb-2">
                        <span class="text-gray-600">Item:</span>
                        <span class="text-gray-800 ml-2"><%= toy != null ? toy.getName() : "Toy" %></span>
                    </div>

                    <div class="mb-2">
                        <span class="text-gray-600">Quantity:</span>
                        <span class="text-gray-800 ml-2"><%= payment.getQuantity() %></span>
                    </div>

                    <div class="mb-2">
                        <span class="text-gray-600">Amount:</span>
                        <span class="text-gray-800 ml-2">$<%= String.format("%.2f", payment.getAmount()) %></span>
                    </div>

                    <div class="mb-2">
                        <span class="text-gray-600">Payment Method:</span>
                        <span class="text-gray-800 ml-2">Credit Card (<%= payment.getCardNumber() %>)</span>
                    </div>
                </div>
            <% } %>

            <div class="bg-gray-100 p-4 rounded-md mb-6 text-left">
                <h3 class="font-semibold text-gray-800 mb-2">Shipping Details</h3>
                <p class="text-gray-600"><%= user.getFullName() != null ? user.getFullName() : user.getUsername() %></p>
                <p class="text-gray-600"><%= user.getAddress() != null ? user.getAddress() : "Address not provided" %></p>
                <p class="text-gray-600"><%= user.getPhone() != null ? user.getPhone() : "Phone not provided" %></p>
            </div>

            <div class="space-y-4">
                <div class="flex flex-col sm:flex-row justify-center space-y-2 sm:space-y-0 sm:space-x-4">
                    <a href="home" class="bg-purple-600 text-white px-6 py-3 rounded-md hover:bg-purple-700 transition-colors duration-300">
                        Continue Shopping
                    </a>

                    <a href="payment?action=history" class="bg-gray-200 text-gray-800 px-6 py-3 rounded-md hover:bg-gray-300 transition-colors duration-300">
                        View Order History
                    </a>
                </div>

                <p class="text-gray-500 text-sm">
                    A confirmation email has been sent to your email address.
                </p>
            </div>
        </div>
    </main>

    <!-- Footer -->
    <footer class="bg-gray-800 text-white py-8 mt-12">
        <div class="container mx-auto px-4">
            <div class="flex flex-col md:flex-row justify-between items-center">
                <div class="mb-4 md:mb-0">
                    <h2 class="text-xl font-bold">ToyLand</h2>
                    <p class="text-gray-400">Where imagination comes to life!</p>
                </div>

                <div class="text-center md:text-right">
                    <p class="text-gray-400">&copy; 2025 ToyLand. All rights reserved.</p>
                </div>
            </div>
        </div>
    </footer>
</body>
</html>