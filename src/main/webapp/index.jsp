<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to ToyLand</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gradient-to-r from-purple-100 to-blue-100 min-h-screen">
<div class="container mx-auto px-4 py-12">
    <div class="text-center mb-12">
        <h1 class="text-5xl font-bold text-purple-600 mb-4">Welcome to ToyLand</h1>
        <p class="text-xl text-gray-600">Where imagination comes to life!</p>
    </div>

    <div class="flex justify-center">
        <div class="bg-white rounded-lg shadow-lg p-8 w-full max-w-md">
            <div class="text-center mb-8">
                <h2 class="text-3xl font-semibold text-gray-800">Log In</h2>
                <p class="text-gray-600 mt-2">Enter your credentials to continue</p>
            </div>

            <% if (request.getAttribute("error") != null) { %>
            <div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6" role="alert">
                <p><%= request.getAttribute("error") %></p>
            </div>
            <% } %>

            <% if (session.getAttribute("message") != null) { %>
            <div class="bg-green-100 border-l-4 border-green-500 text-green-700 p-4 mb-6" role="alert">
                <p><%= session.getAttribute("message") %></p>
            </div>
            <% session.removeAttribute("message"); %>
            <% } %>

            <form action="login" method="post">
                <div class="mb-6">
                    <label for="username" class="block text-gray-700 text-sm font-bold mb-2">Username</label>
                    <input type="text" id="username" name="username" required
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                </div>

                <div class="mb-6">
                    <label for="password" class="block text-gray-700 text-sm font-bold mb-2">Password</label>
                    <input type="password" id="password" name="password" required
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                </div>

                <div class="flex items-center justify-between mb-6">
                    <div class="flex items-center">
                        <input type="checkbox" id="remember" name="remember"
                               class="h-4 w-4 text-purple-600 focus:ring-purple-500 border-gray-300 rounded">
                        <label for="remember" class="ml-2 block text-sm text-gray-700">
                            Remember me
                        </label>
                    </div>

                    <a href="#" class="text-sm text-purple-600 hover:text-purple-800">
                        Forgot your password?
                    </a>
                </div>

                <div>
                    <button type="submit"
                            class="w-full bg-purple-600 text-white py-2 px-4 rounded-md hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 transition-colors duration-300">
                        Log In
                    </button>
                </div>
            </form>

            <div class="text-center mt-6">
                <p class="text-sm text-gray-600">
                    Don't have an account?
                    <a href="register.jsp" class="text-purple-600 hover:text-purple-800 font-semibold">
                        Sign up
                    </a>
                </p>
            </div>
        </div>
    </div>

    <div class="mt-12 text-center">
        <div class="flex justify-center mb-8">
            <div class="w-full max-w-6xl">
                <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
                    <div class="bg-white p-6 rounded-lg shadow-md">
                        <div class="text-purple-600 text-4xl mb-4">
                            <i class="fas fa-child"></i>
                            🧸
                        </div>
                        <h3 class="text-xl font-semibold mb-2">Wide Selection</h3>
                        <p class="text-gray-600">Explore our vast collection of toys for all ages and interests.</p>
                    </div>

                    <div class="bg-white p-6 rounded-lg shadow-md">
                        <div class="text-purple-600 text-4xl mb-4">
                            <i class="fas fa-shipping-fast"></i>
                            🚚
                        </div>
                        <h3 class="text-xl font-semibold mb-2">Fast Delivery</h3>
                        <p class="text-gray-600">Get your favorite toys delivered quickly to your doorstep.</p>
                    </div>

                    <div class="bg-white p-6 rounded-lg shadow-md">
                        <div class="text-purple-600 text-4xl mb-4">
                            <i class="fas fa-tag"></i>
                            💰
                        </div>
                        <h3 class="text-xl font-semibold mb-2">Great Deals</h3>
                        <p class="text-gray-600">Enjoy competitive prices and special offers on quality toys.</p>
                    </div>
                </div>
            </div>
        </div>

        <p class="text-gray-600">&copy; 2025 ToyLand. All rights reserved.</p>
    </div>
</div>
</body>
</html>