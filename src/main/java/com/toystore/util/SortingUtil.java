package com.toystore.util;

import com.toystore.model.Toy;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for sorting toys using various algorithms.
 * Implements selection sort algorithm for sorting toys by age range and price.
 */
public class SortingUtil {

    /**
     * Sorts toys by age range using selection sort algorithm
     * @param toys List of toys to sort
     * @return A new list with toys sorted by age range (ascending)
     */
    public static List<Toy> selectionSortByAgeRange(List<Toy> toys) {
        // Create a new list to avoid modifying the original
        List<Toy> sortedToys = new ArrayList<>(toys);
        int n = sortedToys.size();

        // Selection sort algorithm implementation
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum age range in the unsorted part
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (sortedToys.get(j).getAgeRange() < sortedToys.get(minIndex).getAgeRange()) {
                    minIndex = j;
                }
            }

            // Swap the found minimum element with the first element of the unsorted part
            if (minIndex != i) {
                Toy temp = sortedToys.get(i);
                sortedToys.set(i, sortedToys.get(minIndex));
                sortedToys.set(minIndex, temp);
            }
        }

        return sortedToys;
    }

    /**
     * Sorts toys by price using selection sort algorithm
     * @param toys List of toys to sort
     * @return A new list with toys sorted by price (ascending)
     */
    public static List<Toy> selectionSortByPrice(List<Toy> toys) {
        // Create a new list to avoid modifying the original
        List<Toy> sortedToys = new ArrayList<>(toys);
        int n = sortedToys.size();

        // Selection sort algorithm implementation
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum price in the unsorted part
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (sortedToys.get(j).getPrice() < sortedToys.get(minIndex).getPrice()) {
                    minIndex = j;
                }
            }

            // Swap the found minimum element with the first element of the unsorted part
            if (minIndex != i) {
                Toy temp = sortedToys.get(i);
                sortedToys.set(i, sortedToys.get(minIndex));
                sortedToys.set(minIndex, temp);
            }
        }

        return sortedToys;
    }

    /**
     * Sorts toys by age range and then by price (if ages are equal)
     * @param toys List of toys to sort
     * @return A new list with toys sorted by age range and price
     */
    public static List<Toy> selectionSortByAgeAndPrice(List<Toy> toys) {
        // Create a new list to avoid modifying the original
        List<Toy> sortedToys = new ArrayList<>(toys);
        int n = sortedToys.size();

        // Selection sort algorithm implementation
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in the unsorted part
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                // Compare age ranges first
                if (sortedToys.get(j).getAgeRange() < sortedToys.get(minIndex).getAgeRange()) {
                    minIndex = j;
                }
                // If age ranges are equal, compare prices
                else if (sortedToys.get(j).getAgeRange() == sortedToys.get(minIndex).getAgeRange() &&
                        sortedToys.get(j).getPrice() < sortedToys.get(minIndex).getPrice()) {
                    minIndex = j;
                }
            }

            // Swap the found minimum element with the first element of the unsorted part
            if (minIndex != i) {
                Toy temp = sortedToys.get(i);
                sortedToys.set(i, sortedToys.get(minIndex));
                sortedToys.set(minIndex, temp);
            }
        }

        return sortedToys;
    }
}