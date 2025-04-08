package com.toystore.controller;

import com.toystore.model.Toy;
import com.toystore.model.ToyInventory;
import com.toystore.util.FileHandler;
import com.toystore.util.SortingUtil;

import java.util.List;
import java.util.UUID;

public class ToyController {
    private ToyInventory inventory;
    private String contextPath;

    public ToyController(String contextPath) {
        this.contextPath = contextPath;
        this.inventory = new ToyInventory();
        loadToysFromFile();
    }

    private void loadToysFromFile() {
        List<Toy> toys = FileHandler.loadToys(contextPath);
        for (Toy toy : toys) {
            inventory.addToy(toy);
        }
    }

    private boolean saveToysToFile() {
        return FileHandler.saveToys(inventory.getAllToys(), contextPath);
    }

    public boolean addToy(Toy toy) {
        // Generate a unique ID if it's not set
        if (toy.getId() == null || toy.getId().isEmpty()) {
            toy.setId(UUID.randomUUID().toString());
        }

        inventory.addToy(toy);
        return saveToysToFile();
    }

    public boolean updateToy(Toy toy) {
        boolean success = inventory.updateToy(toy);
        if (success) {
            return saveToysToFile();
        }
        return false;
    }

    public boolean removeToy(String toyId) {
        boolean success = inventory.removeToy(toyId);
        if (success) {
            return saveToysToFile();
        }
        return false;
    }

    public Toy getToyById(String toyId) {
        return inventory.findToyById(toyId);
    }

    public List<Toy> getAllToys() {
        return inventory.getAllToys();
    }

    public List<Toy> getToysBySeller(String sellerId) {
        return inventory.getToysBySeller(sellerId);
    }

    public List<Toy> getToysByCategory(String category) {
        return inventory.getToysByCategory(category);
    }

    public List<Toy> getToysByAgeRange(int minAge) {
        return inventory.getToysByAgeRange(minAge);
    }

    public List<Toy> getAllToysSortedByAgeRange() {
        return inventory.getAllToysSortedByAgeRange();
    }

    public List<Toy> getAllToysSortedByPrice() {
        return inventory.getAllToysSortedByPrice();
    }

    public List<Toy> getAllToysSortedByAgeAndPrice() {
        List<Toy> toys = inventory.getAllToys();
        return SortingUtil.selectionSortByAgeAndPrice(toys);
    }
}