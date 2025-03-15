
package com.seek.checkout.rules;

import com.seek.checkout.model.Customer;
import com.seek.checkout.model.Product;
import java.util.List;

public class SecondBitePricing implements PricingRule {

    @Override
    public List<Product> apply(Customer customer) {
        if (customer.getName().equals("SecondBite")) {
            List<Product> classicAds = customer.getProducts().stream()
                    .filter(p -> p.getName().equals("classic"))
                    .toList();

            int freeAds = classicAds.size() / 3;
            for (int i = 0; i < freeAds; i++) {
                customer.getProducts().remove(classicAds.get(i));
            }
        }
        return customer.getProducts();
    }
}
