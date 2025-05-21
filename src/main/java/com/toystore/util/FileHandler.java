package com.toystore.util;

import com.toystore.model.Payment;
import com.toystore.model.Toy;
import com.toystore.model.User;
import com.toystore.model.Wishlist;
import com.toystore.model.WishlistItem;

import javax.servlet.ServletContext;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileHandler {
    // Modified to use WEB-INF/data with dynamic path resolution
    private static final String DATA_DIRECTORY = "WEB-INF/data/";
    private static final String TOYS_FILE_NAME = "toys.txt";
    private static final String USERS_FILE_NAME = "users.txt";
    private static final String PAYMENTS_FILE_NAME = "payments.txt";  // Added for payments
    private static final String WISHLISTS_FILE_NAME = "wishlists.txt";
    private static final String WISHLIST_ITEMS_FILE_NAME = "wishlist_items.txt";

    private static ServletContext servletContext;

    public static void setServletContext(ServletContext context) {
        servletContext = context;
        // Create directories when context is set
        initializeDirectories();
    }

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

    private static String getPaymentsFilePath(String context) {
        if (servletContext != null) {
            return servletContext.getRealPath("/WEB-INF/data") + File.separator + PAYMENTS_FILE_NAME;
        } else {
            // If servletContext is not set, use the provided context or fallback
            if (context != null && !context.isEmpty()) {
                return context + DATA_DIRECTORY + PAYMENTS_FILE_NAME;
            } else {
                return "data" + File.separator + PAYMENTS_FILE_NAME;
            }
        }
    }

    private static String getWishlistsFilePath(String context) {
        if (servletContext != null) {
            return servletContext.getRealPath("/WEB-INF/data") + File.separator + WISHLISTS_FILE_NAME;
        } else {
            // If servletContext is not set, use the provided context or fallback
            if (context != null && !context.isEmpty()) {
                return context + DATA_DIRECTORY + WISHLISTS_FILE_NAME;
            } else {
                return "data" + File.separator + WISHLISTS_FILE_NAME;
            }
        }
    }

    private static String getWishlistItemsFilePath(String context) {
        if (servletContext != null) {
            return servletContext.getRealPath("/WEB-INF/data") + File.separator + WISHLIST_ITEMS_FILE_NAME;
        } else {
            // If servletContext is not set, use the provided context or fallback
            if (context != null && !context.isEmpty()) {
                return context + DATA_DIRECTORY + WISHLIST_ITEMS_FILE_NAME;
            } else {
                return "data" + File.separator + WISHLIST_ITEMS_FILE_NAME;
            }
        }
    }

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

    public static List<Payment> loadPayments(String context) {
        List<Payment> payments = new ArrayList<>();
        String filePath = getPaymentsFilePath(context);

        try {
            // Ensure directory exists
            File file = new File(filePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Create file if it doesn't exist
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Created payments file: " + filePath);
                return payments; // Return empty list for a new file
            }

            // Read all lines from the file
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    Payment payment = Payment.fromFileString(line);
                    if (payment != null) {
                        payments.add(payment);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading payments: " + e.getMessage());
            e.printStackTrace();
        }

        return payments;
    }


    public static boolean savePayment(Payment payment, String context) {
        List<Payment> payments = loadPayments(context);
        payments.add(payment);
        return savePayments(payments, context);
    }

    public static boolean savePayments(List<Payment> payments, String context) {
        String filePath = getPaymentsFilePath(context);

        try {
            // Ensure directory exists
            File file = new File(filePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Write all payments to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (Payment payment : payments) {
                    writer.write(payment.toFileString());
                    writer.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving payments: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static User findUserByUsername(String username, String context) {
        List<User> users = loadUsers(context);

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    public static User findUserById(String userId, String context) {
        List<User> users = loadUsers(context);

        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }

        return null;
    }

    public static User authenticateUser(String username, String password, String context) {
        List<User> users = loadUsers(context);

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

    // Wishlist methods

    public static List<Wishlist> loadWishlists(String context) {
        List<Wishlist> wishlists = new ArrayList<>();
        String filePath = getWishlistsFilePath(context);

        try {
            // Ensure directory exists
            File file = new File(filePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Create file if it doesn't exist
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Created wishlists file: " + filePath);
                return wishlists; // Return empty list for a new file
            }

            // Read all lines from the file
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    Wishlist wishlist = Wishlist.fromFileString(line);
                    if (wishlist != null) {
                        wishlists.add(wishlist);
                    }
                }
            }

            // Load wishlist items for each wishlist
            List<WishlistItem> allItems = loadWishlistItems(context);
            for (WishlistItem item : allItems) {
                for (Wishlist wishlist : wishlists) {
                    if (wishlist.getId().equals(item.getWishlistId())) {
                        wishlist.addItem(item);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading wishlists: " + e.getMessage());
            e.printStackTrace();
        }

        return wishlists;
    }

    public static boolean saveWishlists(List<Wishlist> wishlists, String context) {
        String filePath = getWishlistsFilePath(context);

        try {
            // Ensure directory exists
            File file = new File(filePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Write all wishlists to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (Wishlist wishlist : wishlists) {
                    writer.write(wishlist.toFileString());
                    writer.newLine();
                }
            }

            // Collect all wishlist items
            List<WishlistItem> allItems = new ArrayList<>();
            for (Wishlist wishlist : wishlists) {
                allItems.addAll(wishlist.getItems());
            }

            // Save all wishlist items
            return saveWishlistItems(allItems, context);
        } catch (IOException e) {
            System.err.println("Error saving wishlists: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<WishlistItem> loadWishlistItems(String context) {
        List<WishlistItem> items = new ArrayList<>();
        String filePath = getWishlistItemsFilePath(context);

        try {
            // Ensure directory exists
            File file = new File(filePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Create file if it doesn't exist
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Created wishlist items file: " + filePath);
                return items; // Return empty list for a new file
            }

            // Read all lines from the file
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    WishlistItem item = WishlistItem.fromFileString(line);
                    if (item != null) {
                        items.add(item);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading wishlist items: " + e.getMessage());
            e.printStackTrace();
        }

        return items;
    }

    public static boolean saveWishlistItems(List<WishlistItem> items, String context) {
        String filePath = getWishlistItemsFilePath(context);

        try {
            // Ensure directory exists
            File file = new File(filePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Write all wishlist items to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (WishlistItem item : items) {
                    writer.write(item.toFileString());
                    writer.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving wishlist items: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<Wishlist> getWishlistsByUser(String userId, String context) {
        List<Wishlist> allWishlists = loadWishlists(context);
        List<Wishlist> userWishlists = new ArrayList<>();

        for (Wishlist wishlist : allWishlists) {
            if (wishlist.getUserId().equals(userId)) {
                userWishlists.add(wishlist);
            }
        }

        return userWishlists;
    }

    public static Wishlist getWishlistById(String wishlistId, String context) {
        List<Wishlist> wishlists = loadWishlists(context);

        for (Wishlist wishlist : wishlists) {
            if (wishlist.getId().equals(wishlistId)) {
                return wishlist;
            }
        }

        return null;
    }

    public static List<Wishlist> getPublicWishlists(String context) {
        List<Wishlist> allWishlists = loadWishlists(context);
        List<Wishlist> publicWishlists = new ArrayList<>();

        for (Wishlist wishlist : allWishlists) {
            if (wishlist.isPublic()) {
                publicWishlists.add(wishlist);
            }
        }

        return publicWishlists;
    }
}