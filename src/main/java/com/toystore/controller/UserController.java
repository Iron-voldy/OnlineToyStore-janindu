package com.toystore.controller;

import com.toystore.model.User;
import com.toystore.util.FileHandler;

import java.util.List;
import java.util.UUID;

public class UserController {
    private String contextPath;

    public UserController(String contextPath) {
        this.contextPath = contextPath;
    }

    public User authenticateUser(String username, String password) {
        return FileHandler.authenticateUser(username, password, contextPath);
    }

    public User getUserByUsername(String username) {
        return FileHandler.findUserByUsername(username, contextPath);
    }

    public User getUserById(String userId) {
        return FileHandler.findUserById(userId, contextPath);
    }

    public List<User> getAllUsers() {
        return FileHandler.loadUsers(contextPath);
    }

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