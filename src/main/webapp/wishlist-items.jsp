<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.toystore.model.User" %>
<%@ page import="com.toystore.model.Toy" %>
<%@ page import="com.toystore.model.Wishlist" %>
<%@ page import="com.toystore.model.WishlistItem" %>

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

    // Get wishlist, owner and toy map
    Wishlist wishlist = (Wishlist) request.getAttribute("wishlist");
    User owner = (User) request.getAttribute("owner");
    Map<String, Toy> toyMap = (Map<String, Toy>) request.getAttribute("toyMap");

    // Get toy if we're adding or editing an item
    Toy selectedToy = (Toy) request.getAttribute("toy");
    List<Toy> allToys = (List<Toy>) request.getAttribute("allToys");

    // Check if we're editing an item
    boolean isEditingItem = request.getAttribute("editing") != null && (boolean) request.getAttribute("editing");
    WishlistItem itemToEdit = (WishlistItem) request.getAttribute("item");

    // Check if the current user is the owner
    boolean isOwner = wishlist != null && user.getId().equals(wishlist.getUserId());

    // Format for displaying date
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ToyLand - Wishlist Items</title>
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
            <!-- Wishlist Header -->
            <div class="flex flex-col md:flex-row md:justify-between md:items-center mb-8">
                <div>
                    <h2 class="text-3xl font-bold text-gray-800"><%= wishlist.getName() %></h2>
                    <p class="text-gray-600">
                        <%= wishlist.getDescription() != null ? wishlist.getDescription() : "" %>
                        <% if (owner != null && !owner.getId().equals(user.getId())) { %>
                            <span class="ml-2 text-sm">by <%= owner.getUsername() %></span>
                        <% } %>
                    </p>
                </div>

                <div class="mt-4 md:mt-0 flex items-center">
                    <span class="<%= wishlist.isPublic() ? "bg-green-100 text-green-800" : "bg-yellow-100 text-yellow-800" %> text-xs px-3 py-1 rounded-full mr-4">
                        <%= wishlist.isPublic() ? "Public" : "Private" %>
                    </span>

                    <% if (isOwner) { %>
                        <a href="<%= request.getContextPath() %>/wishlist/share/<%= wishlist.getId() %>" class="text-purple-600 hover:text-purple-800 text-sm">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 inline-block mr-1" viewBox="0 0 20 20" fill="currentColor">
                                <path d="M15 8a3 3 0 10-2.977-2.63l-4.94 2.47a3 3 0 100 4.319l4.94 2.47a3 3 0 10.895-1.789l-4.94-2.47a3.027 3.027 0 000-.74l4.94-2.47C13.456 7.68 14.19 8 15 8z" />
                            </svg>
                            Share
                        </a>
                    <% } %>
                </div>
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

            <% if (isOwner && selectedToy != null) { %>
                <!-- Add/Edit Item Form -->
                <section class="bg-white rounded-lg shadow-md p-6 mb-8">
                    <h3 class="text-xl font-semibold mb-4">
                        <%= isEditingItem ? "Edit Item" : "Add to Wishlist" %>
                    </h3>

                    <div class="flex flex-col md:flex-row mb-6">
                        <div class="md:w-1/4 mb-4 md:mb-0">
                            <img src="<%= selectedToy.getImageUrl() != null && !selectedToy.getImageUrl().isEmpty() ? selectedToy.getImageUrl() : "/api/placeholder/200/200" %>"
                                 alt="<%= selectedToy.getName() %>" class="w-full h-40 object-cover rounded-md">
                        </div>

                        <div class="md:w-3/4 md:pl-6">
                            <h4 class="text-lg font-semibold text-gray-800"><%= selectedToy.getName() %></h4>
                            <p class="text-gray-600 text-sm mb-2"><%= selectedToy.getDescription() %></p>
                            <div class="flex justify-between items-center mb-4">
                                <span class="text-purple-600 font-bold">$<%= String.format("%.2f", selectedToy.getPrice()) %></span>
                                <span class="text-sm text-gray-500">Age: <%= selectedToy.getAgeRange() %>+</span>
                            </div>
                        </div>
                    </div>

                    <form action="<%= request.getContextPath() %>/wishlist" method="post">
                        <input type="hidden" name="action" value="<%= isEditingItem ? "updateItem" : "addItem" %>">
                        <input type="hidden" name="wishlistId" value="<%= wishlist.getId() %>">
                        <input type="hidden" name="toyId" value="<%= selectedToy.getId() %>">
                        <% if (isEditingItem && itemToEdit != null) { %>
                            <input type="hidden" name="itemId" value="<%= itemToEdit.getId() %>">
                        <% } %>

                        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <label for="priority" class="block text-gray-700 text-sm font-bold mb-2">Priority</label>
                                <select id="priority" name="priority" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                                    <option value="1" <%= isEditingItem && itemToEdit.getPriority() == 1 ? "selected" : "" %>>★★★★★ - Highest</option>
                                    <option value="2" <%= isEditingItem && itemToEdit.getPriority() == 2 ? "selected" : "" %>>★★★★☆ - High</option>
                                    <option value="3" <%= isEditingItem && itemToEdit.getPriority() == 3 ? "selected" : "" %> <%= !isEditingItem ? "selected" : "" %>>★★★☆☆ - Medium</option>
                                    <option value="4" <%= isEditingItem && itemToEdit.getPriority() == 4 ? "selected" : "" %>>★★☆☆☆ - Low</option>
                                    <option value="5" <%= isEditingItem && itemToEdit.getPriority() == 5 ? "selected" : "" %>>★☆☆☆☆ - Lowest</option>
                                </select>
                            </div>

                            <div>
                                <label for="notes" class="block text-gray-700 text-sm font-bold mb-2">Notes</label>
                                <input type="text" id="notes" name="notes" value="<%= isEditingItem && itemToEdit.getNotes() != null ? itemToEdit.getNotes() : "" %>"
                                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                            </div>
                        </div>

                        <div class="mt-6 flex justify-between">
                            <button type="submit"
                                    class="bg-purple-600 text-white py-2 px-6 rounded-md hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 transition-colors duration-300">
                                <%= isEditingItem ? "Update Item" : "Add to Wishlist" %>
                            </button>

                            <a href="<%= request.getContextPath() %>/wishlist/view/<%= wishlist.getId() %>" class="text-gray-600 hover:text-gray-800 py-2">
                                Cancel
                            </a>
                        </div>
                    </form>
                </section>
            <% } else if (isOwner && allToys != null) { %>
                <!-- Select Toy to Add Form -->
                <section class="bg-white rounded-lg shadow-md p-6 mb-8">
                    <h3 class="text-xl font-semibold mb-4">Add Toy to Wishlist</h3>

                    <form action="<%= request.getContextPath() %>/wishlist/add-item/<%= wishlist.getId() %>" method="get">
                        <div>
                            <label for="toyId" class="block text-gray-700 text-sm font-bold mb-2">Select a Toy</label>
                            <select id="toyId" name="toyId" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                                <option value="" disabled selected>Choose a toy to add</option>
                                <% for (Toy toy : allToys) {
                                    // Skip toys that are already in the wishlist
                                    boolean alreadyInWishlist = false;
                                    for (WishlistItem item : wishlist.getItems()) {
                                        if (item.getToyId().equals(toy.getId())) {
                                            alreadyInWishlist = true;
                                            break;
                                        }
                                    }

                                    if (!alreadyInWishlist) {
                                %>
                                    <option value="<%= toy.getId() %>"><%= toy.getName() %> - $<%= String.format("%.2f", toy.getPrice()) %></option>
                                <% }
                                } %>
                            </select>
                        </div>

                        <div class="mt-6">
                            <button type="submit"
                                    class="bg-purple-600 text-white py-2 px-6 rounded-md hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 transition-colors duration-300">
                                Next
                            </button>

                            <a href="<%= request.getContextPath() %>/wishlist/view/<%= wishlist.getId() %>" class="ml-4 text-gray-600 hover:text-gray-800">
                                Cancel
                            </a>
                        </div>
                    </form>
                </section>
            <% } %>

            <!-- Wishlist Items -->
            <section>
                <div class="flex justify-between items-center mb-6">
                    <h3 class="text-xl font-semibold text-gray-800">Wishlist Items</h3>

                    <% if (isOwner) { %>
                        <a href="<%= request.getContextPath() %>/wishlist/add-item/<%= wishlist.getId() %>" class="text-purple-600 hover:text-purple-800">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 inline-block mr-1" viewBox="0 0 20 20" fill="currentColor">
                                <path fill-rule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clip-rule="evenodd" />
                            </svg>
                            Add Item
                        </a>
                    <% } %>
                </div>

                <% if (wishlist.getItems().isEmpty()) { %>
                    <div class="bg-white p-8 rounded-lg shadow-md text-center">
                        <div class="text-gray-400 mb-4">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                            </svg>
                        </div>
                        <h3 class="text-xl font-semibold text-gray-800 mb-2">
                            This Wishlist is Empty
                        </h3>
                        <p class="text-gray-600 mb-6">
                            <% if (isOwner) { %>
                                Add your first toy to start building your wishlist!
                            <% } else { %>
                                This wishlist doesn't have any items yet.
                            <% } %>
                        </p>

                        <% if (isOwner) { %>
                            <a href="<%= request.getContextPath() %>/wishlist/add-item/<%= wishlist.getId() %>"
                               class="bg-purple-600 text-white px-6 py-2 rounded-md hover:bg-purple-700 transition-colors duration-300">
                                Add First Item
                            </a>
                        <% } %>
                    </div>
                <% } else { %>
                    <!-- Items Grid -->
                    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        <%
                        // Sort items by priority (1 = highest priority)
                        List<WishlistItem> sortedItems = new java.util.ArrayList<>(wishlist.getItems());
                        java.util.Collections.sort(sortedItems, new java.util.Comparator<WishlistItem>() {
                            public int compare(WishlistItem a, WishlistItem b) {
                                return Integer.compare(a.getPriority(), b.getPriority());
                            }
                        });

<<<<<<< HEAD
                        if (toyMap != null) { // Add null check for toyMap
                            for (WishlistItem item : sortedItems) {
                                Toy toy = toyMap.get(item.getToyId());
                                if (toy == null) continue;

                                // Generate stars based on priority
                                String stars = "";
                                for (int i = 1; i <= 5; i++) {
                                    if (i <= item.getPriority()) {
                                        stars += "☆";
                                    } else {
                                        stars += "★";
                                    }
                                }
=======
                        for (WishlistItem item : sortedItems) {
                            Toy toy = toyMap.get(item.getToyId());
                            if (toy == null) continue;

                            // Generate stars based on priority
                            String stars = "";
                            for (int i = 1; i <= 5; i++) {
                                if (i <= item.getPriority()) {
                                    stars += "☆";
                                } else {
                                    stars += "★";
                                }
                            }
>>>>>>> c2206e0557b49f6b5db23eb22406e94bd17c8809
                        %>
                        <div class="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
                            <div class="relative">
                                <img src="<%= toy.getImageUrl() != null && !toy.getImageUrl().isEmpty() ? toy.getImageUrl() : "/api/placeholder/300/200" %>"
                                     alt="<%= toy.getName() %>" class="w-full h-48 object-cover">

                                <div class="absolute top-2 right-2 bg-black bg-opacity-50 text-yellow-400 rounded px-2 py-1 text-xs">
                                    <%= stars %> <!-- Priority stars -->
                                </div>
                            </div>

                            <div class="p-4">
                                <div class="flex justify-between items-start mb-2">
                                    <h3 class="text-lg font-semibold text-gray-800"><%= toy.getName() %></h3>
                                    <span class="bg-purple-100 text-purple-800 text-xs px-2 py-1 rounded-full"><%= toy.getCategory() %></span>
                                </div>

                                <% if (item.getNotes() != null && !item.getNotes().isEmpty()) { %>
                                    <div class="bg-gray-100 p-2 rounded-md mb-3">
                                        <p class="text-gray-700 text-sm italic"><%= item.getNotes() %></p>
                                    </div>
                                <% } %>

                                <div class="flex justify-between items-center mb-4">
                                    <span class="text-purple-600 font-bold">$<%= String.format("%.2f", toy.getPrice()) %></span>
                                    <span class="text-sm text-gray-500">Added: <%= dateFormat.format(item.getAddedDate()) %></span>
                                </div>

                                <div class="flex space-x-2">
                                    <a href="<%= request.getContextPath() %>/payment?id=<%= toy.getId() %>"
                                       class="flex-1 bg-purple-600 text-white text-center py-2 rounded-md hover:bg-purple-700 transition-colors duration-300">
                                        Buy Now
                                    </a>

                                    <% if (isOwner) { %>
                                        <a href="<%= request.getContextPath() %>/wishlist/edit-item/<%= wishlist.getId() %>/<%= item.getId() %>"
                                           class="flex-1 bg-blue-600 text-white text-center py-2 rounded-md hover:bg-blue-700 transition-colors duration-300">
                                            Edit
                                        </a>
                                    <% } %>
                                </div>

                                <% if (isOwner) { %>
                                    <form action="<%= request.getContextPath() %>/wishlist" method="post" class="mt-2"
                                          onsubmit="return confirm('Are you sure you want to remove this item from your wishlist?');">
                                        <input type="hidden" name="action" value="removeItem">
                                        <input type="hidden" name="wishlistId" value="<%= wishlist.getId() %>">
                                        <input type="hidden" name="itemId" value="<%= item.getId() %>">
                                        <button type="submit" class="w-full text-red-600 text-sm hover:text-red-800 py-2">
                                            Remove
                                        </button>
                                    </form>
                                <% } %>
                            </div>
                        </div>
<<<<<<< HEAD
                        <% }
                        } else { %>
                            <div class="col-span-3 bg-white p-4 rounded-lg shadow-md text-center">
                                <p class="text-gray-600">There was an error loading the toys. Please try refreshing the page.</p>
                            </div>
=======
>>>>>>> c2206e0557b49f6b5db23eb22406e94bd17c8809
                        <% } %>
                    </div>
                <% } %>
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
                    The wishlist you're looking for doesn't exist or you don't have permission to view it.
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
</body>
</html>