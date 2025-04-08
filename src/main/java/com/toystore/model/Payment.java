package com.toystore.model;

import java.util.Date;

public class Payment {
    private String id;
    private String userId;
    private String toyId;
    private double amount;
    private int quantity;
    private String cardNumber;
    private Date paymentDate;
    private String status;
    private String transactionId;

    // Constructors
    public Payment() {
    }

    public Payment(String id, String userId, String toyId, double amount, int quantity,
                   String cardNumber, Date paymentDate, String status, String transactionId) {
        this.id = id;
        this.userId = userId;
        this.toyId = toyId;
        this.amount = amount;
        this.quantity = quantity;
        this.cardNumber = cardNumber;
        this.paymentDate = paymentDate;
        this.status = status;
        this.transactionId = transactionId;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToyId() {
        return toyId;
    }

    public void setToyId(String toyId) {
        this.toyId = toyId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Converts a Payment object to a string representation for storage in text file
     * @return A string with payment data separated by commas
     */
    public String toFileString() {
        return id + "," + userId + "," + toyId + "," + amount + "," +
                quantity + "," + cardNumber + "," + paymentDate.getTime() + "," +
                status + "," + transactionId;
    }

    /**
     * Creates a Payment object from a string representation from the text file
     * @param data The string data from file
     * @return A new Payment object
     */
    public static Payment fromFileString(String data) {
        String[] parts = data.split(",");
        if (parts.length >= 9) {
            return new Payment(
                    parts[0],                      // id
                    parts[1],                      // userId
                    parts[2],                      // toyId
                    Double.parseDouble(parts[3]),  // amount
                    Integer.parseInt(parts[4]),    // quantity
                    parts[5],                      // cardNumber
                    new Date(Long.parseLong(parts[6])), // paymentDate
                    parts[7],                      // status
                    parts[8]                       // transactionId
            );
        }
        return null;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", toyId='" + toyId + '\'' +
                ", amount=" + amount +
                ", quantity=" + quantity +
                ", cardNumber='" + cardNumber + '\'' +
                ", paymentDate=" + paymentDate +
                ", status='" + status + '\'' +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}