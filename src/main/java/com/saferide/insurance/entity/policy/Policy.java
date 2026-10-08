package com.saferide.insurance.entity.policy;

import com.saferide.insurance.entity.BaseEntity;
import com.saferide.insurance.entity.user.Customer;
import com.saferide.insurance.entity.vehicle.Vehicle;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Abstract Base Class: Policy (Gamage S.M. - IT25102293)
 * Demonstrates OOP Inheritance and Polymorphism in Policy Underwriting.
 */
@Entity
@Table(name = "policies")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Policy extends BaseEntity {

    @Column(name = "policy_number", nullable = false, unique = true, length = 50)
    private String policyNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Enumerated(EnumType.STRING)
    @Column(name = "policy_type", nullable = false, length = 30)
    private PolicyType policyType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PolicyStatus status = PolicyStatus.ACTIVE;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "sum_insured", nullable = false, precision = 12, scale = 2)
    private BigDecimal sumInsured;

    @Column(name = "base_premium", nullable = false, precision = 12, scale = 2)
    private BigDecimal basePremium;

    @Column(name = "total_annual_premium", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAnnualPremium;

    @Column(name = "deductible_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal deductibleAmount = new BigDecimal("5000.00");

    @Column(name = "terms_agreed", nullable = false)
    private boolean termsAgreed = true;

    public Policy() {
    }

    public Policy(String policyNumber, Customer customer, Vehicle vehicle, PolicyType policyType,
                  PolicyStatus status, LocalDate startDate, LocalDate endDate, BigDecimal sumInsured,
                  BigDecimal basePremium, BigDecimal totalAnnualPremium, BigDecimal deductibleAmount) {
        this.policyNumber = policyNumber;
        this.customer = customer;
        this.vehicle = vehicle;
        this.policyType = policyType;
        this.status = status != null ? status : PolicyStatus.ACTIVE;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sumInsured = sumInsured;
        this.basePremium = basePremium;
        this.totalAnnualPremium = totalAnnualPremium;
        this.deductibleAmount = deductibleAmount;
        this.termsAgreed = true;
    }

    // Polymorphic method to get policy coverage summary
    public abstract String getCoverageDetailsSummary();

    public boolean isCurrentlyActive() {
        LocalDate today = LocalDate.now();
        return status == PolicyStatus.ACTIVE && !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    public long getRemainingDays() {
        if (endDate == null) return 0;
        long days = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        return Math.max(0, days);
    }

    // Getters and Setters
    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
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

    public PolicyType getPolicyType() {
        return policyType;
    }

    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public void setStatus(PolicyStatus status) {
        this.status = status;
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

    public boolean isTermsAgreed() {
        return termsAgreed;
    }

    public void setTermsAgreed(boolean termsAgreed) {
        this.termsAgreed = termsAgreed;
    }
}
