package com.seek.checkout.loader;

import com.seek.checkout.rules.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class RuleLoader {

    // For loading the pricing rules from a JSON file
    public static void loadRules(String filePath) {
        try {
            System.out.println(" Loading pricing rules from: " + filePath);

            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println(" File not found: " + filePath);
                return;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rules = objectMapper.readTree(file).get("rules");

            if (rules == null) {
                System.out.println(" No rules found in file.");
                return;
            }

            for (JsonNode rule : rules) {
                String customer = rule.get("customer").asText();
                String type = rule.get("type").asText();

                System.out.println(" Loaded Rule for: " + customer + " (" + type + ")");

                switch (type) {
                    case "ThreeForTwo" ->
                            PricingRuleFactory.registerRule(customer, new SecondBitePricing());
                    case "Discount" -> {
                        String product = rule.get("product").asText();
                        BigDecimal price = BigDecimal.valueOf(rule.get("price").asDouble());
                        PricingRuleFactory.registerRule(customer, new DiscountPricing(customer, product, price));
                    }
                    case "Bundle" -> {
                        String product = rule.get("product").asText();
                        int bundleSize = rule.get("bundleSize").asInt();
                        PricingRuleFactory.registerRule(customer, new BundlePricing(customer, product, bundleSize));
                    }
                    case "PercentageOff" -> {
                        BigDecimal threshold = BigDecimal.valueOf(rule.get("threshold").asDouble());
                        BigDecimal discount = BigDecimal.valueOf(rule.get("discount").asDouble());
                        PricingRuleFactory.registerRule(customer, new PercentageOffPricing(customer, threshold, discount));
                    }
                    case "FreeAd" -> {
                        String triggerProduct = rule.get("triggerProduct").asText();
                        int minQuantity = rule.get("minQuantity").asInt();
                        String bonusProduct = rule.get("bonusProduct").asText();
                        PricingRuleFactory.registerRule(customer, new FreeAdPricing(customer, triggerProduct, minQuantity, bonusProduct));
                    }
                    default ->
                            throw new IllegalArgumentException("Unknown rule type: " + type);
                }
            }
        } catch (IOException e) {
            System.out.println(" Failed to load rules: " + e.getMessage());
            e.printStackTrace(); //TODO: Can be replaced with proper logging framework
        }
    }
}
