package com.toystore.model;

public class ToyNode {
    private Toy toy;
    private ToyNode next;

    // Constructor
    public ToyNode(Toy toy) {
        this.toy = toy;
        this.next = null;
    }

    // Getters and Setters
    public Toy getToy() {
        return toy;
    }

    public void setToy(Toy toy) {
        this.toy = toy;
    }

    public ToyNode getNext() {
        return next;
    }

    public void setNext(ToyNode next) {
        this.next = next;
    }
}