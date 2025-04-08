<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.toystore.model.Toy" %>
<%@ page import="com.toystore.model.User" %>

<%
    // Get the user from the session
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // Get the toy from the request
    Toy toy = (Toy) request.getAttribute("toy");
    if (toy == null) {
        response.sendRedirect("home");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ToyLand - Payment</title>
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
            <h2 class="text-3xl font-bold text-gray-800">Payment</h2>
            <p class="text-gray-600">Complete your purchase</p>
        </div>

        <!-- Error Message (if any) -->
        <% if (request.getAttribute("error") != null) { %>
            <div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6" role="alert">
                <p><%= request.getAttribute("error") %></p>
            </div>
        <% } %>

        <!-- Payment Section -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            <!-- Toy Information -->
            <div class="md:col-span-1">
                <div class="bg-white rounded-lg shadow-md overflow-hidden">
                    <img src="<%= toy.getImageUrl() != null && !toy.getImageUrl().isEmpty() ? toy.getImageUrl() : "/api/placeholder/400/300" %>"
                         alt="<%= toy.getName() %>" class="w-full h-64 object-cover">

                    <div class="p-4">
                        <div class="flex justify-between items-start mb-2">
                            <h3 class="text-xl font-semibold text-gray-800"><%= toy.getName() %></h3>
                            <span class="bg-purple-100 text-purple-800 text-xs px-2 py-1 rounded-full"><%= toy.getCategory() %></span>
                        </div>

                        <p class="text-gray-600 text-sm mb-4"><%= toy.getDescription() != null ? toy.getDescription() : "No description provided." %></p>

                        <div class="flex justify-between items-center mb-2">
                            <span class="text-purple-600 font-bold text-xl">$<%= String.format("%.2f", toy.getPrice()) %></span>
                            <span class="text-sm text-gray-500">Age: <%= toy.getAgeRange() %>+</span>
                        </div>

                        <div class="mb-2">
                            <span class="text-sm text-gray-500">Available: <%= toy.getQuantity() %> in stock</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Payment Form -->
            <div class="md:col-span-2">
                <div class="bg-white rounded-lg shadow-md p-6">
                    <h3 class="text-xl font-semibold mb-6">Payment Details</h3>

                    <form action="payment" method="post">
                        <input type="hidden" name="toyId" value="<%= toy.getId() %>">

                        <div class="mb-6">
                            <label for="quantity" class="block text-gray-700 text-sm font-bold mb-2">Quantity</label>
                            <input type="number" id="quantity" name="quantity" value="1" min="1" max="<%= toy.getQuantity() %>" required
                                   class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600"
                                   onchange="updateTotal()">
                            <p class="text-xs text-gray-500 mt-1">Maximum: <%= toy.getQuantity() %></p>
                        </div>

                        <div class="mb-6">
                            <label for="cardName" class="block text-gray-700 text-sm font-bold mb-2">Name on Card</label>
                            <input type="text" id="cardName" name="cardName" required
                                   class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                        </div>

                        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
                            <div>
                                <label for="cardNumber" class="block text-gray-700 text-sm font-bold mb-2">Card Number</label>
                                <input type="text" id="cardNumber" name="cardNumber" required
                                       placeholder="XXXX XXXX XXXX XXXX"
                                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                            </div>

                            <div class="grid grid-cols-2 gap-4">
                                <div>
                                    <label for="expiryDate" class="block text-gray-700 text-sm font-bold mb-2">Expiry Date</label>
                                    <input type="text" id="expiryDate" name="expiryDate" required
                                           placeholder="MM/YY"
                                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                                </div>

                                <div>
                                    <label for="cvv" class="block text-gray-700 text-sm font-bold mb-2">CVV</label>
                                    <input type="text" id="cvv" name="cvv" required
                                           placeholder="XXX"
                                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                                </div>
                            </div>
                        </div>

                        <div class="mb-6">
                            <label for="address" class="block text-gray-700 text-sm font-bold mb-2">Shipping Address</label>
                            <textarea id="address" name="address" rows="3" required
                                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600"><%= user.getAddress() != null ? user.getAddress() : "" %></textarea>
                        </div>

                        <div class="bg-gray-100 p-4 rounded-md mb-6">
                            <div class="flex justify-between mb-2">
                                <span class="text-gray-600">Subtotal:</span>
                                <span class="text-gray-800" id="subtotal">$<%= String.format("%.2f", toy.getPrice()) %></span>
                            </div>

                            <div class="flex justify-between mb-2">
                                <span class="text-gray-600">Shipping:</span>
                                <span class="text-gray-800">$5.00</span>
                            </div>

                            <div class="flex justify-between font-bold">
                                <span>Total:</span>
                                <span class="text-purple-600" id="totalPrice">$<%= String.format("%.2f", toy.getPrice() + 5.00) %></span>
                            </div>
                        </div>

                        <button type="submit"
                                class="w-full bg-purple-600 text-white py-3 rounded-md hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 transition-colors duration-300">
                            Complete Purchase
                        </button>
                    </form>
                </div>
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

    <script>
        function updateTotal() {
            const quantity = document.getElementById('quantity').value;
            const price = <%= toy.getPrice() %>;
            const shipping = 5.00;

            const subtotal = price * quantity;
            const total = subtotal + shipping;

            document.getElementById('subtotal').textContent = '$' + subtotal.toFixed(2);
            document.getElementById('totalPrice').textContent = '$' + total.toFixed(2);
        }
    </script>
</body>
</html>