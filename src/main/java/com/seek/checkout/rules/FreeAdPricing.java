package com.seek.checkout.rules;

import com.seek.checkout.model.Customer;
import com.seek.checkout.model.Product;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

public class FreeAdPricing implements PricingRule {

    private final String customerName;
    private final String triggerProduct;
    private final int minQuantity;
    private final String bonusProduct;

    public FreeAdPricing(String customerName, String triggerProduct, int minQuantity, String bonusProduct) {
        this.customerName = customerName;
        this.triggerProduct = triggerProduct;
        this.minQuantity = minQuantity;
        this.bonusProduct = bonusProduct;
    }

    @Override
    public List<Product> apply(Customer customer) {
        if (customer.getName().equalsIgnoreCase(customerName)) {
            long triggerCount = customer.getProducts().stream()
                    .filter(p -> p.getName().equals(triggerProduct))
                    .count();

            boolean alreadyHasBonus = customer.getProducts().stream()
                    .anyMatch(p -> p.getName().equals(bonusProduct) && p.getPrice().compareTo(BigDecimal.ZERO) == 0);

            if (triggerCount >= minQuantity && !alreadyHasBonus) {
                System.out.println("Adding free product: " + bonusProduct);

                // Create a new detached object:
                Product freeProduct = new Product(new String(bonusProduct), BigDecimal.ZERO);

                customer.getProducts().add(freeProduct);

                //  Using Iterator to avoid reference mismatch:
                Iterator<Product> iterator = customer.getProducts().iterator();
                boolean removed = false;
                while (iterator.hasNext()) {
                    Product product = iterator.next();
                    if (product.getName().equals(bonusProduct) && product.getPrice().compareTo(BigDecimal.ZERO) == 0) {
                        iterator.remove(); //  Remove the matching product
                        removed = true;
                        break;
                    }
                }

                if (removed) {
                    System.out.println("Free product removed successfully.");
                } else {
                    System.out.println("Failed to remove free product.");
                }
            }
        }
        return customer.getProducts();
    }
}
