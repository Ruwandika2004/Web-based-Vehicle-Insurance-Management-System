package com.saferide.insurance.dto.request;

import com.saferide.insurance.entity.policy.PolicyType;
import com.saferide.insurance.entity.quote.QuoteType;
import com.saferide.insurance.entity.vehicle.VehicleType;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class QuoteCalculationRequest {

    private Long customerId;
    private Long vehicleId;

    private VehicleType vehicleType = VehicleType.PRIVATE_CAR;
    private Integer vehicleAge = 2;

    @NotNull(message = "Market value is required")
    private BigDecimal marketValue;

    @NotNull(message = "Coverage type is required")
    private PolicyType coverageType = PolicyType.COMPREHENSIVE;

    private QuoteType quoteType = QuoteType.STANDARD;

    private BigDecimal ncbDiscountPercent = BigDecimal.ZERO;
    private String promoCode;
    private Integer loyaltyYears = 0;

    public QuoteCalculationRequest() {
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

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getVehicleAge() {
        return vehicleAge;
    }

    public void setVehicleAge(Integer vehicleAge) {
        this.vehicleAge = vehicleAge;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(BigDecimal marketValue) {
        this.marketValue = marketValue;
    }

    public PolicyType getCoverageType() {
        return coverageType;
    }

    public void setCoverageType(PolicyType coverageType) {
        this.coverageType = coverageType;
    }

    public QuoteType getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(QuoteType quoteType) {
        this.quoteType = quoteType;
    }

    public BigDecimal getNcbDiscountPercent() {
        return ncbDiscountPercent;
    }

    public void setNcbDiscountPercent(BigDecimal ncbDiscountPercent) {
        this.ncbDiscountPercent = ncbDiscountPercent;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public Integer getLoyaltyYears() {
        return loyaltyYears;
    }

    public void setLoyaltyYears(Integer loyaltyYears) {
        this.loyaltyYears = loyaltyYears;
    }
}
