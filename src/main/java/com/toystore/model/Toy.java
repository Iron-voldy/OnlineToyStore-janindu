package com.toystore.model;

public class Toy {
    private String id;
    private String name;
    private String description;
    private double price;
    private int ageRange;
    private String category;
    private String seller;
    private int quantity;
    private String imageUrl;

    // Constructors
    public Toy() {
    }

    public Toy(String id, String name, String description, double price,
               int ageRange, String category, String seller, int quantity, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.ageRange = ageRange;
        this.category = category;
        this.seller = seller;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters (Encapsulation)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(int ageRange) {
        this.ageRange = ageRange;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String toFileString() {
        return id + "," + name + "," + description + "," + price + "," +
                ageRange + "," + category + "," + seller + "," +
                quantity + "," + imageUrl;
    }

    public static Toy fromFileString(String data) {
        String[] parts = data.split(",");
        if (parts.length >= 9) {
            return new Toy(
                    parts[0],                 // id
                    parts[1],                 // name
                    parts[2],                 // description
                    Double.parseDouble(parts[3]), // price
                    Integer.parseInt(parts[4]),   // ageRange
                    parts[5],                 // category
                    parts[6],                 // seller
                    Integer.parseInt(parts[7]),   // quantity
                    parts[8]                  // imageUrl
            );
        }
        return null;
    }

    @Override
    public String toString() {
        return "Toy{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", ageRange=" + ageRange +
                ", category='" + category + '\'' +
                '}';
    }
}