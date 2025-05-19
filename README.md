# 🧸 ToyLand - Online Toy Store 🧸

## 📖 Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Technical Implementation](#technical-implementation)
  - [Architecture](#architecture)
  - [Data Structures & Algorithms](#data-structures--algorithms)
  - [Object-Oriented Programming](#object-oriented-programming)
  - [CRUD Operations](#crud-operations)
- [Project Structure](#project-structure)
- [How to Run](#how-to-run)
- [User Guide](#user-guide)
- [Admin Features](#admin-features)
- [Future Enhancements](#future-enhancements)

## 🚀 Introduction

Welcome to ToyLand, a comprehensive online toy store web application built using Java Servlets, JSP, and custom data structures. This application allows users to browse, purchase, and sell toys while implementing core computer science concepts including linked lists and sorting algorithms.

ToyLand demonstrates the practical application of Object-Oriented Programming principles and Data Structures & Algorithms in a real-world e-commerce scenario. The application features a complete CRUD system, user authentication, payment processing, and responsive design.

## ✨ Features

### User Features
- 🔐 User registration and authentication
- 🏠 Dynamic homepage with categorized toy listings
- 🔍 Advanced filtering and sorting of toys
- 🛒 Secure toy purchasing system
- 💰 Complete payment processing
- 📊 Purchase history tracking
- 🛍️ Ability to sell toys (add, edit, delete listings)
- 👤 User profile management
- ⭐ Product rating and feedback system
- 🔄 Logout functionality

### Admin Features
- 👑 Administrative dashboard
- 📊 View all user transactions
- 🔍 Monitor system activity

## 💻 Technical Implementation

### Architecture
The application follows the **Model-View-Controller (MVC)** architectural pattern:
- 📋 **Model**: Java classes representing data entities (User, Toy, Payment)
- 🖥️ **View**: JSP pages for the user interface
- 🎮 **Controller**: Servlet classes handling HTTP requests and business logic

### Data Structures & Algorithms

#### 🔄 Linked List Implementation
The project uses a custom linked list implementation for toy inventory management:
- `ToyInventory` class stores toys using a linked list structure
- `ToyNode` class represents each node in the linked list
- Operations include adding, finding, updating, and removing toys
- Benefits: Dynamic allocation, efficient insertions, and deletions

```java
// Example of the linked list structure
public class ToyInventory {
    private ToyNode head;
    
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
    }
    
    // Other methods for CRUD operations on the linked list
}
```

#### 🔄 Sorting Algorithms
The project implements **Selection Sort** for organizing toy displays:
- `selectionSortByAgeRange()`: Sorts toys by the minimum age recommendation
- `selectionSortByPrice()`: Sorts toys by price (lowest to highest)
- `selectionSortByAgeAndPrice()`: Sorts toys by age first, then by price if ages are equal

```java
// Example of selection sort by age range
public static List<Toy> selectionSortByAgeRange(List<Toy> toys) {
    List<Toy> sortedToys = new ArrayList<>(toys);
    int n = sortedToys.size();
    
    for (int i = 0; i < n - 1; i++) {
        int minIndex = i;
        for (int j = i + 1; j < n; j++) {
            if (sortedToys.get(j).getAgeRange() < sortedToys.get(minIndex).getAgeRange()) {
                minIndex = j;
            }
        }
        
        if (minIndex != i) {
            Toy temp = sortedToys.get(i);
            sortedToys.set(i, sortedToys.get(minIndex));
            sortedToys.set(minIndex, temp);
        }
    }
    
    return sortedToys;
}
```

### Object-Oriented Programming

The project demonstrates core OOP principles:

#### 🧩 Encapsulation
- Private data fields with public getters/setters in model classes
- Data hiding and access control
- Example: `User` class encapsulates user data with controlled access

#### 🔄 Inheritance
- Class hierarchies for extending functionality
- Common base behavior with specialized implementations

#### 🧠 Polymorphism
- Method overloading for flexible functionality
- Appropriate method implementations for different contexts

#### 📝 Abstraction
- Simplified interfaces hiding complex implementations
- Separation of concerns for better modularity
- Example: `FileHandler` abstracts away file I/O operations

### CRUD Operations

The application implements full Create, Read, Update, Delete operations:

#### ✨ Create
- User registration
- Adding new toys for sale
- Creating payment records

```java
// Example of creating a toy
public boolean addToy(Toy toy) {
    // Generate a unique ID if it's not set
    if (toy.getId() == null || toy.getId().isEmpty()) {
        toy.setId(UUID.randomUUID().toString());
    }
    
    inventory.addToy(toy);
    return saveToysToFile();
}
```

#### 📖 Read
- Browsing toys by category
- Viewing user profile
- Checking purchase history

```java
// Example of reading toys by category
public List<Toy> getToysByCategory(String category) {
    return inventory.getToysByCategory(category);
}
```

#### 🔄 Update
- Modifying toy listings
- Updating user profile
- Editing quantities

```java
// Example of updating a toy
public boolean updateToy(Toy toy) {
    boolean success = inventory.updateToy(toy);
    if (success) {
        return saveToysToFile();
    }
    return false;
}
```

#### 🗑️ Delete
- Removing toy listings
- Canceling orders (in future versions)

```java
// Example of deleting a toy
public boolean removeToy(String toyId) {
    boolean success = inventory.removeToy(toyId);
    if (success) {
        return saveToysToFile();
    }
    return false;
}
```

## 📁 Project Structure

The project follows Maven's standard directory structure:

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── toystore/
│   │           ├── controller/   # Servlet controllers
│   │           ├── model/        # Data models
│   │           ├── servlet/      # HTTP request handlers
│   │           └── util/         # Utility classes
│   ├── webapp/
│   │   ├── WEB-INF/
│   │   │   ├── data/           # Data storage
│   │   │   └── web.xml         # Web configuration
│   │   ├── css/               # Stylesheets
│   │   └── *.jsp              # JSP view pages
└── test/                     # Test cases
```

### Key Components:

- **Models**: `User.java`, `Toy.java`, `Payment.java`, `ToyInventory.java`
- **Controllers**: `UserController.java`, `ToyController.java`, `PaymentController.java`
- **Views**: `home.jsp`, `profile.jsp`, `payment.jsp`, `sell.jsp`
- **Utilities**: `FileHandler.java`, `SortingUtil.java`

## 🚀 How to Run

1. **Prerequisites**:
   - Java 8 or higher
   - Apache Tomcat 9.x
   - Maven

2. **Build**:
   ```
   mvn clean package
   ```

3. **Deploy**:
   - Copy the generated WAR file from the `target` directory to Tomcat's `webapps` directory
   - Or use Maven's Tomcat plugin: `mvn tomcat7:run`

4. **Access**:
   - Open a web browser and navigate to: `http://localhost:8080/OnlineToyStore`

## 📚 User Guide

### Registration & Login
- Create a new account or log in using existing credentials
- Default admin credentials: username `admin`, password `admin123`

### Browsing & Shopping
- Browse toys on the homepage
- Filter by category or age range
- Sort by price, age, or both
- Click "Buy" to purchase toys

### Selling Toys
- Navigate to the "Sell" page
- Fill in toy details and upload an image
- Manage your listings in the same interface

### User Profile
- View and update your personal information
- Change your password
- See your purchase history

### Feedback
- Rate toys after purchase
- Provide comments and feedback

## 👑 Admin Features

As an admin:
- View all transactions across the platform
- Monitor user activities
- Access comprehensive sales data

## 🔮 Future Enhancements

Planned improvements:
- 🔍 Advanced search functionality
- 📦 Order tracking system
- 💬 User-to-user messaging
- 📊 Sales analytics dashboard
- 📱 Mobile application
- 🌐 Internationalization support

---

## 🧑‍💻 Contributors
- Hasindu Wanninayake

## 📝 License
This project is for educational purposes only.
