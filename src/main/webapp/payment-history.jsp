<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.toystore.model.Payment" %>
<%@ page import="com.toystore.model.Toy" %>
<%@ page import="com.toystore.model.User" %>

<%
    // Get the user from the session
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // Get the payments from the request
    List<Payment> payments = (List<Payment>) request.getAttribute("payments");
    boolean hasPayments = payments != null && !payments.isEmpty();

    // Format for displaying date
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ToyLand - Payment History</title>
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
    <main class="container mx-auto px-4 py-8">
        <!-- Page Title -->
        <div class="mb-8">
            <h2 class="text-3xl font-bold text-gray-800">Payment History</h2>
            <p class="text-gray-600">View your purchase history</p>
        </div>

        <!-- Admin Options (only shown to admin users) -->
        <% if (user.isAdmin()) { %>
            <div class="mb-8">
                <div class="bg-purple-100 p-4 rounded-lg flex flex-wrap items-center justify-between">
                    <div class="mb-2 md:mb-0">
                        <span class="text-purple-800 font-semibold">Admin View</span>
                    </div>
                    <div class="flex space-x-2">
                        <a href="payment?action=history"
                           class="<%= request.getParameter("view") == null ? "bg-purple-600 text-white" : "bg-gray-200 text-gray-800" %> px-4 py-2 rounded-md hover:bg-purple-700 hover:text-white transition-colors duration-300">
                            My Purchases
                        </a>
                        <a href="payment?action=history&view=all"
                           class="<%= "all".equals(request.getParameter("view")) ? "bg-purple-600 text-white" : "bg-gray-200 text-gray-800" %> px-4 py-2 rounded-md hover:bg-purple-700 hover:text-white transition-colors duration-300">
                            All Purchases
                        </a>
                    </div>
                </div>
            </div>
        <% } %>

        <!-- Payment History Section -->
        <section class="bg-white rounded-lg shadow-md overflow-hidden">
            <% if (!hasPayments) { %>
                <div class="p-8 text-center">
                    <div class="text-gray-400 mb-4">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                        </svg>
                    </div>
                    <h3 class="text-xl font-semibold text-gray-800 mb-2">No Purchase History</h3>
                    <p class="text-gray-600 mb-4">You haven't made any purchases yet.</p>
                    <a href="home" class="text-purple-600 hover:text-purple-800 font-semibold">
                        Start shopping now
                    </a>
                </div>
            <% } else { %>
                <div class="overflow-x-auto">
                    <table class="w-full">
                        <thead class="bg-gray-100">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Date
                                </th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Toy
                                </th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Quantity
                                </th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Price
                                </th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Payment Method
                                </th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Status
                                </th>
                                <% if (user.isAdmin() && "all".equals(request.getParameter("view"))) { %>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        Customer
                                    </th>
                                <% } %>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <% for (Payment payment : payments) {
                                Toy toy = (Toy) request.getAttribute("toy_" + payment.getId());
                                String toyName = toy != null ? toy.getName() : "Unknown Toy";
                            %>
                                <tr class="hover:bg-gray-50">
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm text-gray-900"><%= dateFormat.format(payment.getPaymentDate()) %></div>
                                        <div class="text-xs text-gray-500">ID: <%= payment.getTransactionId() %></div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm font-medium text-gray-900"><%= toyName %></div>
                                        <% if (toy != null) { %>
                                            <div class="text-xs text-gray-500"><%= toy.getCategory() %></div>
                                        <% } %>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm text-gray-900"><%= payment.getQuantity() %></div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm text-gray-900">$<%= String.format("%.2f", payment.getAmount()) %></div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm text-gray-900">Credit Card</div>
                                        <div class="text-xs text-gray-500"><%= payment.getCardNumber() %></div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full
                                            <%= "completed".equals(payment.getStatus()) ? "bg-green-100 text-green-800" :
                                               "refunded".equals(payment.getStatus()) ? "bg-yellow-100 text-yellow-800" :
                                               "bg-red-100 text-red-800" %>">
                                            <%= payment.getStatus() %>
                                        </span>
                                    </td>
                                    <% if (user.isAdmin() && "all".equals(request.getParameter("view"))) {
                                        User paymentUser = (User) request.getAttribute("user_" + payment.getUserId());
                                        String userName = paymentUser != null ? paymentUser.getUsername() : "Unknown User";
                                    %>
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <div class="text-sm text-gray-900"><%= userName %></div>
                                        </td>
                                    <% } %>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
        </section>

        <!-- Return to Shopping Button -->
        <div class="mt-8 text-center">
            <a href="home" class="bg-purple-600 text-white px-6 py-3 rounded-md hover:bg-purple-700 transition-colors duration-300 inline-block">
                Continue Shopping
            </a>
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