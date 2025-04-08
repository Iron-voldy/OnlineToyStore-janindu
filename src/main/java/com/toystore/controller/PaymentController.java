package com.toystore.controller;

import com.toystore.model.Payment;
import com.toystore.model.Toy;
import com.toystore.model.User;
import com.toystore.util.FileHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PaymentController {
    private String contextPath;

    public PaymentController(String contextPath) {
        this.contextPath = contextPath;
    }

    public Payment processPayment(User user, Toy toy, int quantity, String cardNumber) {
        // Validate toy quantity
        if (toy.getQuantity() < quantity) {
            return null; // Not enough inventory
        }

        // Calculate payment amount
        double amount = toy.getPrice() * quantity;

        // Create a secure representation of card number (last 4 digits only)
        String secureCardNumber = "xxxx-xxxx-xxxx-" + cardNumber.substring(cardNumber.length() - 4);

        // Create a new payment
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID().toString());
        payment.setUserId(user.getId());
        payment.setToyId(toy.getId());
        payment.setAmount(amount);
        payment.setQuantity(quantity);
        payment.setCardNumber(secureCardNumber);
        payment.setPaymentDate(new Date());
        payment.setStatus("completed");
        payment.setTransactionId("TXN" + System.currentTimeMillis());

        // Save the payment
        boolean saved = FileHandler.savePayment(payment, contextPath);
        if (!saved) {
            return null;
        }

        // Update toy quantity
        toy.setQuantity(toy.getQuantity() - quantity);
        ToyController toyController = new ToyController(contextPath);
        toyController.updateToy(toy);

        return payment;
    }

    public List<Payment> getPaymentsByUser(String userId) {
        List<Payment> allPayments = FileHandler.loadPayments(contextPath);
        List<Payment> userPayments = new ArrayList<>();

        for (Payment payment : allPayments) {
            if (payment.getUserId().equals(userId)) {
                userPayments.add(payment);
            }
        }

        return userPayments;
    }

    public Payment getPaymentById(String paymentId) {
        List<Payment> payments = FileHandler.loadPayments(contextPath);

        for (Payment payment : payments) {
            if (payment.getId().equals(paymentId)) {
                return payment;
            }
        }

        return null;
    }

    public List<Payment> getAllPayments() {
        return FileHandler.loadPayments(contextPath);
    }
}