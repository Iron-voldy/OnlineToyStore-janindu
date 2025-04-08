package com.toystore.model;

import com.toystore.util.SortingUtil;
import java.util.ArrayList;
import java.util.List;

public class ToyInventory {
    private ToyNode head;
    private int size;

    // Constructor
    public ToyInventory() {
        this.head = null;
        this.size = 0;
    }

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

    public List<Toy> getAllToys() {
        List<Toy> toyList = new ArrayList<>();
        ToyNode current = head;

        while (current != null) {
            toyList.add(current.getToy());
            current = current.getNext();
        }

        return toyList;
    }

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

    public List<Toy> getAllToysSortedByAgeRange() {
        List<Toy> toyList = getAllToys();
        return SortingUtil.selectionSortByAgeRange(toyList);
    }

    public List<Toy> getAllToysSortedByPrice() {
        List<Toy> toyList = getAllToys();
        return SortingUtil.selectionSortByPrice(toyList);
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}