package com.saferide.insurance.pattern.decorator;

import java.math.BigDecimal;
import java.util.List;

/**
 * DECORATOR PATTERN: Concrete Decorator 3
 * Dynamically decorates an InsurancePlan with Passenger Personal Accident & Medical Expense Protection.
 */
public class PassengerCoverDecorator extends PlanAddonDecorator {

    private static final BigDecimal PASSENGER_COVER_FEE = new BigDecimal("3500.00");

    public PassengerCoverDecorator(InsurancePlan decoratedPlan) {
        super(decoratedPlan);
    }

    @Override
    public String getPlanName() {
        return super.getPlanName() + " + [Passenger Personal Accident]";
    }

    @Override
    public BigDecimal calculatePremium() {
        return super.calculatePremium().add(PASSENGER_COVER_FEE);
    }

    @Override
    public List<String> getCoverageFeatures() {
        List<String> features = super.getCoverageFeatures();
        features.add("Accidental Death & Permanent Disablement Cover for Driver and Passengers");
        features.add("Hospitalization & Emergency Medical Expense Reimbursement (up to LKR 500,000)");
        return features;
    }
}
