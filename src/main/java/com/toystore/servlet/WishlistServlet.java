package com.toystore.servlet;

import com.toystore.controller.ToyController;
import com.toystore.controller.UserController;
import com.toystore.controller.WishlistController;
import com.toystore.model.Toy;
import com.toystore.model.User;
import com.toystore.model.Wishlist;
import com.toystore.model.WishlistItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

<<<<<<< HEAD
@WebServlet({"/wishlist/*", "/wishlist"})
=======
@WebServlet("/wishlist/*")
>>>>>>> c2206e0557b49f6b5db23eb22406e94bd17c8809
public class WishlistServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Check if user is logged in
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

<<<<<<< HEAD
=======
        String pathInfo = request.getPathInfo();
>>>>>>> c2206e0557b49f6b5db23eb22406e94bd17c8809
        String contextPath = getServletContext().getRealPath("/");
        WishlistController wishlistController = new WishlistController(contextPath);
        ToyController toyController = new ToyController(contextPath);

<<<<<<< HEAD
        // Handle direct add-item request from home page
        String action = request.getParameter("action");
        if ("add-item".equals(action)) {
            String toyId = request.getParameter("toyId");

            if (toyId == null || toyId.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }

            // Get or create default wishlist
            List<Wishlist> userWishlists = wishlistController.getUserWishlists(user.getId());
            Wishlist defaultWishlist;

            if (userWishlists.isEmpty()) {
                // Create a default wishlist
                defaultWishlist = wishlistController.createWishlist(user.getId(), "My Wishlist", "Default wishlist", false);
                if (defaultWishlist == null) {
                    request.setAttribute("error", "Failed to create default wishlist");
                    response.sendRedirect(request.getContextPath() + "/wishlist");
                    return;
                }
            } else {
                // Use the first wishlist
                defaultWishlist = userWishlists.get(0);
            }

            // Add the toy to the default wishlist
            Toy toy = toyController.getToyById(toyId);
            if (toy != null) {
                boolean success = wishlistController.addItemToWishlist(defaultWishlist.getId(), toyId, "", 3);
                if (success) {
                    request.setAttribute("message", "Item added to wishlist successfully");
                } else {
                    request.setAttribute("error", "Failed to add item to wishlist");
                }
            }

            response.sendRedirect(request.getContextPath() + "/wishlist/view/" + defaultWishlist.getId());
            return;
        }

        String pathInfo = request.getPathInfo();

=======
>>>>>>> c2206e0557b49f6b5db23eb22406e94bd17c8809
        if (pathInfo == null || pathInfo.equals("/")) {
            // List all wishlists
            List<Wishlist> wishlists = wishlistController.getUserWishlists(user.getId());
            request.setAttribute("wishlists", wishlists);
            request.getRequestDispatcher("/wishlist.jsp").forward(request, response);
            return;
        }

        // View a specific wishlist
        if (pathInfo.startsWith("/view/")) {
            String wishlistId = pathInfo.substring("/view/".length());
            Wishlist wishlist = wishlistController.getWishlistById(wishlistId);

            if (wishlist == null) {
                request.setAttribute("error", "Wishlist not found");
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

            // Check if the user is allowed to view this wishlist
            if (!wishlist.getUserId().equals(user.getId()) && !wishlist.isPublic()) {
                request.setAttribute("error", "You don't have permission to view this wishlist");
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

            // Get the owner of the wishlist
            UserController userController = new UserController(contextPath);
            User owner = userController.getUserById(wishlist.getUserId());

            // Get toys for each wishlist item
            Map<String, Toy> toyMap = new HashMap<>();
            for (WishlistItem item : wishlist.getItems()) {
                Toy toy = toyController.getToyById(item.getToyId());
                if (toy != null) {
                    toyMap.put(item.getToyId(), toy);
                }
            }

            request.setAttribute("wishlist", wishlist);
            request.setAttribute("owner", owner);
<<<<<<< HEAD
            request.setAttribute("toyMap", toyMap);  // Make sure this is set
=======
            request.setAttribute("toyMap", toyMap);
>>>>>>> c2206e0557b49f6b5db23eb22406e94bd17c8809
            request.getRequestDispatcher("/wishlist-items.jsp").forward(request, response);
            return;
        }

        // Show form to create a new wishlist
        if (pathInfo.equals("/create")) {
            request.getRequestDispatcher("/wishlist.jsp").forward(request, response);
            return;
        }

        // Show form to edit a wishlist
        if (pathInfo.startsWith("/edit/")) {
            String wishlistId = pathInfo.substring("/edit/".length());
            Wishlist wishlist = wishlistController.getWishlistById(wishlistId);

            if (wishlist == null) {
                request.setAttribute("error", "Wishlist not found");
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

            // Check if the user is the owner of the wishlist
            if (!wishlist.getUserId().equals(user.getId())) {
                request.setAttribute("error", "You don't have permission to edit this wishlist");
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

            request.setAttribute("wishlist", wishlist);
            request.setAttribute("editing", true);
            request.getRequestDispatcher("/wishlist.jsp").forward(request, response);
            return;
        }

        // Form to add a toy to a wishlist
        if (pathInfo.startsWith("/add-item/")) {
            String wishlistId = pathInfo.substring("/add-item/".length());
            Wishlist wishlist = wishlistController.getWishlistById(wishlistId);

            if (wishlist == null) {
                request.setAttribute("error", "Wishlist not found");
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

            // Check if the user is the owner of the wishlist
            if (!wishlist.getUserId().equals(user.getId())) {
                request.setAttribute("error", "You don't have permission to add items to this wishlist");
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

            // Get the toy ID from the query parameter
            String toyId = request.getParameter("toyId");
            if (toyId != null && !toyId.isEmpty()) {
                Toy toy = toyController.getToyById(toyId);
                if (toy != null) {
                    request.setAttribute("toy", toy);
                }
            }

            request.setAttribute("wishlist", wishlist);
            request.setAttribute("allToys", toyController.getAllToys());
<<<<<<< HEAD

            // Ensure toyMap is set to prevent NullPointerException
            Map<String, Toy> toyMap = new HashMap<>();
            for (WishlistItem item : wishlist.getItems()) {
                Toy toy = toyController.getToyById(item.getToyId());
                if (toy != null) {
                    toyMap.put(item.getToyId(), toy);
                }
            }
            request.setAttribute("toyMap", toyMap);

=======
>>>>>>> c2206e0557b49f6b5db23eb22406e94bd17c8809
            request.getRequestDispatcher("/wishlist-items.jsp").forward(request, response);
            return;
        }

        // Edit a wishlist item
        if (pathInfo.startsWith("/edit-item/")) {
            String[] parts = pathInfo.substring("/edit-item/".length()).split("/");
            if (parts.length != 2) {
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

            String wishlistId = parts[0];
            String itemId = parts[1];

            Wishlist wishlist = wishlistController.getWishlistById(wishlistId);
            if (wishlist == null) {
                request.setAttribute("error", "Wishlist not found");
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

            // Check if the user is the owner of the wishlist
            if (!wishlist.getUserId().equals(user.getId())) {
                request.setAttribute("error", "You don't have permission to edit items in this wishlist");
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

            WishlistItem item = wishlistController.getWishlistItem(wishlistId, itemId);
            if (item == null) {
                request.setAttribute("error", "Item not found");
                response.sendRedirect(request.getContextPath() + "/wishlist/view/" + wishlistId);
                return;
            }

            Toy toy = toyController.getToyById(item.getToyId());
            if (toy != null) {
                request.setAttribute("toy", toy);
            }

            request.setAttribute("wishlist", wishlist);
            request.setAttribute("item", item);
            request.setAttribute("editing", true);
<<<<<<< HEAD

            // Get toys for each wishlist item to prevent NullPointerException
            Map<String, Toy> toyMap = new HashMap<>();
            for (WishlistItem wishlistItem : wishlist.getItems()) {
                Toy itemToy = toyController.getToyById(wishlistItem.getToyId());
                if (itemToy != null) {
                    toyMap.put(wishlistItem.getToyId(), itemToy);
                }
            }
            request.setAttribute("toyMap", toyMap);

=======
>>>>>>> c2206e0557b49f6b5db23eb22406e94bd17c8809
            request.getRequestDispatcher("/wishlist-items.jsp").forward(request, response);
            return;
        }

        // Share a wishlist
        if (pathInfo.startsWith("/share/")) {
            String wishlistId = pathInfo.substring("/share/".length());
            Wishlist wishlist = wishlistController.getWishlistById(wishlistId);

            if (wishlist == null) {
                request.setAttribute("error", "Wishlist not found");
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

            // Check if the user is the owner of the wishlist
            if (!wishlist.getUserId().equals(user.getId())) {
                request.setAttribute("error", "You don't have permission to share this wishlist");
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

            request.setAttribute("wishlist", wishlist);
            request.getRequestDispatcher("/wishlist-share.jsp").forward(request, response);
            return;
        }

        // Public wishlists
        if (pathInfo.equals("/public")) {
            List<Wishlist> publicWishlists = wishlistController.getPublicWishlists();
            request.setAttribute("publicWishlists", publicWishlists);

            // Get owner names for each wishlist
            UserController userController = new UserController(contextPath);
            Map<String, String> ownerNames = new HashMap<>();
            for (Wishlist wishlist : publicWishlists) {
                User owner = userController.getUserById(wishlist.getUserId());
                if (owner != null) {
                    ownerNames.put(wishlist.getId(), owner.getUsername());
                }
            }

            request.setAttribute("ownerNames", ownerNames);
            request.getRequestDispatcher("/wishlist.jsp").forward(request, response);
            return;
        }

        // Default: redirect to wishlist home
        response.sendRedirect(request.getContextPath() + "/wishlist");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Check if user is logged in
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String action = request.getParameter("action");
        String contextPath = getServletContext().getRealPath("/");
        WishlistController wishlistController = new WishlistController(contextPath);

        // Create a new wishlist
        if ("create".equals(action)) {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            boolean isPublic = "on".equals(request.getParameter("isPublic"));

            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("error", "Wishlist name is required");
                request.getRequestDispatcher("/wishlist.jsp").forward(request, response);
                return;
            }

            Wishlist wishlist = wishlistController.createWishlist(user.getId(), name, description, isPublic);
            if (wishlist != null) {
                response.sendRedirect(request.getContextPath() + "/wishlist/view/" + wishlist.getId());
            } else {
                request.setAttribute("error", "Failed to create wishlist");
                request.getRequestDispatcher("/wishlist.jsp").forward(request, response);
            }

            return;
        }

        // Update a wishlist
        if ("update".equals(action)) {
            String wishlistId = request.getParameter("wishlistId");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            boolean isPublic = "on".equals(request.getParameter("isPublic"));

            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("error", "Wishlist name is required");
                request.getRequestDispatcher("/wishlist.jsp").forward(request, response);
                return;
            }

            Wishlist wishlist = wishlistController.getWishlistById(wishlistId);
            if (wishlist == null) {
                request.setAttribute("error", "Wishlist not found");
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

            // Check if the user is the owner of the wishlist
            if (!wishlist.getUserId().equals(user.getId())) {
                request.setAttribute("error", "You don't have permission to update this wishlist");
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

            wishlist.setName(name);
            wishlist.setDescription(description);
            wishlist.setPublic(isPublic);

            boolean success = wishlistController.updateWishlist(wishlist);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/wishlist/view/" + wishlist.getId());
            } else {
                request.setAttribute("error", "Failed to update wishlist");
                request.getRequestDispatcher("/wishlist.jsp").forward(request, response);
            }

            return;
        }

        // Delete a wishlist
        if ("delete".equals(action)) {
            String wishlistId = request.getParameter("wishlistId");

            Wishlist wishlist = wishlistController.getWishlistById(wishlistId);
            if (wishlist == null) {
                request.setAttribute("error", "Wishlist not found");
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

            // Check if the user is the owner of the wishlist
            if (!wishlist.getUserId().equals(user.getId())) {
                request.setAttribute("error", "You don't have permission to delete this wishlist");
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

            boolean success = wishlistController.deleteWishlist(wishlistId);
            if (success) {
                request.setAttribute("message", "Wishlist deleted successfully");
            } else {
                request.setAttribute("error", "Failed to delete wishlist");
            }

            response.sendRedirect(request.getContextPath() + "/wishlist");
            return;
        }

        // Add item to wishlist
        if ("addItem".equals(action)) {
            String wishlistId = request.getParameter("wishlistId");
            String toyId = request.getParameter("toyId");
            String notes = request.getParameter("notes");
            int priority = 3; // Default medium priority

            try {
                priority = Integer.parseInt(request.getParameter("priority"));
            } catch (NumberFormatException | NullPointerException e) {
                // Use default priority
            }

            if (toyId == null || toyId.trim().isEmpty()) {
                request.setAttribute("error", "Please select a toy");
                response.sendRedirect(request.getContextPath() + "/wishlist/add-item/" + wishlistId);
                return;
            }

            boolean success = wishlistController.addItemToWishlist(wishlistId, toyId, notes, priority);
            if (success) {
                request.setAttribute("message", "Item added to wishlist successfully");
            } else {
                request.setAttribute("error", "Failed to add item to wishlist");
            }

            response.sendRedirect(request.getContextPath() + "/wishlist/view/" + wishlistId);
            return;
        }

        // Update wishlist item
        if ("updateItem".equals(action)) {
            String wishlistId = request.getParameter("wishlistId");
            String itemId = request.getParameter("itemId");
            String notes = request.getParameter("notes");
            int priority = 3; // Default medium priority

            try {
                priority = Integer.parseInt(request.getParameter("priority"));
            } catch (NumberFormatException | NullPointerException e) {
                // Use default priority
            }

            WishlistItem item = wishlistController.getWishlistItem(wishlistId, itemId);
            if (item == null) {
                request.setAttribute("error", "Item not found");
                response.sendRedirect(request.getContextPath() + "/wishlist/view/" + wishlistId);
                return;
            }

            item.setNotes(notes);
            item.setPriority(priority);

            boolean success = wishlistController.updateWishlistItem(item);
            if (success) {
                request.setAttribute("message", "Item updated successfully");
            } else {
                request.setAttribute("error", "Failed to update item");
            }

            response.sendRedirect(request.getContextPath() + "/wishlist/view/" + wishlistId);
            return;
        }

        // Remove item from wishlist
        if ("removeItem".equals(action)) {
            String wishlistId = request.getParameter("wishlistId");
            String itemId = request.getParameter("itemId");

            boolean success = wishlistController.removeItemFromWishlist(wishlistId, itemId);
            if (success) {
                request.setAttribute("message", "Item removed from wishlist successfully");
            } else {
                request.setAttribute("error", "Failed to remove item from wishlist");
            }

            response.sendRedirect(request.getContextPath() + "/wishlist/view/" + wishlistId);
            return;
        }

        // Update wishlist visibility (for sharing)
        if ("updateVisibility".equals(action)) {
            String wishlistId = request.getParameter("wishlistId");
            boolean isPublic = "on".equals(request.getParameter("isPublic"));

            Wishlist wishlist = wishlistController.getWishlistById(wishlistId);
            if (wishlist == null) {
                request.setAttribute("error", "Wishlist not found");
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

            // Check if the user is the owner of the wishlist
            if (!wishlist.getUserId().equals(user.getId())) {
                request.setAttribute("error", "You don't have permission to update this wishlist");
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

            wishlist.setPublic(isPublic);

            boolean success = wishlistController.updateWishlist(wishlist);
            if (success) {
                request.setAttribute("message", "Wishlist visibility updated successfully");
            } else {
                request.setAttribute("error", "Failed to update wishlist visibility");
            }

            response.sendRedirect(request.getContextPath() + "/wishlist/share/" + wishlistId);
            return;
        }

        // Default: redirect to wishlist home
        response.sendRedirect(request.getContextPath() + "/wishlist");
    }
}