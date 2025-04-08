<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.toystore.model.Toy" %>
<%@ page import="com.toystore.model.User" %>

<%
    // Get the user from the session
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // Get the user's toys from the request
    List<Toy> userToys = (List<Toy>) request.getAttribute("userToys");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ToyLand - Your Profile</title>
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
                    <a href="sell" class="hover:text-purple-200 transition-colors duration-300">Sell</a>
                    <a href="logout" class="bg-purple-800 hover:bg-purple-900 px-4 py-2 rounded-md transition-colors duration-300">Logout</a>
                </div>
            </div>
        </div>
    </header>

    <!-- Main Content -->
    <main class="container mx-auto px-4 py-8">
        <!-- Page Title -->
        <div class="mb-8">
            <h2 class="text-3xl font-bold text-gray-800">Your Profile</h2>
            <p class="text-gray-600">Manage your account and listings</p>
        </div>

        <!-- Error Message (if any) -->
        <% if (request.getAttribute("error") != null) { %>
            <div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6" role="alert">
                <p><%= request.getAttribute("error") %></p>
            </div>
        <% } %>

        <!-- Success Message (if any) -->
        <% if (request.getAttribute("message") != null) { %>
            <div class="bg-green-100 border-l-4 border-green-500 text-green-700 p-4 mb-6" role="alert">
                <p><%= request.getAttribute("message") %></p>
            </div>
        <% } %>

        <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
            <!-- Profile Information Section -->
            <section class="lg:col-span-1">
                <div class="bg-white rounded-lg shadow-md p-6">
                    <div class="flex items-center justify-center mb-6">
                        <div class="w-24 h-24 bg-purple-200 rounded-full flex items-center justify-center">
                            <span class="text-purple-800 text-3xl font-bold"><%= user.getUsername().substring(0, 1).toUpperCase() %></span>
                        </div>
                    </div>

                    <h3 class="text-xl font-semibold text-center mb-4"><%= user.getFullName() != null ? user.getFullName() : user.getUsername() %></h3>

                    <div class="mt-6">
                        <div class="mb-4">
                            <span class="text-gray-500 text-sm">Username:</span>
                            <p class="text-gray-800"><%= user.getUsername() %></p>
                        </div>

                        <div class="mb-4">
                            <span class="text-gray-500 text-sm">Email:</span>
                            <p class="text-gray-800"><%= user.getEmail() != null ? user.getEmail() : "Not set" %></p>
                        </div>

                        <div class="mb-4">
                            <span class="text-gray-500 text-sm">Address:</span>
                            <p class="text-gray-800"><%= user.getAddress() != null ? user.getAddress() : "Not set" %></p>
                        </div>

                        <div class="mb-4">
                            <span class="text-gray-500 text-sm">Phone:</span>
                            <p class="text-gray-800"><%= user.getPhone() != null ? user.getPhone() : "Not set" %></p>
                        </div>
                    </div>
                </div>
            </section>

            <!-- Edit Profile Section -->
            <section class="lg:col-span-2">
                <div class="bg-white rounded-lg shadow-md p-6">
                    <h3 class="text-xl font-semibold mb-6">Edit Profile Information</h3>

                    <form action="profile" method="post">
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <label for="fullName" class="block text-gray-700 text-sm font-bold mb-2">Full Name</label>
                                <input type="text" id="fullName" name="fullName"
                                       value="<%= user.getFullName() != null ? user.getFullName() : "" %>"
                                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                            </div>

                            <div>
                                <label for="email" class="block text-gray-700 text-sm font-bold mb-2">Email</label>
                                <input type="email" id="email" name="email"
                                       value="<%= user.getEmail() != null ? user.getEmail() : "" %>"
                                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                            </div>

                            <div>
                                <label for="phone" class="block text-gray-700 text-sm font-bold mb-2">Phone</label>
                                <input type="tel" id="phone" name="phone"
                                       value="<%= user.getPhone() != null ? user.getPhone() : "" %>"
                                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                            </div>

                            <div class="md:col-span-2">
                                <label for="address" class="block text-gray-700 text-sm font-bold mb-2">Address</label>
                                <textarea id="address" name="address" rows="3"
                                          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600"><%= user.getAddress() != null ? user.getAddress() : "" %></textarea>
                            </div>

                            <div>
                                <label for="password" class="block text-gray-700 text-sm font-bold mb-2">New Password</label>
                                <input type="password" id="password" name="password"
                                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                                <p class="text-xs text-gray-500 mt-1">Leave blank to keep current password</p>
                            </div>

                            <div>
                                <label for="confirmPassword" class="block text-gray-700 text-sm font-bold mb-2">Confirm New Password</label>
                                <input type="password" id="confirmPassword" name="confirmPassword"
                                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                            </div>
                        </div>

                        <div class="mt-6">
                            <button type="submit"
                                    class="bg-purple-600 text-white py-2 px-6 rounded-md hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 transition-colors duration-300">
                                Update Profile
                            </button>
                        </div>
                    </form>
                </div>
            </section>
        </div>

            <!-- Account Options Section -->
            <section class="mt-4">
                <div class="bg-white rounded-lg shadow-md p-6">
                    <h3 class="text-xl font-semibold mb-4">Account Options</h3>

                    <div class="flex flex-col space-y-3">
                        <a href="payment?action=history" class="flex items-center text-gray-700 hover:text-purple-600 transition-colors duration-300">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01" />
                            </svg>
                            View Purchase History
                        </a>

                        <a href="sell" class="flex items-center text-gray-700 hover:text-purple-600 transition-colors duration-300">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                            </svg>
                            Sell New Toy
                        </a>

                        <% if (user.isAdmin()) { %>
                            <a href="payment?action=history&view=all" class="flex items-center text-gray-700 hover:text-purple-600 transition-colors duration-300">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
                                </svg>
                                View All Purchases (Admin)
                            </a>
                        <% } %>
                    </div>
                </div>
            </section>

        <!-- Your Listed Toys Section -->
        <section class="mt-12">
            <div class="flex justify-between items-center mb-6">
                <h3 class="text-xl font-semibold text-gray-800">Your Listed Toys</h3>
                <a href="sell" class="bg-purple-600 text-white px-4 py-2 rounded-md hover:bg-purple-700 transition-colors duration-300">
                    Add New Toy
                </a>
            </div>

            <% if (userToys == null || userToys.isEmpty()) { %>
                <div class="bg-gray-100 p-8 rounded-lg text-center">
                    <p class="text-gray-600 text-lg">You haven't listed any toys for sale yet.</p>
                    <a href="sell" class="text-purple-600 hover:text-purple-800 font-semibold mt-4 inline-block">
                        Start selling now
                    </a>
                </div>
            <% } else { %>
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    <% for (Toy toy : userToys) { %>
                    <div class="bg-white rounded-lg shadow-md overflow-hidden">
                        <img src="<%= toy.getImageUrl() != null && !toy.getImageUrl().isEmpty() ? toy.getImageUrl() : "/api/placeholder/300/200" %>"
                             alt="<%= toy.getName() %>" class="w-full h-48 object-cover">

                        <div class="p-4">
                            <div class="flex justify-between items-start mb-2">
                                <h3 class="text-lg font-semibold text-gray-800"><%= toy.getName() %></h3>
                                <span class="bg-purple-100 text-purple-800 text-xs px-2 py-1 rounded-full"><%= toy.getCategory() %></span>
                            </div>

                            <p class="text-gray-600 text-sm mb-4 line-clamp-2"><%= toy.getDescription() != null ? toy.getDescription() : "No description provided." %></p>

                            <div class="flex justify-between items-center mb-4">
                                <span class="text-purple-600 font-bold">$<%= String.format("%.2f", toy.getPrice()) %></span>
                                <span class="text-sm text-gray-500">Age: <%= toy.getAgeRange() %>+ | Qty: <%= toy.getQuantity() %></span>
                            </div>

                            <div class="flex space-x-2">
                                <a href="sell?action=edit&id=<%= toy.getId() %>"
                                   class="flex-1 bg-blue-600 text-white text-center py-2 rounded-md hover:bg-blue-700 transition-colors duration-300">
                                    Edit
                                </a>
                                <a href="sell?action=delete&id=<%= toy.getId() %>"
                                   onclick="return confirm('Are you sure you want to delete this toy?');"
                                   class="flex-1 bg-red-600 text-white text-center py-2 rounded-md hover:bg-red-700 transition-colors duration-300">
                                    Delete
                                </a>
                            </div>
                        </div>
                    </div>
                    <% } %>
                </div>
            <% } %>
        </section>
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