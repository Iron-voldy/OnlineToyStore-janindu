<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
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

    // Get wishlists
    List<Wishlist> wishlists = (List<Wishlist>) request.getAttribute("wishlists");

    // Check if we're in edit mode
    boolean isEditing = request.getAttribute("editing") != null && (boolean) request.getAttribute("editing");
    Wishlist editWishlist = isEditing ? (Wishlist) request.getAttribute("wishlist") : null;

    // Check if we're showing public wishlists
    List<Wishlist> publicWishlists = (List<Wishlist>) request.getAttribute("publicWishlists");
    Map<String, String> ownerNames = (Map<String, String>) request.getAttribute("ownerNames");
    boolean showingPublic = publicWishlists != null;

    // Format for displaying date
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ToyLand - Wishlist</title>
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
            <h2 class="text-3xl font-bold text-gray-800">
                <% if (showingPublic) { %>
                    Public Wishlists
                <% } else { %>
                    Your Wishlists
                <% } %>
            </h2>
            <p class="text-gray-600">
                <% if (showingPublic) { %>
                    Browse public wishlists shared by other users
                <% } else { %>
                    Manage your toy wishlists
                <% } %>
            </p>
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

        <!-- Wishlist Navigation -->
        <div class="flex mb-8 border-b pb-4">
            <a href="<%= request.getContextPath() %>/wishlist"
               class="<%= !showingPublic ? "text-purple-600 border-b-2 border-purple-600 font-semibold" : "text-gray-600 hover:text-purple-600" %> mr-6 pb-2">
                My Wishlists
            </a>
            <a href="<%= request.getContextPath() %>/wishlist/public"
               class="<%= showingPublic ? "text-purple-600 border-b-2 border-purple-600 font-semibold" : "text-gray-600 hover:text-purple-600" %> mr-6 pb-2">
                Public Wishlists
            </a>
        </div>

        <% if (!showingPublic) { %>
            <!-- Create/Edit Wishlist Section -->
            <section id="create-form" class="bg-white rounded-lg shadow-md p-6 mb-8">
                <h3 class="text-xl font-semibold mb-4"><%= isEditing ? "Edit Wishlist" : "Create New Wishlist" %></h3>

                <form action="wishlist" method="post">
                    <input type="hidden" name="action" value="<%= isEditing ? "update" : "create" %>">
                    <% if (isEditing) { %>
                        <input type="hidden" name="wishlistId" value="<%= editWishlist.getId() %>">
                    <% } %>

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div>
                            <label for="name" class="block text-gray-700 text-sm font-bold mb-2">Wishlist Name *</label>
                            <input type="text" id="name" name="name" required
                                   value="<%= isEditing ? editWishlist.getName() : "" %>"
                                   class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                        </div>

                        <div>
                            <label for="description" class="block text-gray-700 text-sm font-bold mb-2">Description</label>
                            <input type="text" id="description" name="description"
                                   value="<%= isEditing && editWishlist.getDescription() != null ? editWishlist.getDescription() : "" %>"
                                   class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                        </div>
                    </div>

                    <div class="mt-4">
                        <label class="flex items-center">
                            <input type="checkbox" name="isPublic" <%= isEditing && editWishlist.isPublic() ? "checked" : "" %>
                                   class="h-4 w-4 text-purple-600 focus:ring-purple-500 border-gray-300 rounded">
                            <span class="ml-2 text-gray-700">Make this wishlist public</span>
                        </label>
                        <p class="text-xs text-gray-500 mt-1">Public wishlists can be viewed by other users</p>
                    </div>

                    <div class="mt-6">
                        <button type="submit"
                                class="bg-purple-600 text-white py-2 px-6 rounded-md hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 transition-colors duration-300">
                            <%= isEditing ? "Update Wishlist" : "Create Wishlist" %>
                        </button>

                        <% if (isEditing) { %>
                            <a href="<%= request.getContextPath() %>/wishlist" class="ml-4 text-gray-600 hover:text-gray-800">Cancel</a>
                        <% } %>
                    </div>
                </form>
            </section>
        <% } %>

        <!-- Wishlists Grid -->
        <section>
            <div class="flex justify-between items-center mb-6">
                <h3 class="text-xl font-semibold text-gray-800">
                    <% if (showingPublic) { %>
                        Browse Public Wishlists
                    <% } else { %>
                        Your Wishlists
                    <% } %>
                </h3>

                <% if (!showingPublic) { %>
                    <a href="#" onclick="document.getElementById('create-form').scrollIntoView({behavior: 'smooth'})" class="text-purple-600 hover:text-purple-800">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 inline-block mr-1" viewBox="0 0 20 20" fill="currentColor">
                            <path fill-rule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clip-rule="evenodd" />
                        </svg>
                        Create New
                    </a>
                <% } %>
            </div>

            <%
                List<Wishlist> displayWishlists = showingPublic ? publicWishlists : wishlists;
                if (displayWishlists == null || displayWishlists.isEmpty()) {
            %>
                <div class="bg-white p-8 rounded-lg shadow-md text-center">
                    <div class="text-gray-400 mb-4">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                        </svg>
                    </div>
                    <h3 class="text-xl font-semibold text-gray-800 mb-2">
                        <% if (showingPublic) { %>
                            No Public Wishlists Found
                        <% } else { %>
                            No Wishlists Yet
                        <% } %>
                    </h3>
                    <p class="text-gray-600 mb-6">
                        <% if (showingPublic) { %>
                            There are no public wishlists available to browse.
                        <% } else { %>
                            Create your first wishlist to start saving toys you love!
                        <% } %>
                    </p>

                    <% if (!showingPublic) { %>
                        <a href="#" onclick="document.getElementById('create-form').scrollIntoView({behavior: 'smooth'})"
                           class="bg-purple-600 text-white px-6 py-2 rounded-md hover:bg-purple-700 transition-colors duration-300">
                            Create Your First Wishlist
                        </a>
                    <% } %>
                </div>
            <% } else { %>
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    <% for (Wishlist wishlist : displayWishlists) { %>
                    <div class="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
                        <div class="p-6">
                            <div class="flex justify-between items-start mb-4">
                                <h3 class="text-lg font-semibold text-gray-800"><%= wishlist.getName() %></h3>
                                <span class="<%= wishlist.isPublic() ? "bg-green-100 text-green-800" : "bg-yellow-100 text-yellow-800" %> text-xs px-2 py-1 rounded-full">
                                    <%= wishlist.isPublic() ? "Public" : "Private" %>
                                </span>
                            </div>

                            <% if (showingPublic && ownerNames != null && ownerNames.containsKey(wishlist.getId())) { %>
                                <p class="text-sm text-gray-600 mb-3">
                                    By: <%= ownerNames.get(wishlist.getId()) %>
                                </p>
                            <% } %>

                            <p class="text-gray-600 text-sm mb-4">
                                <%= wishlist.getDescription() != null ? wishlist.getDescription() : "No description provided." %>
                            </p>

                            <div class="flex justify-between items-center mb-4">
                                <span class="text-sm text-gray-500">
                                    Created: <%= dateFormat.format(wishlist.getCreatedDate()) %>
                                </span>
                                <span class="text-sm text-purple-600 font-semibold">
                                    <%= wishlist.getItems().size() %> items
                                </span>
                            </div>

                            <div class="flex space-x-2">
                                <a href="<%= request.getContextPath() %>/wishlist/view/<%= wishlist.getId() %>"
                                   class="flex-1 bg-purple-600 text-white text-center py-2 rounded-md hover:bg-purple-700 transition-colors duration-300">
                                    View
                                </a>

                                <% if (!showingPublic) { // Only show edit/share buttons for user's own wishlists %>
                                    <a href="<%= request.getContextPath() %>/wishlist/edit/<%= wishlist.getId() %>"
                                       class="flex-1 bg-blue-600 text-white text-center py-2 rounded-md hover:bg-blue-700 transition-colors duration-300">
                                        Edit
                                    </a>

                                    <a href="<%= request.getContextPath() %>/wishlist/share/<%= wishlist.getId() %>"
                                       class="flex-1 bg-green-600 text-white text-center py-2 rounded-md hover:bg-green-700 transition-colors duration-300">
                                        Share
                                    </a>
                                <% } %>
                            </div>

                            <% if (!showingPublic) { // Only show delete button for user's own wishlists %>
                                <form action="<%= request.getContextPath() %>/wishlist" method="post" class="mt-2"
                                      onsubmit="return confirm('Are you sure you want to delete this wishlist? This cannot be undone.');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="wishlistId" value="<%= wishlist.getId() %>">
                                    <button type="submit" class="w-full text-red-600 text-sm hover:text-red-800 py-2">
                                        Delete Wishlist
                                    </button>
                                </form>
                            <% } %>
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