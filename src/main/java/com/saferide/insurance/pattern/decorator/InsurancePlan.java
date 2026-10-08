package com.saferide.insurance.pattern.decorator;

import java.math.BigDecimal;
import java.util.List;

/**
 * DECORATOR PATTERN: Component Interface
 * Defines the core contract for all insurance plans (base plans and decorated add-on plans).
 */
public interface InsurancePlan {
    
    /**
     * Returns the name/description of the insurance plan and any attached riders.
     */
    String getPlanName();

    /**
     * Calculates the total annual premium including base coverage and any attached add-ons.
     */
    BigDecimal calculatePremium();

    /**
     * Returns the list of all active coverages/features included in this plan.
     */
    List<String> getCoverageFeatures();
}
