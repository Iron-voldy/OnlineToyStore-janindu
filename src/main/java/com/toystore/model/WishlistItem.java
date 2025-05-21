package com.toystore.model;

import java.util.Date;
import java.util.UUID;

public class WishlistItem {
    private String id;
    private String wishlistId;
    private String toyId;
    private String notes;
    private int priority; // 1-5, where 1 is highest priority
    private Date addedDate;

    // Constructors
    public WishlistItem() {
        this.id = UUID.randomUUID().toString();
        this.addedDate = new Date();
        this.priority = 3; // Default priority (medium)
    }

    public WishlistItem(String id, String wishlistId, String toyId, String notes, int priority, Date addedDate) {
        this.id = id;
        this.wishlistId = wishlistId;
        this.toyId = toyId;
        this.notes = notes;
        this.priority = priority;
        this.addedDate = addedDate;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(String wishlistId) {
        this.wishlistId = wishlistId;
    }

    public String getToyId() {
        return toyId;
    }

    public void setToyId(String toyId) {
        this.toyId = toyId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        // Ensure priority is between 1 and 5
        this.priority = Math.max(1, Math.min(5, priority));
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    /**
     * Converts a WishlistItem object to a string representation for storage in text file
     * @return A string with wishlist item data separated by commas
     */
    public String toFileString() {
        return id + "," + wishlistId + "," + toyId + "," + notes + "," +
                priority + "," + addedDate.getTime();
    }

    /**
     * Creates a WishlistItem object from a string representation from the text file
     * @param data The string data from file
     * @return A new WishlistItem object
     */
    public static WishlistItem fromFileString(String data) {
        String[] parts = data.split(",");
        if (parts.length >= 6) {
            return new WishlistItem(
                    parts[0],                      // id
                    parts[1],                      // wishlistId
                    parts[2],                      // toyId
                    parts[3],                      // notes
                    Integer.parseInt(parts[4]),    // priority
                    new Date(Long.parseLong(parts[5])) // addedDate
            );
        }
        return null;
    }

    @Override
    public String toString() {
        return "WishlistItem{" +
                "id='" + id + '\'' +
                ", wishlistId='" + wishlistId + '\'' +
                ", toyId='" + toyId + '\'' +
                ", notes='" + notes + '\'' +
                ", priority=" + priority +
                ", addedDate=" + addedDate +
                '}';
    }
}