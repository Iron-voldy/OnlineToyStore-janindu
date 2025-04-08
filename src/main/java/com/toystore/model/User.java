package com.toystore.model;

public class User {
    private String id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String address;
    private String phone;
    private String userType; // "regular" or "admin"

    // Constructors
    public User() {
    }

    public User(String id, String username, String password, String email,
                String fullName, String address, String phone, String userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.userType = userType;
    }

    // Getters and Setters (Encapsulation)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(userType);
    }

    public String toFileString() {
        return id + "," + username + "," + password + "," + email + "," +
                fullName + "," + address + "," + phone + "," + userType;
    }

    public static User fromFileString(String data) {
        String[] parts = data.split(",");
        if (parts.length >= 8) {
            return new User(
                    parts[0], // id
                    parts[1], // username
                    parts[2], // password
                    parts[3], // email
                    parts[4], // fullName
                    parts[5], // address
                    parts[6], // phone
                    parts[7]  // userType
            );
        }
        return null;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}