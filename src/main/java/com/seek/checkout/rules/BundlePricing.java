package com.seek.checkout.rules;

import com.seek.checkout.model.Customer;
import com.seek.checkout.model.Product;
import java.util.List;

public class BundlePricing implements PricingRule {

    private final String customerName;
    private final String product;
    private final int bundleSize;

    public BundlePricing(String customerName, String product, int bundleSize) {
        this.customerName = customerName;
        this.product = product;
        this.bundleSize = bundleSize;
    }

    @Override
    public List<Product> apply(Customer customer) {
        if (customer.getName().equals(customerName)) {
            List<Product> eligibleProducts = customer.getProducts().stream()
                    .filter(p -> p.getName().equals(product))
                    .toList();

            int freeItems = eligibleProducts.size() / bundleSize;
            for (int i = 0; i < freeItems; i++) {
                customer.getProducts().remove(eligibleProducts.get(i));
            }
        }
        return customer.getProducts();
    }
}
