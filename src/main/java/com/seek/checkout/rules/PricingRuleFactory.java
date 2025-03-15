package com.seek.checkout.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PricingRuleFactory {

    private static final Map<String, PricingRule> rules = new ConcurrentHashMap<>();

    public static void registerRule(String customerName, PricingRule rule) {
        System.out.println("Registering Rule for: " + customerName);
        rules.put(customerName, rule);
    }

    public static List<PricingRule> getAllRules() {
        System.out.println("Fetching all rules from factory. Total rules: " + rules.size());
        return new ArrayList<>(rules.values());
    }

}
