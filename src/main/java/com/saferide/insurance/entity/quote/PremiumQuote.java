package com.saferide.insurance.entity.quote;

import com.saferide.insurance.entity.BaseEntity;
import com.saferide.insurance.entity.policy.PolicyType;
import com.saferide.insurance.entity.user.Customer;
import com.saferide.insurance.entity.vehicle.Vehicle;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Abstract Base Class: PremiumQuote (Subasinghe S.A.K.G. - IT25102301)
 * Demonstrates OOP Polymorphism for actuarial vehicle insurance quotation calculation.
 */
@Entity
@Table(name = "premium_quotes")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PremiumQuote extends BaseEntity {

    @Column(name = "quote_reference", nullable = false, unique = true, length = 50)
    private String quoteReference;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Enumerated(EnumType.STRING)
    @Column(name = "quote_type", nullable = false, length = 30)
    private QuoteType quoteType;

    @Enumerated(EnumType.STRING)
    @Column(name = "coverage_type", nullable = false, length = 30)
    private PolicyType coverageType;

    @Column(name = "calculated_sum_insured", nullable = false, precision = 12, scale = 2)
    private BigDecimal calculatedSumInsured;

    @Column(name = "base_rate", nullable = false, precision = 6, scale = 4)
    private BigDecimal baseRate;

    @Column(name = "risk_loading_percent", nullable = false, precision = 5, scale = 2)
    private BigDecimal riskLoadingPercent = BigDecimal.ZERO;

    @Column(name = "ncb_discount_percent", nullable = false, precision = 5, scale = 2)
    private BigDecimal ncbDiscountPercent = BigDecimal.ZERO;

    @Column(name = "final_premium", nullable = false, precision = 12, scale = 2)
    private BigDecimal finalPremium;

    @Column(name = "is_applied_to_policy", nullable = false)
    private boolean appliedToPolicy = false;

    @Column(name = "valid_until", nullable = false)
    private LocalDate validUntil;

    public PremiumQuote() {
    }

    public PremiumQuote(String quoteReference, Customer customer, Vehicle vehicle, QuoteType quoteType,
                        PolicyType coverageType, BigDecimal calculatedSumInsured, BigDecimal baseRate,
                        BigDecimal riskLoadingPercent, BigDecimal ncbDiscountPercent, LocalDate validUntil) {
        this.quoteReference = quoteReference;
        this.customer = customer;
        this.vehicle = vehicle;
        this.quoteType = quoteType;
        this.coverageType = coverageType;
        this.calculatedSumInsured = calculatedSumInsured;
        this.baseRate = baseRate;
        this.riskLoadingPercent = riskLoadingPercent != null ? riskLoadingPercent : BigDecimal.ZERO;
        this.ncbDiscountPercent = ncbDiscountPercent != null ? ncbDiscountPercent : BigDecimal.ZERO;
        this.validUntil = validUntil != null ? validUntil : LocalDate.now().plusDays(30);
        this.appliedToPolicy = false;
        this.finalPremium = calculateFinalPremium();
    }

    // Polymorphic actuarial calculation method
    public abstract BigDecimal calculateFinalPremium();

    protected BigDecimal computeRawPremium() {
        BigDecimal base = calculatedSumInsured.multiply(baseRate);
        BigDecimal loading = base.multiply(riskLoadingPercent.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
        BigDecimal subTotal = base.add(loading);
        BigDecimal discount = subTotal.multiply(ncbDiscountPercent.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
        return subTotal.subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isValid() {
        return !appliedToPolicy && (validUntil == null || !LocalDate.now().isAfter(validUntil));
    }

    // Getters and Setters
    public String getQuoteReference() {
        return quoteReference;
    }

    public void setQuoteReference(String quoteReference) {
        this.quoteReference = quoteReference;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public QuoteType getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(QuoteType quoteType) {
        this.quoteType = quoteType;
    }

    public PolicyType getCoverageType() {
        return coverageType;
    }

    public void setCoverageType(PolicyType coverageType) {
        this.coverageType = coverageType;
    }

    public BigDecimal getCalculatedSumInsured() {
        return calculatedSumInsured;
    }

    public void setCalculatedSumInsured(BigDecimal calculatedSumInsured) {
        this.calculatedSumInsured = calculatedSumInsured;
    }

    public BigDecimal getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(BigDecimal baseRate) {
        this.baseRate = baseRate;
    }

    public BigDecimal getRiskLoadingPercent() {
        return riskLoadingPercent;
    }

    public void setRiskLoadingPercent(BigDecimal riskLoadingPercent) {
        this.riskLoadingPercent = riskLoadingPercent;
    }

    public BigDecimal getNcbDiscountPercent() {
        return ncbDiscountPercent;
    }

    public void setNcbDiscountPercent(BigDecimal ncbDiscountPercent) {
        this.ncbDiscountPercent = ncbDiscountPercent;
    }

    public BigDecimal getFinalPremium() {
        return finalPremium;
    }

    public void setFinalPremium(BigDecimal finalPremium) {
        this.finalPremium = finalPremium;
    }

    public boolean isAppliedToPolicy() {
        return appliedToPolicy;
    }

    public void setAppliedToPolicy(boolean appliedToPolicy) {
        this.appliedToPolicy = appliedToPolicy;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }
}
