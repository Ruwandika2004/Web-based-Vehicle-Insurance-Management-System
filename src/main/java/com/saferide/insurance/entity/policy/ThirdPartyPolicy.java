package com.saferide.insurance.entity.policy;

import com.saferide.insurance.entity.user.Customer;
import com.saferide.insurance.entity.vehicle.Vehicle;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "third_party_policies")
@PrimaryKeyJoinColumn(name = "id")
public class ThirdPartyPolicy extends Policy {

    @Column(name = "third_party_property_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal thirdPartyPropertyLimit = new BigDecimal("1000000.00");

    @Column(name = "legal_liability_coverage")
    private boolean legalLiabilityCoverage = true;

    public ThirdPartyPolicy() {
        super();
        setPolicyType(PolicyType.THIRD_PARTY);
    }

    public ThirdPartyPolicy(String policyNumber, Customer customer, Vehicle vehicle,
                            PolicyStatus status, LocalDate startDate, LocalDate endDate,
                            BigDecimal sumInsured, BigDecimal basePremium, BigDecimal totalAnnualPremium,
                            BigDecimal deductibleAmount, BigDecimal thirdPartyPropertyLimit,
                            boolean legalLiabilityCoverage) {
        super(policyNumber, customer, vehicle, PolicyType.THIRD_PARTY, status, startDate, endDate,
                sumInsured, basePremium, totalAnnualPremium, deductibleAmount);
        this.thirdPartyPropertyLimit = thirdPartyPropertyLimit;
        this.legalLiabilityCoverage = legalLiabilityCoverage;
    }

    @Override
    public String getCoverageDetailsSummary() {
        return "Third-Party Tier: Statutory Third-Party Bodily Injury / Death & Property Damage Up to LKR " +
                thirdPartyPropertyLimit;
    }

    public BigDecimal getThirdPartyPropertyLimit() {
        return thirdPartyPropertyLimit;
    }

    public void setThirdPartyPropertyLimit(BigDecimal thirdPartyPropertyLimit) {
        this.thirdPartyPropertyLimit = thirdPartyPropertyLimit;
    }

    public boolean isLegalLiabilityCoverage() {
        return legalLiabilityCoverage;
    }

    public void setLegalLiabilityCoverage(boolean legalLiabilityCoverage) {
        this.legalLiabilityCoverage = legalLiabilityCoverage;
    }
}
