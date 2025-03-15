package com.seek.checkout;

import com.seek.checkout.loader.RuleLoader;
import com.seek.checkout.model.Customer;
import com.seek.checkout.model.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckoutTest {

    private static Checkout checkout;

    @BeforeAll
    static void setUp() {
        RuleLoader.loadRules("src/test/resources/rules.json");
        checkout = new Checkout();
    }



    @Test
    void testDefaultCustomer() {
        Customer customer = new Customer("Default");
        checkout.add(customer, new Product("classic", new BigDecimal("269.99")));
        checkout.add(customer, new Product("standout", new BigDecimal("322.99")));
        checkout.add(customer, new Product("premium", new BigDecimal("394.99")));

        BigDecimal expectedTotal = new BigDecimal("987.97");
        assertEquals(expectedTotal, checkout.total(customer));
    }

    @Test
    void testSecondBiteCustomer() {
        Customer customer = new Customer("SecondBite");
        checkout.add(customer, new Product("classic", new BigDecimal("269.99")));
        checkout.add(customer, new Product("classic", new BigDecimal("269.99")));
        checkout.add(customer, new Product("classic", new BigDecimal("269.99")));
        checkout.add(customer, new Product("premium", new BigDecimal("394.99")));

        BigDecimal expectedTotal = new BigDecimal("934.97");
        assertEquals(expectedTotal, checkout.total(customer));
    }

    @Test
    void testAxilCoffeeRoastersCustomer() {
        Customer customer = new Customer("Axil Coffee Roasters");
        checkout.add(customer, new Product("standout", new BigDecimal("322.99")));
        checkout.add(customer, new Product("standout", new BigDecimal("322.99")));
        checkout.add(customer, new Product("standout", new BigDecimal("322.99")));
        checkout.add(customer, new Product("premium", new BigDecimal("394.99")));

        BigDecimal expectedTotal = new BigDecimal("1294.96");
        assertEquals(expectedTotal, checkout.total(customer));
    }

    @Test
    void testMyerCustomer() {
        Customer customer = new Customer("MYER");
        checkout.add(customer, new Product("classic", new BigDecimal("269.99")));
        checkout.add(customer, new Product("classic", new BigDecimal("269.99")));
        checkout.add(customer, new Product("classic", new BigDecimal("269.99")));
        checkout.add(customer, new Product("standout", new BigDecimal("322.99")));
        checkout.add(customer, new Product("premium", new BigDecimal("394.99")));

        System.out.println("Products before checkout:");
        customer.getProducts().forEach(p -> System.out.println(p.getName() + " - $" + p.getPrice()));

        BigDecimal expectedTotal = new BigDecimal("1218.36");
        BigDecimal actualTotal = checkout.total(customer);

        System.out.println("Products after checkout:");
        customer.getProducts().forEach(p -> System.out.println(p.getName() + " - $" + p.getPrice()));

        System.out.println("Expected: " + expectedTotal + ", Actual: " + actualTotal);
        assertEquals(expectedTotal, actualTotal);
    }

}
