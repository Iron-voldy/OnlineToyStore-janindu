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
    <title>ToyLand - Rate & Feedback</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        .rating {
            display: flex;
            flex-direction: row-reverse;
            justify-content: flex-end;
        }

        .rating > input {
            display: none;
        }

        .rating > label {
            position: relative;
            width: 1.1em;
            font-size: 2.5em;
            color: #FFD700;
            cursor: pointer;
        }

        .rating > label::before {
            content: "★";
            position: absolute;
            opacity: 0;
        }

        .rating > label:hover:before,
        .rating > label:hover ~ label:before {
            opacity: 1 !important;
        }

        .rating > input:checked ~ label:before {
            opacity: 1;
        }

        .rating:hover > input:checked ~ label:before {
            opacity: 0.4;
        }
    </style>
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
            <h2 class="text-3xl font-bold text-gray-800">Rate & Feedback</h2>
            <p class="text-gray-600">Share your experience with this toy</p>
        </div>

        <!-- Success Message (if any) -->
        <% if (request.getAttribute("success") != null) { %>
            <div class="bg-green-100 border-l-4 border-green-500 text-green-700 p-4 mb-6" role="alert">
                <p><%= request.getAttribute("success") %></p>
            </div>
        <% } %>

        <!-- Feedback Section -->
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

                        <div class="flex justify-between items-center">
                            <span class="text-purple-600 font-bold">$<%= String.format("%.2f", toy.getPrice()) %></span>
                            <span class="text-sm text-gray-500">Age: <%= toy.getAgeRange() %>+</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Feedback Form -->
            <div class="md:col-span-2">
                <div class="bg-white rounded-lg shadow-md p-6">
                    <h3 class="text-xl font-semibold mb-6">Your Feedback</h3>

                    <form action="feedback" method="post">
                        <input type="hidden" name="toyId" value="<%= toy.getId() %>">

                        <div class="mb-6">
                            <label class="block text-gray-700 text-sm font-bold mb-2">Rating</label>
                            <div class="rating">
                                <input type="radio" name="rating" value="5" id="rate5" required>
                                <label for="rate5" title="5 stars"></label>

                                <input type="radio" name="rating" value="4" id="rate4">
                                <label for="rate4" title="4 stars"></label>

                                <input type="radio" name="rating" value="3" id="rate3">
                                <label for="rate3" title="3 stars"></label>

                                <input type="radio" name="rating" value="2" id="rate2">
                                <label for="rate2" title="2 stars"></label>

                                <input type="radio" name="rating" value="1" id="rate1">
                                <label for="rate1" title="1 star"></label>
                            </div>
                        </div>

                        <div class="mb-6">
                            <label for="comment" class="block text-gray-700 text-sm font-bold mb-2">Your Comments</label>
                            <textarea id="comment" name="comment" rows="5" placeholder="Share your experience with this toy..."
                                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600"></textarea>
                        </div>

                        <div class="flex items-center mb-6">
                            <input type="checkbox" id="recommend" name="recommend" class="h-4 w-4 text-purple-600 focus:ring-purple-500 border-gray-300 rounded">
                            <label for="recommend" class="ml-2 block text-sm text-gray-700">
                                I would recommend this toy to others
                            </label>
                        </div>

                        <button type="submit"
                                class="bg-purple-600 text-white py-2 px-6 rounded-md hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 transition-colors duration-300">
                            Submit Feedback
                        </button>

                        <a href="home" class="ml-4 text-gray-600 hover:text-gray-800">
                            Cancel
                        </a>
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
</body>
</html>