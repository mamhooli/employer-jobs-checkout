package com.seek.checkout;

import com.seek.checkout.model.Customer;
import com.seek.checkout.model.Product;
import com.seek.checkout.rules.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Checkout {

    private List<PricingRule> pricingRules;

    public Checkout() {
        pricingRules = PricingRuleFactory.getAllRules();

        if (pricingRules == null || pricingRules.isEmpty()) {
            System.out.println("No pricing rules found! Initializing empty list.");
            pricingRules = new ArrayList<>();
        }
    }

    public void add(Customer customer, Product product) {
        customer.addProduct(new Product(product.getName(), product.getPrice()));
    }

    public BigDecimal total(Customer customer) {
        List<Product> products = new ArrayList<>(customer.getProducts());

        for (PricingRule rule : pricingRules) {
            products = new ArrayList<>(rule.apply(customer));
            customer.setProducts(products); // Force state update
        }

        // Use latest state for calculation:
        products = customer.getProducts();

        return products.stream()
                .filter(p -> p.getPrice().compareTo(BigDecimal.ZERO) > 0)
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
