package com.saferide.insurance.pattern.decorator;

import java.math.BigDecimal;
import java.util.List;

/**
 * DECORATOR PATTERN: Concrete Decorator 1
 * Dynamically decorates an InsurancePlan with 24/7 Roadside Assistance & Breakdown Recovery.
 */
public class RoadsideAssistanceDecorator extends PlanAddonDecorator {

    private static final BigDecimal ROADSIDE_FEE = new BigDecimal("2500.00");

    public RoadsideAssistanceDecorator(InsurancePlan decoratedPlan) {
        super(decoratedPlan);
    }

    @Override
    public String getPlanName() {
        return super.getPlanName() + " + [24/7 Roadside Assistance]";
    }

    @Override
    public BigDecimal calculatePremium() {
        return super.calculatePremium().add(ROADSIDE_FEE);
    }

    @Override
    public List<String> getCoverageFeatures() {
        List<String> features = super.getCoverageFeatures();
        features.add("24/7 Islandwide Emergency Towing & Mechanical Breakdown Assistance");
        features.add("On-site Flat Tire Replacement, Battery Jumpstart & Fuel Delivery");
        return features;
    }
}
