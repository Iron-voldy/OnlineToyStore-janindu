package com.toystore.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Wishlist {
    private String id;
    private String userId;
    private String name;
    private String description;
    private boolean isPublic;
    private Date createdDate;
    private List<WishlistItem> items;

    // Constructors
    public Wishlist() {
        this.id = UUID.randomUUID().toString();
        this.createdDate = new Date();
        this.items = new ArrayList<>();
        this.isPublic = false;
    }

    public Wishlist(String id, String userId, String name, String description, boolean isPublic, Date createdDate) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.createdDate = createdDate;
        this.items = new ArrayList<>();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<WishlistItem> getItems() {
        return items;
    }

    public void setItems(List<WishlistItem> items) {
        this.items = items;
    }

    public void addItem(WishlistItem item) {
        this.items.add(item);
    }

    public boolean removeItem(String itemId) {
        return this.items.removeIf(item -> item.getId().equals(itemId));
    }

    public int getTotalItems() {
        return this.items.size();
    }

    /**
     * Converts a Wishlist object to a string representation for storage in text file
     * @return A string with wishlist data separated by commas
     */
    public String toFileString() {
        return id + "," + userId + "," + name + "," + description + "," +
                isPublic + "," + createdDate.getTime();
    }

    /**
     * Creates a Wishlist object from a string representation from the text file
     * @param data The string data from file
     * @return A new Wishlist object
     */
    public static Wishlist fromFileString(String data) {
        String[] parts = data.split(",");
        if (parts.length >= 6) {
            return new Wishlist(
                    parts[0],                      // id
                    parts[1],                      // userId
                    parts[2],                      // name
                    parts[3],                      // description
                    Boolean.parseBoolean(parts[4]), // isPublic
                    new Date(Long.parseLong(parts[5])) // createdDate
            );
        }
        return null;
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isPublic=" + isPublic +
                ", createdDate=" + createdDate +
                ", itemCount=" + (items != null ? items.size() : 0) +
                '}';
    }
}