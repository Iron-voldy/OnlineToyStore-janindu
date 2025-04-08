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

    // Get the toys from the request
    List<Toy> toys = (List<Toy>) request.getAttribute("toys");
    List<String> categories = (List<String>) request.getAttribute("categories");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ToyLand - Home</title>
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
                    <a href="profile" class="hover:text-purple-200 transition-colors duration-300">Profile</a>
                    <a href="sell" class="hover:text-purple-200 transition-colors duration-300">Sell</a>
                    <a href="logout" class="bg-purple-800 hover:bg-purple-900 px-4 py-2 rounded-md transition-colors duration-300">Logout</a>
                </div>
            </div>
        </div>
    </header>

    <!-- Main Content -->
    <main class="container mx-auto px-4 py-8">
        <!-- Welcome Section -->
        <section class="bg-gradient-to-r from-purple-500 to-indigo-600 text-white rounded-lg p-8 mb-8">
            <h2 class="text-3xl font-bold mb-2">Welcome, <%= user.getFullName() != null ? user.getFullName() : user.getUsername() %>!</h2>
            <p class="text-xl">Discover amazing toys for all ages and interests.</p>
        </section>

        <!-- Filter and Sort Section -->
        <section class="bg-white rounded-lg shadow-md p-6 mb-8">
            <div class="flex flex-wrap items-center justify-between">
                <div class="w-full md:w-auto mb-4 md:mb-0">
                    <h3 class="text-lg font-semibold mb-2">Filter & Sort</h3>
                </div>

                <div class="flex flex-wrap gap-4">
                    <!-- Category Filter -->
                    <div>
                        <form action="home" method="get" class="inline-block">
                            <select name="category" onchange="this.form.submit()"
                                    class="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                                <option value="">All Categories</option>
                                <% if (categories != null) {
                                    for (String category : categories) { %>
                                        <option value="<%= category %>" <%= request.getParameter("category") != null && request.getParameter("category").equals(category) ? "selected" : "" %>>
                                            <%= category %>
                                        </option>
                                <% } } %>
                            </select>
                        </form>
                    </div>

                    <!-- Age Filter -->
                    <div>
                        <form action="home" method="get" class="inline-block">
                            <select name="age" onchange="this.form.submit()"
                                    class="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                                <option value="">All Ages</option>
                                <option value="0" <%= "0".equals(request.getParameter("age")) ? "selected" : "" %>>0+ years</option>
                                <option value="3" <%= "3".equals(request.getParameter("age")) ? "selected" : "" %>>3+ years</option>
                                <option value="5" <%= "5".equals(request.getParameter("age")) ? "selected" : "" %>>5+ years</option>
                                <option value="8" <%= "8".equals(request.getParameter("age")) ? "selected" : "" %>>8+ years</option>
                                <option value="12" <%= "12".equals(request.getParameter("age")) ? "selected" : "" %>>12+ years</option>
                            </select>
                        </form>
                    </div>

                    <!-- Sort -->
                    <div>
                        <form action="home" method="get" class="inline-block">
                            <select name="sortBy" onchange="this.form.submit()"
                                    class="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                                <option value="">Sort By</option>
                                <option value="price" <%= "price".equals(request.getParameter("sortBy")) ? "selected" : "" %>>Price: Low to High</option>
                                <option value="age" <%= "age".equals(request.getParameter("sortBy")) ? "selected" : "" %>>Age Range</option>
                                <option value="ageAndPrice" <%= "ageAndPrice".equals(request.getParameter("sortBy")) ? "selected" : "" %>>Age Range & Price</option>
                            </select>
                        </form>
                    </div>
                </div>
            </div>
        </section>

        <!-- Featured Toys Section -->
        <section class="mb-12">
            <h2 class="text-2xl font-bold mb-6">Featured Toys</h2>

            <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                <% if (toys != null && !toys.isEmpty()) {
                    // Display first 4 toys as featured
                    int featCount = Math.min(toys.size(), 4);
                    for (int i = 0; i < featCount; i++) {
                        Toy toy = toys.get(i);
                %>
                <div class="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
                    <img src="<%= toy.getImageUrl() != null && !toy.getImageUrl().isEmpty() ? toy.getImageUrl() : "/api/placeholder/300/200" %>"
                         alt="<%= toy.getName() %>" class="w-full h-48 object-cover">

                    <div class="p-4">
                        <h3 class="text-lg font-semibold text-gray-800 mb-2"><%= toy.getName() %></h3>
                        <p class="text-gray-600 text-sm mb-4 line-clamp-2"><%= toy.getDescription() %></p>

                        <div class="flex justify-between items-center mb-4">
                            <span class="text-purple-600 font-bold">$<%= String.format("%.2f", toy.getPrice()) %></span>
                            <span class="text-sm text-gray-500">Age: <%= toy.getAgeRange() %>+</span>
                        </div>

                        <div class="flex space-x-2">
                            <a href="payment?id=<%= toy.getId() %>"
                               class="flex-1 bg-purple-600 text-white text-center py-2 rounded-md hover:bg-purple-700 transition-colors duration-300">
                                Buy
                            </a>
                            <a href="feedback?id=<%= toy.getId() %>"
                               class="flex-1 bg-gray-200 text-gray-800 text-center py-2 rounded-md hover:bg-gray-300 transition-colors duration-300">
                                Rate
                            </a>
                        </div>
                    </div>
                </div>
                <% } } %>
            </div>
        </section>

        <!-- All Toys Section -->
        <section>
            <h2 class="text-2xl font-bold mb-6">All Toys</h2>

            <% if (toys == null || toys.isEmpty()) { %>
                <div class="bg-gray-100 p-8 rounded-lg text-center">
                    <p class="text-gray-600 text-lg">No toys available at the moment.</p>
                </div>
            <% } else { %>
                <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                    <% for (Toy toy : toys) { %>
                    <div class="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
                        <img src="<%= toy.getImageUrl() != null && !toy.getImageUrl().isEmpty() ? toy.getImageUrl() : "/api/placeholder/300/200" %>"
                             alt="<%= toy.getName() %>" class="w-full h-48 object-cover">

                        <div class="p-4">
                            <div class="flex justify-between items-start mb-2">
                                <h3 class="text-lg font-semibold text-gray-800"><%= toy.getName() %></h3>
                                <span class="bg-purple-100 text-purple-800 text-xs px-2 py-1 rounded-full"><%= toy.getCategory() %></span>
                            </div>

                            <p class="text-gray-600 text-sm mb-4 line-clamp-2"><%= toy.getDescription() %></p>

                            <div class="flex justify-between items-center mb-4">
                                <span class="text-purple-600 font-bold">$<%= String.format("%.2f", toy.getPrice()) %></span>
                                <span class="text-sm text-gray-500">Age: <%= toy.getAgeRange() %>+</span>
                            </div>

                            <div class="flex space-x-2">
                                <a href="payment?id=<%= toy.getId() %>"
                                   class="flex-1 bg-purple-600 text-white text-center py-2 rounded-md hover:bg-purple-700 transition-colors duration-300">
                                    Buy
                                </a>
                                <a href="feedback?id=<%= toy.getId() %>"
                                   class="flex-1 bg-gray-200 text-gray-800 text-center py-2 rounded-md hover:bg-gray-300 transition-colors duration-300">
                                    Rate
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