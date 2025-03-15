package com.seek.checkout.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {

    private String name;
    private List<Product> products;

    public Customer(String name) {
        this.name = name;
        this.products = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void setProducts(List<Product> products) {
        this.products = new ArrayList<>(products); // Defensive copy to avoid stale references
    }
}
