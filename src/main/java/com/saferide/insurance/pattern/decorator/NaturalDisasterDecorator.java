package com.saferide.insurance.pattern.decorator;

import java.math.BigDecimal;
import java.util.List;

/**
 * DECORATOR PATTERN: Concrete Decorator 2
 * Dynamically decorates an InsurancePlan with Natural Disaster (Flood, Storm, Landslide) Coverage.
 */
public class NaturalDisasterDecorator extends PlanAddonDecorator {

    private static final BigDecimal DISASTER_COVER_FEE = new BigDecimal("5000.00");

    public NaturalDisasterDecorator(InsurancePlan decoratedPlan) {
        super(decoratedPlan);
    }

    @Override
    public String getPlanName() {
        return super.getPlanName() + " + [Flood & Natural Perils Rider]";
    }

    @Override
    public BigDecimal calculatePremium() {
        return super.calculatePremium().add(DISASTER_COVER_FEE);
    }

    @Override
    public List<String> getCoverageFeatures() {
        List<String> features = super.getCoverageFeatures();
        features.add("Protection Against Floods, Inundation, Flash Storms & Cyclones");
        features.add("Earthslip, Rockfall & Natural Calamity Hull Protection");
        return features;
    }
}
