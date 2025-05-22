<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ToyLand - Register</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gradient-to-r from-purple-100 to-blue-100 min-h-screen">
<div class="container mx-auto px-4 py-12">
    <div class="text-center mb-12">
        <h1 class="text-5xl font-bold text-purple-600 mb-4">Welcome to ToyLand</h1>
        <p class="text-xl text-gray-600">Create your account today!</p>
    </div>

    <div class="flex justify-center">
        <div class="bg-white rounded-lg shadow-lg p-8 w-full max-w-md">
            <div class="text-center mb-8">
                <h2 class="text-3xl font-semibold text-gray-800">Register</h2>
                <p class="text-gray-600 mt-2">Fill in your details to create an account</p>
            </div>

            <% if (request.getAttribute("error") != null) { %>
            <div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6" role="alert">
                <p><%= request.getAttribute("error") %></p>
            </div>
            <% } %>

            <form action="register" method="post">
                <div class="mb-6">
                    <label for="username" class="block text-gray-700 text-sm font-bold mb-2">Username*</label>
                    <input type="text" id="username" name="username" required
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                </div>

                <div class="mb-6">
                    <label for="fullName" class="block text-gray-700 text-sm font-bold mb-2">Full Name*</label>
                    <input type="text" id="fullName" name="fullName" required
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                </div>

                <div class="mb-6">
                    <label for="email" class="block text-gray-700 text-sm font-bold mb-2">Email*</label>
                    <input type="email" id="email" name="email" required
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                </div>

                <div class="mb-6">
                    <label for="password" class="block text-gray-700 text-sm font-bold mb-2">Password*</label>
                    <input type="password" id="password" name="password" required
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                </div>

                <div class="mb-6">
                    <label for="confirmPassword" class="block text-gray-700 text-sm font-bold mb-2">Confirm Password*</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                </div>

                <div class="mb-6">
                    <label for="phone" class="block text-gray-700 text-sm font-bold mb-2">Phone Number</label>
                    <input type="tel" id="phone" name="phone"
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                </div>

                <div class="mb-6">
                    <label for="address" class="block text-gray-700 text-sm font-bold mb-2">Address</label>
                    <textarea id="address" name="address" rows="3"
                              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600"></textarea>
                </div>

                <div>
                    <button type="submit"
                            class="w-full bg-purple-600 text-white py-2 px-4 rounded-md hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 transition-colors duration-300">
                        Create Account
                    </button>
                </div>
            </form>

            <div class="text-center mt-6">
                <p class="text-sm text-gray-600">
                    Already have an account?
                    <a href="index.jsp" class="text-purple-600 hover:text-purple-800 font-semibold">
                        Log in
                    </a>
                </p>
            </div>
        </div>
    </div>
</div>
</body>
</html> 