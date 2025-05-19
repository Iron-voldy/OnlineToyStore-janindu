<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.toystore.model.User" %>
<%@ page import="com.toystore.model.Wishlist" %>

<%
    // Get the user from the session
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // Get error or success message if any
    String error = (String) request.getAttribute("error");
    String message = (String) request.getAttribute("message");

    // Get wishlist
    Wishlist wishlist = (Wishlist) request.getAttribute("wishlist");

    // Check if the current user is the owner
    boolean isOwner = wishlist != null && user.getId().equals(wishlist.getUserId());

    // Make sure only the owner can access this page
    if (!isOwner) {
        response.sendRedirect(request.getContextPath() + "/wishlist");
        return;
    }

    // Format for displaying date
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

    // Generate share URL
    String shareUrl = request.getScheme() + "://" + request.getServerName();
    if (request.getServerPort() != 80 && request.getServerPort() != 443) {
        shareUrl += ":" + request.getServerPort();
    }
    shareUrl += request.getContextPath() + "/wishlist/view/" + wishlist.getId();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ToyLand - Share Wishlist</title>
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
                    <a href="<%= request.getContextPath() %>/home" class="hover:text-purple-200 transition-colors duration-300">Home</a>
                    <a href="<%= request.getContextPath() %>/wishlist" class="hover:text-purple-200 transition-colors duration-300">Wishlists</a>
                    <a href="<%= request.getContextPath() %>/profile" class="hover:text-purple-200 transition-colors duration-300">Profile</a>
                    <a href="<%= request.getContextPath() %>/logout" class="bg-purple-800 hover:bg-purple-900 px-4 py-2 rounded-md transition-colors duration-300">Logout</a>
                </div>
            </div>
        </div>
    </header>

    <!-- Main Content -->
    <main class="container mx-auto px-4 py-8">
        <% if (wishlist != null) { %>
            <!-- Page Title -->
            <div class="mb-8">
                <div class="flex items-center">
                    <a href="<%= request.getContextPath() %>/wishlist/view/<%= wishlist.getId() %>" class="text-gray-600 hover:text-gray-800 mr-2">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                            <path fill-rule="evenodd" d="M9.707 16.707a1 1 0 01-1.414 0l-6-6a1 1 0 010-1.414l6-6a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l4.293 4.293a1 1 0 010 1.414z" clip-rule="evenodd" />
                        </svg>
                    </a>
                    <h2 class="text-3xl font-bold text-gray-800">Share Wishlist</h2>
                </div>
                <p class="text-gray-600 mt-2">Share your "<%= wishlist.getName() %>" wishlist with friends and family</p>
            </div>

            <!-- Error Message (if any) -->
            <% if (error != null) { %>
                <div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6" role="alert">
                    <p><%= error %></p>
                </div>
            <% } %>

            <!-- Success Message (if any) -->
            <% if (message != null) { %>
                <div class="bg-green-100 border-l-4 border-green-500 text-green-700 p-4 mb-6" role="alert">
                    <p><%= message %></p>
                </div>
            <% } %>

            <!-- Sharing Settings Section -->
            <section class="bg-white rounded-lg shadow-md p-6 mb-8">
                <h3 class="text-xl font-semibold mb-6">Visibility Settings</h3>

                <form action="<%= request.getContextPath() %>/wishlist" method="post">
                    <input type="hidden" name="action" value="updateVisibility">
                    <input type="hidden" name="wishlistId" value="<%= wishlist.getId() %>">

                    <div class="mb-6">
                        <label class="flex items-center">
                            <input type="checkbox" name="isPublic" <%= wishlist.isPublic() ? "checked" : "" %>
                                   class="h-4 w-4 text-purple-600 focus:ring-purple-500 border-gray-300 rounded">
                            <span class="ml-2 text-gray-700">Make this wishlist public</span>
                        </label>

                        <div class="mt-2 text-sm">
                            <p class="text-gray-600">
                                <% if (wishlist.isPublic()) { %>
                                    This wishlist is currently <span class="font-semibold text-green-600">public</span>. Anyone with the link can view it, and it appears in public wishlists.
                                <% } else { %>
                                    This wishlist is currently <span class="font-semibold text-yellow-600">private</span>. Only you can view it.
                                <% } %>
                            </p>
                        </div>
                    </div>

                    <button type="submit"
                            class="bg-purple-600 text-white py-2 px-6 rounded-md hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 transition-colors duration-300">
                        Save Settings
                    </button>
                </form>
            </section>

            <!-- Share Link Section -->
            <section class="bg-white rounded-lg shadow-md p-6 mb-8">
                <h3 class="text-xl font-semibold mb-6">Share Link</h3>

                <div class="relative">
                    <input type="text" id="shareUrl" value="<%= shareUrl %>" readonly
                           class="w-full px-3 py-3 pr-28 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                    <button id="copyBtn" onclick="copyShareLink()"
                            class="absolute right-1 top-1 bottom-1 bg-purple-600 text-white px-4 rounded-md hover:bg-purple-700 transition-colors duration-300">
                        Copy Link
                    </button>
                </div>

                <p class="mt-3 text-sm">
                    <% if (!wishlist.isPublic()) { %>
                        <span class="text-yellow-600">Note:</span> This wishlist is private. Make it public for others to view it with this link.
                    <% } else { %>
                        Share this link with friends and family so they can view your wishlist.
                    <% } %>
                </p>
            </section>

            <!-- Social Sharing Section -->
            <section class="bg-white rounded-lg shadow-md p-6">
                <h3 class="text-xl font-semibold mb-6">Share on Social Media</h3>

                <div class="flex flex-wrap gap-4">
                    <!-- Email Sharing -->
                    <a href="mailto:?subject=Check out my ToyLand wishlist: <%= wishlist.getName() %>&body=Hi,%0D%0A%0D%0AI wanted to share my ToyLand wishlist with you: <%= wishlist.getName() %>%0D%0A%0D%0AYou can view it here: <%= shareUrl %>%0D%0A%0D%0A"
                       class="flex items-center px-4 py-2 rounded-md bg-gray-100 hover:bg-gray-200 transition-colors duration-300"
                       target="_blank" rel="noopener noreferrer">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                        </svg>
                        Email
                    </a>

                    <!-- Facebook Sharing -->
                    <a href="https://www.facebook.com/sharer/sharer.php?u=<%= shareUrl %>"
                       class="flex items-center px-4 py-2 rounded-md bg-blue-600 hover:bg-blue-700 text-white transition-colors duration-300"
                       target="_blank" rel="noopener noreferrer">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="currentColor" viewBox="0 0 24 24">
                            <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z" />
                        </svg>
                        Facebook
                    </a>

                    <!-- Twitter/X Sharing -->
                    <a href="https://twitter.com/intent/tweet?text=Check out my ToyLand wishlist: <%= wishlist.getName() %>&url=<%= shareUrl %>"
                       class="flex items-center px-4 py-2 rounded-md bg-black hover:bg-gray-800 text-white transition-colors duration-300"
                       target="_blank" rel="noopener noreferrer">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="currentColor" viewBox="0 0 24 24">
                            <path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z" />
                        </svg>
                        X
                    </a>

                    <!-- WhatsApp Sharing -->
                    <a href="https://wa.me/?text=Check out my ToyLand wishlist: <%= wishlist.getName() %> <%= shareUrl %>"
                       class="flex items-center px-4 py-2 rounded-md bg-green-500 hover:bg-green-600 text-white transition-colors duration-300"
                       target="_blank" rel="noopener noreferrer">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="currentColor" viewBox="0 0 24 24">
                            <path d="M17.472 14.382c-.297-.149-1.758-.867-2.03-.967-.273-.099-.471-.148-.67.15-.197.297-.767.966-.94 1.164-.173.199-.347.223-.644.075-.297-.15-1.255-.463-2.39-1.475-.883-.788-1.48-1.761-1.653-2.059-.173-.297-.018-.458.13-.606.134-.133.298-.347.446-.52.149-.174.198-.298.298-.497.099-.198.05-.371-.025-.52-.075-.149-.669-1.612-.916-2.207-.242-.579-.487-.5-.669-.51-.173-.008-.371-.01-.57-.01-.198 0-.52.074-.792.372-.272.297-1.04 1.016-1.04 2.479 0 1.462 1.065 2.875 1.213 3.074.149.198 2.096 3.2 5.077 4.487.709.306 1.262.489 1.694.625.712.227 1.36.195 1.871.118.571-.085 1.758-.719 2.006-1.413.248-.694.248-1.289.173-1.413-.074-.124-.272-.198-.57-.347m-5.421 7.403h-.004a9.87 9.87 0 01-5.031-1.378l-.361-.214-3.741.982.998-3.648-.235-.374a9.86 9.86 0 01-1.51-5.26c.001-5.45 4.436-9.884 9.888-9.884 2.64 0 5.122 1.03 6.988 2.898a9.825 9.825 0 012.893 6.994c-.003 5.45-4.437 9.884-9.885 9.884m8.413-18.297A11.815 11.815 0 0012.05 0C5.495 0 .16 5.335.157 11.892c0 2.096.547 4.142 1.588 5.945L.057 24l6.305-1.654a11.882 11.882 0 005.683 1.448h.005c6.554 0 11.89-5.335 11.893-11.893a11.821 11.821 0 00-3.48-8.413z" />
                        </svg>
                        WhatsApp
                    </a>
                </div>

                <div class="mt-6 text-center">
                    <a href="<%= request.getContextPath() %>/wishlist/view/<%= wishlist.getId() %>" class="text-purple-600 hover:text-purple-800">
                        Back to Wishlist
                    </a>
                </div>
            </section>

        <% } else { %>
            <div class="bg-white p-8 rounded-lg shadow-md text-center">
                <div class="text-gray-400 mb-4">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                </div>
                <h3 class="text-xl font-semibold text-gray-800 mb-2">Wishlist Not Found</h3>
                <p class="text-gray-600 mb-6">
                    The wishlist you're trying to share doesn't exist.
                </p>
                <a href="<%= request.getContextPath() %>/wishlist" class="text-purple-600 hover:text-purple-800 font-semibold">
                    Return to Wishlists
                </a>
            </div>
        <% } %>
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

    <!-- JavaScript for copying share link -->
    <script>
        function copyShareLink() {
            const shareUrlInput = document.getElementById('shareUrl');
            const copyBtn = document.getElementById('copyBtn');

            // Select the text field
            shareUrlInput.select();
            shareUrlInput.setSelectionRange(0, 99999); // For mobile devices

            // Copy the text inside the text field
            document.execCommand("copy");

            // Change button text temporarily
            const originalText = copyBtn.innerText;
            copyBtn.innerText = "Copied!";
            setTimeout(function() {
                copyBtn.innerText = originalText;
            }, 2000);
        }
    </script>
</body>
</html>