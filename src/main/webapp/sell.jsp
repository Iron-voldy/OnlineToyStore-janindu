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

    // Check if we're editing a toy
    Toy toyToEdit = (Toy) request.getAttribute("toy");
    boolean isEditing = toyToEdit != null;
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ToyLand - Sell Toys</title>
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
            <h2 class="text-3xl font-bold text-gray-800">Sell Your Toys</h2>
            <p class="text-gray-600">List your toys for sale on ToyLand</p>
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

        <!-- Toy Form Section -->
        <section class="bg-white rounded-lg shadow-md p-6 mb-8">
            <h3 class="text-xl font-semibold mb-4"><%= isEditing ? "Edit Toy" : "Add New Toy" %></h3>

            <form action="sell" method="post" id="toyForm">
                <% if (isEditing) { %>
                    <input type="hidden" name="id" value="<%= toyToEdit.getId() %>">
                <% } %>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div>
                        <label for="name" class="block text-gray-700 text-sm font-bold mb-2">Toy Name *</label>
                        <input type="text" id="name" name="name" required
                               value="<%= isEditing ? toyToEdit.getName() : "" %>"
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                    </div>

                    <div>
                        <label for="category" class="block text-gray-700 text-sm font-bold mb-2">Category *</label>
                        <select id="category" name="category" required
                                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                            <option value="" disabled <%= !isEditing ? "selected" : "" %>>Select a category</option>
                            <option value="Action Figures" <%= isEditing && "Action Figures".equals(toyToEdit.getCategory()) ? "selected" : "" %>>Action Figures</option>
                            <option value="Board Games" <%= isEditing && "Board Games".equals(toyToEdit.getCategory()) ? "selected" : "" %>>Board Games</option>
                            <option value="Building Blocks" <%= isEditing && "Building Blocks".equals(toyToEdit.getCategory()) ? "selected" : "" %>>Building Blocks</option>
                            <option value="Dolls" <%= isEditing && "Dolls".equals(toyToEdit.getCategory()) ? "selected" : "" %>>Dolls</option>
                            <option value="Educational" <%= isEditing && "Educational".equals(toyToEdit.getCategory()) ? "selected" : "" %>>Educational</option>
                            <option value="Electronic" <%= isEditing && "Electronic".equals(toyToEdit.getCategory()) ? "selected" : "" %>>Electronic</option>
                            <option value="Outdoor" <%= isEditing && "Outdoor".equals(toyToEdit.getCategory()) ? "selected" : "" %>>Outdoor</option>
                            <option value="Puzzles" <%= isEditing && "Puzzles".equals(toyToEdit.getCategory()) ? "selected" : "" %>>Puzzles</option>
                            <option value="Stuffed Animals" <%= isEditing && "Stuffed Animals".equals(toyToEdit.getCategory()) ? "selected" : "" %>>Stuffed Animals</option>
                            <option value="Vehicles" <%= isEditing && "Vehicles".equals(toyToEdit.getCategory()) ? "selected" : "" %>>Vehicles</option>
                        </select>
                    </div>

                    <div>
                        <label for="price" class="block text-gray-700 text-sm font-bold mb-2">Price ($) *</label>
                        <input type="number" id="price" name="price" step="0.01" min="0.01" required
                               value="<%= isEditing ? toyToEdit.getPrice() : "" %>"
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                    </div>

                    <div>
                        <label for="ageRange" class="block text-gray-700 text-sm font-bold mb-2">Age Range (years) *</label>
                        <input type="number" id="ageRange" name="ageRange" min="0" max="18" required
                               value="<%= isEditing ? toyToEdit.getAgeRange() : "" %>"
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                    </div>

                    <div>
                        <label for="quantity" class="block text-gray-700 text-sm font-bold mb-2">Quantity *</label>
                        <input type="number" id="quantity" name="quantity" min="1" required
                               value="<%= isEditing ? toyToEdit.getQuantity() : "1" %>"
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600">
                    </div>

                    <!-- Image Upload Section -->
                    <div>
                        <label class="block text-gray-700 text-sm font-bold mb-2">Toy Image</label>
                        <div class="flex items-center space-x-4">
                            <div class="flex-1">
                                <div class="relative border border-gray-300 rounded-md overflow-hidden">
                                    <input type="file" id="imageFile" accept="image/*"
                                           class="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
                                           onchange="handleImageUpload()">
                                    <div class="p-3 text-center bg-gray-100 hover:bg-gray-200 transition-colors duration-300">
                                        <span id="upload-text">Choose Image</span>
                                        <div id="upload-progress" class="hidden mt-2 h-2 bg-gray-200 rounded-full">
                                            <div id="progress-bar" class="h-full bg-purple-600 rounded-full" style="width: 0%"></div>
                                        </div>
                                    </div>
                                </div>
                                <div id="upload-error" class="text-red-600 text-xs mt-1 hidden"></div>
                            </div>

                            <div class="w-24 h-24 border border-gray-300 rounded-md overflow-hidden">
                                <img id="image-preview" src="<%= isEditing && toyToEdit.getImageUrl() != null && !toyToEdit.getImageUrl().isEmpty() && !toyToEdit.getImageUrl().startsWith("/api/placeholder") ? toyToEdit.getImageUrl() : "/api/placeholder/96/96" %>"
                                     alt="Toy image preview" class="w-full h-full object-cover">
                            </div>
                        </div>
                        <input type="hidden" id="imageUrl" name="imageUrl"
                               value="<%= isEditing && toyToEdit.getImageUrl() != null ? toyToEdit.getImageUrl() : "" %>">
                        <p class="text-xs text-gray-500 mt-1">Max size: 5MB. Formats: JPG, PNG, GIF</p>
                    </div>

                    <div class="md:col-span-2">
                        <label for="description" class="block text-gray-700 text-sm font-bold mb-2">Description</label>
                        <textarea id="description" name="description" rows="4"
                                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-600"><%= isEditing && toyToEdit.getDescription() != null ? toyToEdit.getDescription() : "" %></textarea>
                    </div>
                </div>

                <div class="mt-6">
                    <button type="submit"
                            class="bg-purple-600 text-white py-2 px-6 rounded-md hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 transition-colors duration-300">
                        <%= isEditing ? "Update Toy" : "Add Toy" %>
                    </button>

                    <% if (isEditing) { %>
                        <a href="sell" class="ml-4 text-gray-600 hover:text-gray-800">Cancel</a>
                    <% } %>
                </div>
            </form>
        </section>

        <!-- Your Listed Toys Section -->
        <section>
            <div class="flex justify-between items-center mb-6">
                <h3 class="text-xl font-semibold text-gray-800">Your Listed Toys</h3>
            </div>

            <% if (userToys == null || userToys.isEmpty()) { %>
                <div class="bg-gray-100 p-8 rounded-lg text-center">
                    <p class="text-gray-600 text-lg">You haven't listed any toys for sale yet.</p>
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

    <!-- JavaScript for image upload -->
    <script>
        function handleImageUpload() {
            const fileInput = document.getElementById('imageFile');
            const file = fileInput.files[0];
            const uploadText = document.getElementById('upload-text');
            const uploadProgress = document.getElementById('upload-progress');
            const progressBar = document.getElementById('progress-bar');
            const imagePreview = document.getElementById('image-preview');
            const imageUrlInput = document.getElementById('imageUrl');
            const uploadError = document.getElementById('upload-error');

            // Reset error message
            uploadError.classList.add('hidden');
            uploadError.textContent = '';

            if (!file) return;

            // Validate file size (max 5MB)
            if (file.size > 5 * 1024 * 1024) {
                uploadError.textContent = 'File size exceeds 5MB limit';
                uploadError.classList.remove('hidden');
                return;
            }

            // Validate file type
            const fileType = file.type.toLowerCase();
            if (!fileType.startsWith('image/jpeg') &&
                !fileType.startsWith('image/png') &&
                !fileType.startsWith('image/gif')) {
                uploadError.textContent = 'Only JPG, PNG, and GIF files are allowed';
                uploadError.classList.remove('hidden');
                return;
            }

            // Show upload progress
            uploadText.textContent = 'Uploading...';
            uploadProgress.classList.remove('hidden');

            // Create form data
            const formData = new FormData();
            formData.append('file', file);

            // Create XHR request
            const xhr = new XMLHttpRequest();
            xhr.open('POST', 'upload', true);

            // Handle progress
            xhr.upload.onprogress = function(e) {
                if (e.lengthComputable) {
                    const percentComplete = (e.loaded / e.total) * 100;
                    progressBar.style.width = percentComplete + '%';
                }
            };

            // Handle response
            xhr.onload = function() {
                if (xhr.status === 200) {
                    // Success - update the image URL input and preview
                    imageUrlInput.value = xhr.responseText;
                    imagePreview.src = xhr.responseText;
                    uploadText.textContent = 'Upload Complete';
                } else {
                    // Error
                    uploadError.textContent = 'Upload failed: ' + xhr.statusText;
                    uploadError.classList.remove('hidden');
                    uploadText.textContent = 'Choose Image';
                }
                uploadProgress.classList.add('hidden');
                progressBar.style.width = '0%';
            };

            // Handle error
            xhr.onerror = function() {
                uploadError.textContent = 'Upload failed. Please try again.';
                uploadError.classList.remove('hidden');
                uploadText.textContent = 'Choose Image';
                uploadProgress.classList.add('hidden');
                progressBar.style.width = '0%';
            };

            // Show preview image
            const reader = new FileReader();
            reader.onload = function(e) {
                imagePreview.src = e.target.result;
            };
            reader.readAsDataURL(file);

            // Send the upload
            xhr.send(formData);
        }
    </script>
</body>
</html>