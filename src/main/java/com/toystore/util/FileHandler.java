package com.toystore.util;

import com.toystore.model.Toy;
import com.toystore.model.User;

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
    private static final String DATA_DIRECTORY = "WEB-INF/data/";
    private static final String TOYS_FILE = DATA_DIRECTORY + "toys.txt";
    private static final String USERS_FILE = DATA_DIRECTORY + "users.txt";

    /**
     * Loads all toys from the toys.txt file
     * @param context The servlet context path to locate the file
     * @return List of all toys
     */
    public static List<Toy> loadToys(String context) {
        List<Toy> toys = new ArrayList<>();
        String filePath = context + TOYS_FILE;

        try {
            // Create directory if it doesn't exist
            Files.createDirectories(Paths.get(context + DATA_DIRECTORY));

            // Create file if it doesn't exist
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            // Read all lines from the file
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                Toy toy = Toy.fromFileString(line);
                if (toy != null) {
                    toys.add(toy);
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return toys;
    }

    /**
     * Saves all toys to the toys.txt file
     * @param toys The list of toys to save
     * @param context The servlet context path to locate the file
     * @return true if successful, false otherwise
     */
    public static boolean saveToys(List<Toy> toys, String context) {
        String filePath = context + TOYS_FILE;

        try {
            // Create directory if it doesn't exist
            Files.createDirectories(Paths.get(context + DATA_DIRECTORY));

            // Write all toys to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            for (Toy toy : toys) {
                writer.write(toy.toFileString());
                writer.newLine();
            }

            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds a new toy to the toys.txt file
     * @param toy The toy to add
     * @param context The servlet context path to locate the file
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
     * @param context The servlet context path to locate the file
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
     * @param context The servlet context path to locate the file
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
     * @param context The servlet context path to locate the file
     * @return List of all users
     */
    public static List<User> loadUsers(String context) {
        List<User> users = new ArrayList<>();
        String filePath = context + USERS_FILE;

        try {
            // Create directory if it doesn't exist
            Files.createDirectories(Paths.get(context + DATA_DIRECTORY));

            // Create file if it doesn't exist
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            // Read all lines from the file
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                User user = User.fromFileString(line);
                if (user != null) {
                    users.add(user);
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Saves all users to the users.txt file
     * @param users The list of users to save
     * @param context The servlet context path to locate the file
     * @return true if successful, false otherwise
     */
    public static boolean saveUsers(List<User> users, String context) {
        String filePath = context + USERS_FILE;

        try {
            // Create directory if it doesn't exist
            Files.createDirectories(Paths.get(context + DATA_DIRECTORY));

            // Write all users to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            for (User user : users) {
                writer.write(user.toFileString());
                writer.newLine();
            }

            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Finds a user by username
     * @param username The username to search for
     * @param context The servlet context path to locate the file
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
     * @param context The servlet context path to locate the file
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
     * @param context The servlet context path to locate the file
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