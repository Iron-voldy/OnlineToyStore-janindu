package com.toystore.controller;

import com.toystore.model.Toy;
import com.toystore.model.Wishlist;
import com.toystore.model.WishlistItem;
import com.toystore.util.FileHandler;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class WishlistController {
    private String contextPath;

    public WishlistController(String contextPath) {
        this.contextPath = contextPath;
    }

    /**
     * Create a new wishlist for a user
     * @param userId The user ID
     * @param name Wishlist name
     * @param description Wishlist description
     * @param isPublic Whether the wishlist is public
     * @return The created wishlist, or null if creation failed
     */
    public Wishlist createWishlist(String userId, String name, String description, boolean isPublic) {
        // Create a new wishlist
        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(userId);
        wishlist.setName(name);
        wishlist.setDescription(description);
        wishlist.setPublic(isPublic);

        // Save the wishlist
        List<Wishlist> wishlists = FileHandler.loadWishlists(contextPath);
        wishlists.add(wishlist);
        boolean saved = FileHandler.saveWishlists(wishlists, contextPath);

        if (saved) {
            return wishlist;
        } else {
            return null;
        }
    }

    /**
     * Get all wishlists for a user
     * @param userId The user ID
     * @return List of the user's wishlists
     */
    public List<Wishlist> getUserWishlists(String userId) {
        return FileHandler.getWishlistsByUser(userId, contextPath);
    }

    /**
     * Get a wishlist by its ID
     * @param wishlistId The wishlist ID
     * @return The wishlist or null if not found
     */
    public Wishlist getWishlistById(String wishlistId) {
        return FileHandler.getWishlistById(wishlistId, contextPath);
    }

    /**
     * Get all public wishlists
     * @return List of public wishlists
     */
    public List<Wishlist> getPublicWishlists() {
        return FileHandler.getPublicWishlists(contextPath);
    }

    /**
     * Update an existing wishlist
     * @param wishlist The wishlist with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateWishlist(Wishlist wishlist) {
        List<Wishlist> wishlists = FileHandler.loadWishlists(contextPath);

        for (int i = 0; i < wishlists.size(); i++) {
            if (wishlists.get(i).getId().equals(wishlist.getId())) {
                // Preserve the items from the original wishlist
                List<WishlistItem> items = wishlists.get(i).getItems();
                wishlist.setItems(items);

                wishlists.set(i, wishlist);
                return FileHandler.saveWishlists(wishlists, contextPath);
            }
        }

        return false;
    }

    /**
     * Delete a wishlist
     * @param wishlistId The ID of the wishlist to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteWishlist(String wishlistId) {
        List<Wishlist> wishlists = FileHandler.loadWishlists(contextPath);

        for (int i = 0; i < wishlists.size(); i++) {
            if (wishlists.get(i).getId().equals(wishlistId)) {
                wishlists.remove(i);
                return FileHandler.saveWishlists(wishlists, contextPath);
            }
        }

        return false;
    }

    /**
     * Add a toy to a wishlist
     * @param wishlistId The wishlist ID
     * @param toyId The toy ID
     * @param notes Optional notes about the item
     * @param priority Priority level (1-5)
     * @return true if successful, false otherwise
     */
    public boolean addItemToWishlist(String wishlistId, String toyId, String notes, int priority) {
        Wishlist wishlist = getWishlistById(wishlistId);
        if (wishlist == null) {
            return false;
        }

        // Check if toy already exists in the wishlist
        for (WishlistItem item : wishlist.getItems()) {
            if (item.getToyId().equals(toyId)) {
                return false; // Item already exists in wishlist
            }
        }

        // Create a new wishlist item
        WishlistItem item = new WishlistItem();
        item.setWishlistId(wishlistId);
        item.setToyId(toyId);
        item.setNotes(notes);
        item.setPriority(priority);

        // Add the item to the wishlist
        wishlist.addItem(item);

        // Save all wishlists (which will also save the items)
        List<Wishlist> wishlists = FileHandler.loadWishlists(contextPath);
        for (int i = 0; i < wishlists.size(); i++) {
            if (wishlists.get(i).getId().equals(wishlistId)) {
                wishlists.set(i, wishlist);
                break;
            }
        }

        return FileHandler.saveWishlists(wishlists, contextPath);
    }

    /**
     * Update a wishlist item
     * @param item The wishlist item with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateWishlistItem(WishlistItem item) {
        Wishlist wishlist = getWishlistById(item.getWishlistId());
        if (wishlist == null) {
            return false;
        }

        // Find and update the item
        boolean found = false;
        for (int i = 0; i < wishlist.getItems().size(); i++) {
            if (wishlist.getItems().get(i).getId().equals(item.getId())) {
                wishlist.getItems().set(i, item);
                found = true;
                break;
            }
        }

        if (!found) {
            return false;
        }

        // Save the updated wishlist
        List<Wishlist> wishlists = FileHandler.loadWishlists(contextPath);
        for (int i = 0; i < wishlists.size(); i++) {
            if (wishlists.get(i).getId().equals(wishlist.getId())) {
                wishlists.set(i, wishlist);
                break;
            }
        }

        return FileHandler.saveWishlists(wishlists, contextPath);
    }

    /**
     * Remove an item from a wishlist
     * @param wishlistId The wishlist ID
     * @param itemId The wishlist item ID
     * @return true if successful, false otherwise
     */
    public boolean removeItemFromWishlist(String wishlistId, String itemId) {
        Wishlist wishlist = getWishlistById(wishlistId);
        if (wishlist == null) {
            return false;
        }

        // Remove the item
        if (!wishlist.removeItem(itemId)) {
            return false;
        }

        // Save the updated wishlist
        List<Wishlist> wishlists = FileHandler.loadWishlists(contextPath);
        for (int i = 0; i < wishlists.size(); i++) {
            if (wishlists.get(i).getId().equals(wishlistId)) {
                wishlists.set(i, wishlist);
                break;
            }
        }

        return FileHandler.saveWishlists(wishlists, contextPath);
    }

    /**
     * Get an item from a wishlist
     * @param wishlistId The wishlist ID
     * @param itemId The wishlist item ID
     * @return The wishlist item or null if not found
     */
    public WishlistItem getWishlistItem(String wishlistId, String itemId) {
        Wishlist wishlist = getWishlistById(wishlistId);
        if (wishlist == null) {
            return null;
        }

        for (WishlistItem item : wishlist.getItems()) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }

        return null;
    }
}