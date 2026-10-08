package com.saferide.insurance.pattern.decorator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DECORATOR PATTERN: Base Abstract Decorator
 * Maintains a reference to an InsurancePlan component and forwards core method calls.
 * Subclasses extend this to attach additional coverages and premium calculations dynamically.
 */
public abstract class PlanAddonDecorator implements InsurancePlan {

    protected final InsurancePlan decoratedPlan;

    public PlanAddonDecorator(InsurancePlan decoratedPlan) {
        if (decoratedPlan == null) {
            throw new IllegalArgumentException("Decorated plan cannot be null");
        }
        this.decoratedPlan = decoratedPlan;
    }

    @Override
    public String getPlanName() {
        return decoratedPlan.getPlanName();
    }

    @Override
    public BigDecimal calculatePremium() {
        return decoratedPlan.calculatePremium();
    }

    @Override
    public List<String> getCoverageFeatures() {
        return new ArrayList<>(decoratedPlan.getCoverageFeatures());
    }

    /**
     * Getter for inspecting the inner wrapped plan.
     */
    public InsurancePlan getDecoratedPlan() {
        return decoratedPlan;
    }
}
