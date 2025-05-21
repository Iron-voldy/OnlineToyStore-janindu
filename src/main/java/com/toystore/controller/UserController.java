package com.toystore.controller;

import com.toystore.model.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.nio.file.*;

public class UserController {
    private static final String USERS_FILE = "users.txt";
    private static final String DATA_DIR = "WEB-INF/data";
    private final String dataPath;
    private final File usersFile;
    private final File sourceUsersFile;

    public UserController() {
        this(null);
    }

    public UserController(String contextPath) {
        // Set up the data path for deployed application
        if (contextPath != null) {
            dataPath = contextPath + File.separator + DATA_DIR;

            // Also set up path to source directory for development
            String projectRoot = new File(contextPath).getParentFile().getParentFile().getParentFile().getAbsolutePath();
            String sourcePath = projectRoot + File.separator + "src" + File.separator + "main" +
                    File.separator + "webapp" + File.separator + DATA_DIR;
            sourceUsersFile = new File(sourcePath, USERS_FILE);
            System.out.println("Source users file path: " + sourceUsersFile.getAbsolutePath());
        } else {
            dataPath = System.getProperty("user.home") + File.separator + "toystore" + File.separator + "data";
            sourceUsersFile = null;
        }

        System.out.println("Data directory path: " + dataPath);

        // Create data directory if it doesn't exist
        File directory = new File(dataPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            System.out.println("Created data directory: " + directory.getAbsolutePath() + ", success: " + created);
            directory.setWritable(true, false);
            directory.setReadable(true, false);
        }

        // Create source directory if needed
        if (sourceUsersFile != null) {
            File sourceDir = sourceUsersFile.getParentFile();
            if (!sourceDir.exists()) {
                boolean created = sourceDir.mkdirs();
                System.out.println("Created source data directory: " + sourceDir.getAbsolutePath() + ", success: " + created);
            }
        }

        // Set up users file
        usersFile = new File(directory, USERS_FILE);
        try {
            initializeUsersFile();
        } catch (IOException e) {
            System.err.println("Error during initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private synchronized void initializeUsersFile() throws IOException {
        if (!usersFile.exists()) {
            // Check if source file exists and copy from it
            if (sourceUsersFile != null && sourceUsersFile.exists() && sourceUsersFile.length() > 0) {
                Files.copy(sourceUsersFile.toPath(), usersFile.toPath());
                System.out.println("Copied existing data from source users file");
                return;
            }

            // Create new users file
            try {
                boolean created = usersFile.createNewFile();
                System.out.println("Created users file: " + usersFile.getAbsolutePath() + ", success: " + created);
                usersFile.setWritable(true, false);
                usersFile.setReadable(true, false);

                // Create and save admin user
                User admin = new User(
                        UUID.randomUUID().toString(),
                        "admin",
                        "admin123",
                        "admin@toystore.com",
                        "Admin User",
                        "Admin Address",
                        "1234567890",
                        "admin"
                );
                List<User> users = new ArrayList<>();
                users.add(admin);
                saveUsers(users);
                System.out.println("Added admin user");
            } catch (IOException e) {
                System.err.println("Failed to create users file: " + e.getMessage());
                throw e;
            }
        }
    }

    public synchronized void addUser(User user) throws IOException {
        List<User> users = getAllUsers();
        users.add(user);
        saveUsers(users);
        System.out.println("Added user: " + user.getUsername());
    }

    public synchronized void updateUser(User user) throws IOException {
        List<User> users = getAllUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                saveUsers(users);
                System.out.println("Updated user: " + user.getUsername());
                break;
            }
        }
    }

    public User getUserByUsername(String username) {
        try {
            List<User> users = getAllUsers();
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    return user;
                }
            }
        } catch (IOException e) {
            System.err.println("Error getting user by username: " + e.getMessage());
        }
        return null;
    }

    public User getUserById(String id) {
        try {
            List<User> users = getAllUsers();
            for (User user : users) {
                if (user.getId().equals(id)) {
                    return user;
                }
            }
        } catch (IOException e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
        }
        return null;
    }

    public User authenticate(String username, String password) {
        try {
            List<User> users = getAllUsers();
            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    return user;
                }
            }
        } catch (IOException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        }
        return null;
    }

    public synchronized List<User> getAllUsers() throws IOException {
        List<User> users = new ArrayList<>();

        if (!usersFile.exists()) {
            initializeUsersFile();
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(usersFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    User user = User.fromFileString(line);
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        }
        return users;
    }

    private synchronized void saveUsers(List<User> users) throws IOException {
        // Save to deployed location
        Path tempPath = Files.createTempFile(usersFile.getParentFile().toPath(), "users", ".tmp");
        File tempFile = tempPath.toFile();
        tempFile.setWritable(true, false);

        // Write to temporary file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            for (User user : users) {
                writer.write(user.toFileString());
                writer.newLine();
            }
            writer.flush();
        }

        // Move temp file to actual file
        try {
            Files.move(tempPath, usersFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            System.out.println("Saved users to: " + usersFile.getAbsolutePath());

            // Also save to source directory if it exists
            if (sourceUsersFile != null) {
                try {
                    // Ensure source directory exists
                    sourceUsersFile.getParentFile().mkdirs();
                    Files.copy(usersFile.toPath(), sourceUsersFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Copied data to source users file: " + sourceUsersFile.getAbsolutePath());
                } catch (IOException e) {
                    System.out.println("Note: Could not copy to source file (this is normal in production): " + e.getMessage());
                }
            }
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(tempPath, usersFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Saved users using non-atomic move");
        } catch (IOException e) {
            System.err.println("Failed to move temp file to final location: " + e.getMessage());
            try {
                Files.deleteIfExists(tempPath);
            } catch (IOException deleteError) {
                System.err.println("Failed to delete temp file: " + deleteError.getMessage());
            }
            throw new IOException("Failed to save users file: " + e.getMessage());
        }
    }

    public synchronized boolean deleteUser(String userId) throws IOException {
        List<User> users = getAllUsers();
        boolean removed = false;

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(userId)) {
                users.remove(i);
                removed = true;
                break;
            }
        }

        if (removed) {
            saveUsers(users);
            System.out.println("Deleted user: " + userId);
        }
        return removed;
    }

    public List<User> searchUsers(String query) throws IOException {
        List<User> users = getAllUsers();
        List<User> results = new ArrayList<>();
        String lowercaseQuery = query.toLowerCase();

        for (User user : users) {
            if (user.getUsername().toLowerCase().contains(lowercaseQuery) ||
                    (user.getFullName() != null && user.getFullName().toLowerCase().contains(lowercaseQuery)) ||
                    (user.getEmail() != null && user.getEmail().toLowerCase().contains(lowercaseQuery))) {
                results.add(user);
            }
        }
        return results;
    }
}