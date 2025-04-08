package com.toystore.controller;

import com.toystore.model.User;
import com.toystore.util.FileHandler;

import java.util.List;
import java.util.UUID;

/**
 * Controller class for managing user-related operations.
 * Acts as an intermediary between the servlets and the data model.
 */
public class UserController {
    private String contextPath;

    /**
     * Constructor
     * @param contextPath The servlet context path
     */
    public UserController(String contextPath) {
        this.contextPath = contextPath;
    }

    /**
     * Authenticates a user with username and password
     * @param username The username
     * @param password The password
     * @return The user if authentication is successful, null otherwise
     */
    public User authenticateUser(String username, String password) {
        return FileHandler.authenticateUser(username, password, contextPath);
    }

    /**
     * Gets a user by username
     * @param username The username to search for
     * @return The user if found, null otherwise
     */
    public User getUserByUsername(String username) {
        return FileHandler.findUserByUsername(username, contextPath);
    }

    /**
     * Gets a user by ID
     * @param userId The user ID to search for
     * @return The user if found, null otherwise
     */
    public User getUserById(String userId) {
        return FileHandler.findUserById(userId, contextPath);
    }

    /**
     * Gets all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return FileHandler.loadUsers(contextPath);
    }

    /**
     * Adds a new user
     * @param user The user to add
     * @return true if successful, false otherwise
     */
    public boolean addUser(User user) {
        // Generate a unique ID if it's not set
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }

        List<User> users = FileHandler.loadUsers(contextPath);

        // Check if username already exists
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername())) {
                return false;
            }
        }

        users.add(user);
        return FileHandler.saveUsers(users, contextPath);
    }

    /**
     * Updates an existing user
     * @param updatedUser The user with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateUser(User updatedUser) {
        List<User> users = FileHandler.loadUsers(contextPath);

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(updatedUser.getId())) {
                // Check if username already exists (except for this user)
                for (User existingUser : users) {
                    if (!existingUser.getId().equals(updatedUser.getId()) &&
                            existingUser.getUsername().equals(updatedUser.getUsername())) {
                        return false;
                    }
                }

                users.set(i, updatedUser);
                return FileHandler.saveUsers(users, contextPath);
            }
        }

        return false;
    }

    /**
     * Removes a user
     * @param userId The ID of the user to remove
     * @return true if successful, false otherwise
     */
    public boolean removeUser(String userId) {
        List<User> users = FileHandler.loadUsers(contextPath);

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(userId)) {
                users.remove(i);
                return FileHandler.saveUsers(users, contextPath);
            }
        }

        return false;
    }
}