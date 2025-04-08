package com.toystore.model;

import com.toystore.util.SortingUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements a linked list data structure to manage toy inventory.
 * Provides methods for adding, removing, searching, and sorting toys.
 */
public class ToyInventory {
    private ToyNode head;
    private int size;

    // Constructor
    public ToyInventory() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Adds a new toy to the inventory
     * @param toy The toy to add
     */
    public void addToy(Toy toy) {
        ToyNode newNode = new ToyNode(toy);

        if (head == null) {
            head = newNode;
        } else {
            ToyNode current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
        size++;
    }

    /**
     * Removes a toy from the inventory by its ID
     * @param toyId The ID of the toy to remove
     * @return true if toy was found and removed, false otherwise
     */
    public boolean removeToy(String toyId) {
        if (head == null) {
            return false;
        }

        // If the head node is the toy to remove
        if (head.getToy().getId().equals(toyId)) {
            head = head.getNext();
            size--;
            return true;
        }

        // Check the rest of the list
        ToyNode current = head;
        while (current.getNext() != null) {
            if (current.getNext().getToy().getId().equals(toyId)) {
                current.setNext(current.getNext().getNext());
                size--;
                return true;
            }
            current = current.getNext();
        }

        return false;
    }

    /**
     * Updates an existing toy's information
     * @param updatedToy The toy with updated information
     * @return true if toy was found and updated, false otherwise
     */
    public boolean updateToy(Toy updatedToy) {
        ToyNode current = head;

        while (current != null) {
            if (current.getToy().getId().equals(updatedToy.getId())) {
                current.setToy(updatedToy);
                return true;
            }
            current = current.getNext();
        }

        return false;
    }

    /**
     * Searches for a toy by its ID
     * @param toyId The ID of the toy to find
     * @return The toy if found, null otherwise
     */
    public Toy findToyById(String toyId) {
        ToyNode current = head;

        while (current != null) {
            if (current.getToy().getId().equals(toyId)) {
                return current.getToy();
            }
            current = current.getNext();
        }

        return null;
    }

    /**
     * Returns all toys in the inventory as a list
     * @return List of all toys
     */
    public List<Toy> getAllToys() {
        List<Toy> toyList = new ArrayList<>();
        ToyNode current = head;

        while (current != null) {
            toyList.add(current.getToy());
            current = current.getNext();
        }

        return toyList;
    }

    /**
     * Returns all toys from a specific seller
     * @param sellerId The ID of the seller
     * @return List of toys from the seller
     */
    public List<Toy> getToysBySeller(String sellerId) {
        List<Toy> sellerToys = new ArrayList<>();
        ToyNode current = head;

        while (current != null) {
            if (current.getToy().getSeller().equals(sellerId)) {
                sellerToys.add(current.getToy());
            }
            current = current.getNext();
        }

        return sellerToys;
    }

    /**
     * Returns all toys in a specific category
     * @param category The category to filter by
     * @return List of toys in the category
     */
    public List<Toy> getToysByCategory(String category) {
        List<Toy> categoryToys = new ArrayList<>();
        ToyNode current = head;

        while (current != null) {
            if (current.getToy().getCategory().equalsIgnoreCase(category)) {
                categoryToys.add(current.getToy());
            }
            current = current.getNext();
        }

        return categoryToys;
    }

    /**
     * Returns toys suitable for a specific age range
     * @param minAge The minimum age to filter by
     * @return List of toys suitable for the specified age
     */
    public List<Toy> getToysByAgeRange(int minAge) {
        List<Toy> ageToys = new ArrayList<>();
        ToyNode current = head;

        while (current != null) {
            if (current.getToy().getAgeRange() <= minAge) {
                ageToys.add(current.getToy());
            }
            current = current.getNext();
        }

        return ageToys;
    }

    /**
     * Sorts all toys by age range using selection sort algorithm
     * @return Sorted list of all toys by age range (ascending)
     */
    public List<Toy> getAllToysSortedByAgeRange() {
        List<Toy> toyList = getAllToys();
        return SortingUtil.selectionSortByAgeRange(toyList);
    }

    /**
     * Sorts all toys by price using selection sort algorithm
     * @return Sorted list of all toys by price (ascending)
     */
    public List<Toy> getAllToysSortedByPrice() {
        List<Toy> toyList = getAllToys();
        return SortingUtil.selectionSortByPrice(toyList);
    }

    /**
     * Returns the number of toys in the inventory
     * @return The size of the inventory
     */
    public int getSize() {
        return size;
    }

    /**
     * Checks if the inventory is empty
     * @return true if the inventory is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }
}