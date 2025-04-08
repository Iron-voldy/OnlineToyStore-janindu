package com.toystore.controller;

import com.toystore.model.Toy;
import com.toystore.model.ToyInventory;
import com.toystore.util.FileHandler;
import com.toystore.util.SortingUtil;

import java.util.List;
import java.util.UUID;

/**
 * Controller class for managing toy-related operations.
 * Acts as an intermediary between the servlets and the data model.
 */
public class ToyController {
    private ToyInventory inventory;
    private String contextPath;

    /**
     * Constructor
     * @param contextPath The servlet context path
     */
    public ToyController(String contextPath) {
        this.contextPath = contextPath;
        this.inventory = new ToyInventory();
        loadToysFromFile();
    }

    /**
     * Loads toys from the file into the inventory
     */
    private void loadToysFromFile() {
        List<Toy> toys = FileHandler.loadToys(contextPath);
        for (Toy toy : toys) {
            inventory.addToy(toy);
        }
    }

    /**
     * Saves the current inventory to the file
     * @return true if successful, false otherwise
     */
    private boolean saveToysToFile() {
        return FileHandler.saveToys(inventory.getAllToys(), contextPath);
    }

    /**
     * Adds a new toy to the inventory
     * @param toy The toy to add
     * @return true if successful, false otherwise
     */
    public boolean addToy(Toy toy) {
        // Generate a unique ID if it's not set
        if (toy.getId() == null || toy.getId().isEmpty()) {
            toy.setId(UUID.randomUUID().toString());
        }

        inventory.addToy(toy);
        return saveToysToFile();
    }

    /**
     * Updates an existing toy in the inventory
     * @param toy The toy with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateToy(Toy toy) {
        boolean success = inventory.updateToy(toy);
        if (success) {
            return saveToysToFile();
        }
        return false;
    }

    /**
     * Removes a toy from the inventory
     * @param toyId The ID of the toy to remove
     * @return true if successful, false otherwise
     */
    public boolean removeToy(String toyId) {
        boolean success = inventory.removeToy(toyId);
        if (success) {
            return saveToysToFile();
        }
        return false;
    }

    /**
     * Gets a toy by its ID
     * @param toyId The ID of the toy to find
     * @return The toy if found, null otherwise
     */
    public Toy getToyById(String toyId) {
        return inventory.findToyById(toyId);
    }

    /**
     * Gets all toys in the inventory
     * @return List of all toys
     */
    public List<Toy> getAllToys() {
        return inventory.getAllToys();
    }

    /**
     * Gets all toys from a specific seller
     * @param sellerId The ID of the seller
     * @return List of toys from the seller
     */
    public List<Toy> getToysBySeller(String sellerId) {
        return inventory.getToysBySeller(sellerId);
    }

    /**
     * Gets all toys in a specific category
     * @param category The category to filter by
     * @return List of toys in the category
     */
    public List<Toy> getToysByCategory(String category) {
        return inventory.getToysByCategory(category);
    }

    /**
     * Gets toys suitable for a specific age range
     * @param minAge The minimum age to filter by
     * @return List of toys suitable for the specified age
     */
    public List<Toy> getToysByAgeRange(int minAge) {
        return inventory.getToysByAgeRange(minAge);
    }

    /**
     * Gets all toys sorted by age range
     * @return Sorted list of all toys by age range
     */
    public List<Toy> getAllToysSortedByAgeRange() {
        return inventory.getAllToysSortedByAgeRange();
    }

    /**
     * Gets all toys sorted by price
     * @return Sorted list of all toys by price
     */
    public List<Toy> getAllToysSortedByPrice() {
        return inventory.getAllToysSortedByPrice();
    }

    /**
     * Gets all toys sorted by age range and then by price
     * @return Sorted list of all toys by age range and price
     */
    public List<Toy> getAllToysSortedByAgeAndPrice() {
        List<Toy> toys = inventory.getAllToys();
        return SortingUtil.selectionSortByAgeAndPrice(toys);
    }
}