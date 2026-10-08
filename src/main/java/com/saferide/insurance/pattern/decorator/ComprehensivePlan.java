package com.saferide.insurance.pattern.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * DECORATOR PATTERN: Concrete Component 2
 * Represents a Comprehensive Own-Damage vehicle insurance policy.
 */
public class ComprehensivePlan implements InsurancePlan {

    private final BigDecimal sumInsured;
    private final BigDecimal baseRate;

    public ComprehensivePlan(BigDecimal sumInsured) {
        this(sumInsured, new BigDecimal("0.0125")); // Default 1.25%
    }

    public ComprehensivePlan(BigDecimal sumInsured, BigDecimal baseRate) {
        this.sumInsured = sumInsured != null ? sumInsured : new BigDecimal("4500000.00");
        this.baseRate = baseRate != null ? baseRate : new BigDecimal("0.0125");
    }

    @Override
    public String getPlanName() {
        return "SafeRide Comprehensive Vehicle Insurance Plan";
    }

    @Override
    public BigDecimal calculatePremium() {
        return sumInsured.multiply(baseRate).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public List<String> getCoverageFeatures() {
        List<String> features = new ArrayList<>();
        features.add("Own Damage from Accidental External Means");
        features.add("Fire, Self-Ignition & Lightning Damage");
        features.add("Theft & Malicious Damage Cover");
        features.add("Full Third-Party Bodily Injury & Property Liability");
        return features;
    }

    public BigDecimal getSumInsured() {
        return sumInsured;
    }
}
