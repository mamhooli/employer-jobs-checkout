
package com.seek.checkout.rules;

import com.seek.checkout.model.Customer;
import com.seek.checkout.model.Product;
import java.math.BigDecimal;
import java.util.List;

public class DiscountPricing implements PricingRule {

    private final String customerName;
    private final String product;
    private final BigDecimal price;

    public DiscountPricing(String customerName, String product, BigDecimal price) {
        this.customerName = customerName;
        this.product = product;
        this.price = price;
    }

    @Override
    public List<Product> apply(Customer customer) {
        if (customer.getName().equals(customerName)) {
            for (int i = 0; i < customer.getProducts().size(); i++) {
                if (customer.getProducts().get(i).getName().equals(product)) {
                    customer.getProducts().set(i, new Product(product, price));
                }
            }
        }
        return customer.getProducts();
    }
}
