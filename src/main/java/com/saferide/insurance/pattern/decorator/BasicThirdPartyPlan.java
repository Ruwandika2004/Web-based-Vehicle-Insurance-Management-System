package com.saferide.insurance.pattern.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


public class BasicThirdPartyPlan implements InsurancePlan {

    private final BigDecimal basePremium;

    public BasicThirdPartyPlan() {
        // Standard statutory third-party rate
        this.basePremium = new BigDecimal("18500.00");
    }

    public BasicThirdPartyPlan(BigDecimal basePremium) {
        this.basePremium = basePremium != null ? basePremium : new BigDecimal("18500.00");
    }

    @Override
    public String getPlanName() {
        return "SafeRide Basic Third-Party Liability Plan";
    }

    @Override
    public BigDecimal calculatePremium() {
        return basePremium.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public List<String> getCoverageFeatures() {
        List<String> features = new ArrayList<>();
        features.add("Third-Party Bodily Injury & Death Liability");
        features.add("Third-Party Property Damage Coverage (up to LKR 1,000,000)");
        features.add("Legal Defense & Statutory Compliance Coverage");
        return features;
    }
}
