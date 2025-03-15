package com.seek.checkout.rules;

import com.seek.checkout.model.Customer;
import com.seek.checkout.model.Product;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Iterator;

public class PercentageOffPricing implements PricingRule {

    private final String customerName;
    private final BigDecimal threshold;
    private final BigDecimal discount;

    public PercentageOffPricing(String customerName, BigDecimal threshold, BigDecimal discount) {
        this.customerName = customerName;
        this.threshold = threshold;
        this.discount = discount;
    }

    @Override
    public List<Product> apply(Customer customer) {
        if (customer.getName().equalsIgnoreCase(customerName)) {

            // Ensure no zero-valued products:
            Iterator<Product> iterator = customer.getProducts().iterator();
            while (iterator.hasNext()) {
                Product product = iterator.next();
                if (product.getPrice().compareTo(BigDecimal.ZERO) == 0) {
                    iterator.remove(); //  remove stale objects
                }
            }

            BigDecimal total = customer.getProducts().stream()
                    .map(Product::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);

            System.out.println("Total before discount (excluding free products): " + total);

            if (total.compareTo(threshold) >= 0) {
                BigDecimal discountFactor = BigDecimal.valueOf(1)
                        .subtract(discount.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));

                for (int i = 0; i < customer.getProducts().size(); i++) {
                    Product product = customer.getProducts().get(i);
                    BigDecimal discountedPrice = product.getPrice()
                            .multiply(discountFactor)
                            .setScale(2, RoundingMode.HALF_UP);
                    customer.getProducts().set(i, new Product(product.getName(), discountedPrice));
                }
            }
        }
        return customer.getProducts();
    }
}
