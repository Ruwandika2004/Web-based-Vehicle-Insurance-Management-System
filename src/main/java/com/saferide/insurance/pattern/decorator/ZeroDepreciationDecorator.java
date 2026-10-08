package com.saferide.insurance.pattern.decorator;

import java.math.BigDecimal;
import java.util.List;

/**
 * DECORATOR PATTERN: Concrete Decorator 4
 * Dynamically decorates an InsurancePlan with Zero Depreciation (Bumper-to-Bumper) coverage.
 */
public class ZeroDepreciationDecorator extends PlanAddonDecorator {

    private static final BigDecimal ZERO_DEP_FEE = new BigDecimal("4500.00");

    public ZeroDepreciationDecorator(InsurancePlan decoratedPlan) {
        super(decoratedPlan);
    }

    @Override
    public String getPlanName() {
        return super.getPlanName() + " + [Zero Depreciation]";
    }

    @Override
    public BigDecimal calculatePremium() {
        return super.calculatePremium().add(ZERO_DEP_FEE);
    }

    @Override
    public List<String> getCoverageFeatures() {
        List<String> features = super.getCoverageFeatures();
        features.add("100% Claim Settlement on Plastic, Rubber, Glass & Metal Body Parts Without Depreciation Deduction");
        features.add("Bumper-to-Bumper Collision Repair Cost Reimbursement");
        return features;
    }
}
