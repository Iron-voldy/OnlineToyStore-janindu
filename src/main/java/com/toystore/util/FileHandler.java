package com.toystore.util;

import com.toystore.model.Toy;
import com.toystore.model.User;

import javax.servlet.ServletContext;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Utility class for handling file operations.
 * Provides methods for reading from and writing to text files.
 */
public class FileHandler {
    // Modified to use WEB-INF/data with dynamic path resolution
    private static final String DATA_DIRECTORY = "WEB-INF/data/";
    private static final String TOYS_FILE_NAME = "toys.txt";
    private static final String USERS_FILE_NAME = "users.txt";

    private static ServletContext servletContext;

    /**
     * Sets the ServletContext for file operations
     * @param context The ServletContext
     */
    public static void setServletContext(ServletContext context) {
        servletContext = context;
        // Create directories when context is set
        initializeDirectories();
    }

    /**
     * Initialize directories
     */
    private static void initializeDirectories() {
        if (servletContext != null) {
            String webInfDataPath = "/WEB-INF/data";
            File dataDir = new File(servletContext.getRealPath(webInfDataPath));
            if (!dataDir.exists()) {
                boolean created = dataDir.mkdirs();
                System.out.println("Created WEB-INF/data directory: " + dataDir.getAbsolutePath() + " - Success: " + created);
            }
        } else {
            // Fallback to simple data directory if not in web context
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                boolean created = dataDir.mkdirs();
                System.out.println("Created fallback data directory: data - Success: " + created);
            }
        }
    }

    /**
     * Gets the path to the toys data file
     * @param context The context path (will be ignored if ServletContext is set)
     * @return The path to the toys data file
     */
    private static String getToysFilePath(String context) {
        if (servletContext != null) {
            return servletContext.getRealPath("/WEB-INF/data") + File.separator + TOYS_FILE_NAME;
        } else {
            // If servletContext is not set, use the provided context or fallback
            if (context != null && !context.isEmpty()) {
                return context + DATA_DIRECTORY + TOYS_FILE_NAME;
            } else {
                return "data" + File.separator + TOYS_FILE_NAME;
            }
        }
    }

    /**
     * Gets the path to the users data file
     * @param context The context path (will be ignored if ServletContext is set)
     * @return The path to the users data file
     */
    private static String getUsersFilePath(String context) {
        if (servletContext != null) {
            return servletContext.getRealPath("/WEB-INF/data") + File.separator + USERS_FILE_NAME;
        } else {
            // If servletContext is not set, use the provided context or fallback
            if (context != null && !context.isEmpty()) {
                return context + DATA_DIRECTORY + USERS_FILE_NAME;
            } else {
                return "data" + File.separator + USERS_FILE_NAME;
            }
        }
    }

    /**
     * Loads all toys from the toys.txt file
     * @param context The servlet context path (optional, used as fallback)
     * @return List of all toys
     */
    public static List<Toy> loadToys(String context) {
        List<Toy> toys = new ArrayList<>();
        String filePath = getToysFilePath(context);

        try {
            // Ensure directory exists
            File file = new File(filePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Create file if it doesn't exist
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Created toys file: " + filePath);
                return toys; // Return empty list for a new file
            }

            // Read all lines from the file
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    Toy toy = Toy.fromFileString(line);
                    if (toy != null) {
                        toys.add(toy);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading toys: " + e.getMessage());
            e.printStackTrace();
        }

        return toys;
    }

    /**
     * Saves all toys to the toys.txt file
     * @param toys The list of toys to save
     * @param context The servlet context path (optional, used as fallback)
     * @return true if successful, false otherwise
     */
    public static boolean saveToys(List<Toy> toys, String context) {
        String filePath = getToysFilePath(context);

        try {
            // Ensure directory exists
            File file = new File(filePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Write all toys to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (Toy toy : toys) {
                    writer.write(toy.toFileString());
                    writer.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving toys: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds a new toy to the toys.txt file
     * @param toy The toy to add
     * @param context The servlet context path (optional, used as fallback)
     * @return true if successful, false otherwise
     */
    public static boolean addToy(Toy toy, String context) {
        // Generate a unique ID if it's not set
        if (toy.getId() == null || toy.getId().isEmpty()) {
            toy.setId(UUID.randomUUID().toString());
        }

        List<Toy> toys = loadToys(context);
        toys.add(toy);
        return saveToys(toys, context);
    }

    /**
     * Updates an existing toy in the toys.txt file
     * @param updatedToy The toy with updated information
     * @param context The servlet context path (optional, used as fallback)
     * @return true if successful, false otherwise
     */
    public static boolean updateToy(Toy updatedToy, String context) {
        List<Toy> toys = loadToys(context);

        for (int i = 0; i < toys.size(); i++) {
            if (toys.get(i).getId().equals(updatedToy.getId())) {
                toys.set(i, updatedToy);
                return saveToys(toys, context);
            }
        }

        return false;
    }

    /**
     * Removes a toy from the toys.txt file
     * @param toyId The ID of the toy to remove
     * @param context The servlet context path (optional, used as fallback)
     * @return true if successful, false otherwise
     */
    public static boolean removeToy(String toyId, String context) {
        List<Toy> toys = loadToys(context);

        for (int i = 0; i < toys.size(); i++) {
            if (toys.get(i).getId().equals(toyId)) {
                toys.remove(i);
                return saveToys(toys, context);
            }
        }

        return false;
    }

    /**
     * Loads all users from the users.txt file
     * @param context The servlet context path (optional, used as fallback)
     * @return List of all users
     */
    public static List<User> loadUsers(String context) {
        List<User> users = new ArrayList<>();
        String filePath = getUsersFilePath(context);

        try {
            // Ensure directory exists
            File file = new File(filePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Create file if it doesn't exist
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Created users file: " + filePath);

                // Add a default admin user for testing
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

                // Write the admin user to the file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(admin.toFileString());
                    writer.newLine();
                }

                users.add(admin);
                return users;
            }

            // Read all lines from the file
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    User user = User.fromFileString(line);
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Saves all users to the users.txt file
     * @param users The list of users to save
     * @param context The servlet context path (optional, used as fallback)
     * @return true if successful, false otherwise
     */
    public static boolean saveUsers(List<User> users, String context) {
        String filePath = getUsersFilePath(context);

        try {
            // Ensure directory exists
            File file = new File(filePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Write all users to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (User user : users) {
                    writer.write(user.toFileString());
                    writer.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Finds a user by username
     * @param username The username to search for
     * @param context The servlet context path (optional, used as fallback)
     * @return The user if found, null otherwise
     */
    public static User findUserByUsername(String username, String context) {
        List<User> users = loadUsers(context);

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    /**
     * Finds a user by ID
     * @param userId The user ID to search for
     * @param context The servlet context path (optional, used as fallback)
     * @return The user if found, null otherwise
     */
    public static User findUserById(String userId, String context) {
        List<User> users = loadUsers(context);

        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }

        return null;
    }

    /**
     * Authenticates a user with username and password
     * @param username The username
     * @param password The password
     * @param context The servlet context path (optional, used as fallback)
     * @return The user if authentication is successful, null otherwise
     */
    public static User authenticateUser(String username, String password, String context) {
        List<User> users = loadUsers(context);

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }
}