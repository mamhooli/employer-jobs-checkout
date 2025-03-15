
package com.seek.checkout.rules;

import com.seek.checkout.model.Customer;
import com.seek.checkout.model.Product;
import java.util.List;

public interface PricingRule {
    List<Product> apply(Customer customer);
}
