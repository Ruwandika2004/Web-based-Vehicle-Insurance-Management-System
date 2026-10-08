package com.saferide.insurance.dto.request;

import com.saferide.insurance.entity.policy.PolicyType;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PolicyRequest {

    private Long customerId;

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    @NotNull(message = "Policy type is required")
    private PolicyType policyType;

    private LocalDate startDate;
    private LocalDate endDate;

    @NotNull(message = "Sum insured is required")
    private BigDecimal sumInsured;

    private BigDecimal basePremium;
    private BigDecimal totalAnnualPremium;
    private BigDecimal deductibleAmount = new BigDecimal("5000.00");

    // Comprehensive policy fields
    private Boolean includesNaturalDisaster = true;
    private Boolean includesTheft = true;
    private Boolean includesRiotStrike = true;
    private String roadsideAssistanceLevel = "PLATINUM";

    // Third-party policy fields
    private BigDecimal thirdPartyPropertyLimit = new BigDecimal("1000000.00");
    private Boolean legalLiabilityCoverage = true;

    public PolicyRequest() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public PolicyType getPolicyType() {
        return policyType;
    }

    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getSumInsured() {
        return sumInsured;
    }

    public void setSumInsured(BigDecimal sumInsured) {
        this.sumInsured = sumInsured;
    }

    public BigDecimal getBasePremium() {
        return basePremium;
    }

    public void setBasePremium(BigDecimal basePremium) {
        this.basePremium = basePremium;
    }

    public BigDecimal getTotalAnnualPremium() {
        return totalAnnualPremium;
    }

    public void setTotalAnnualPremium(BigDecimal totalAnnualPremium) {
        this.totalAnnualPremium = totalAnnualPremium;
    }

    public BigDecimal getDeductibleAmount() {
        return deductibleAmount;
    }

    public void setDeductibleAmount(BigDecimal deductibleAmount) {
        this.deductibleAmount = deductibleAmount;
    }

    public Boolean getIncludesNaturalDisaster() {
        return includesNaturalDisaster;
    }

    public void setIncludesNaturalDisaster(Boolean includesNaturalDisaster) {
        this.includesNaturalDisaster = includesNaturalDisaster;
    }

    public Boolean getIncludesTheft() {
        return includesTheft;
    }

    public void setIncludesTheft(Boolean includesTheft) {
        this.includesTheft = includesTheft;
    }

    public Boolean getIncludesRiotStrike() {
        return includesRiotStrike;
    }

    public void setIncludesRiotStrike(Boolean includesRiotStrike) {
        this.includesRiotStrike = includesRiotStrike;
    }

    public String getRoadsideAssistanceLevel() {
        return roadsideAssistanceLevel;
    }

    public void setRoadsideAssistanceLevel(String roadsideAssistanceLevel) {
        this.roadsideAssistanceLevel = roadsideAssistanceLevel;
    }

    public BigDecimal getThirdPartyPropertyLimit() {
        return thirdPartyPropertyLimit;
    }

    public void setThirdPartyPropertyLimit(BigDecimal thirdPartyPropertyLimit) {
        this.thirdPartyPropertyLimit = thirdPartyPropertyLimit;
    }

    public Boolean getLegalLiabilityCoverage() {
        return legalLiabilityCoverage;
    }

    public void setLegalLiabilityCoverage(Boolean legalLiabilityCoverage) {
        this.legalLiabilityCoverage = legalLiabilityCoverage;
    }
}
