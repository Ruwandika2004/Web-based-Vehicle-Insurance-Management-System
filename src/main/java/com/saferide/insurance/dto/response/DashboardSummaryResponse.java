package com.saferide.insurance.dto.response;

import java.math.BigDecimal;

public class DashboardSummaryResponse {

    private long totalPolicies;
    private long activePolicies;
    private long totalVehicles;
    private long totalClaims;
    private long pendingClaims;
    private long approvedClaims;
    private long totalInspections;
    private long pendingInspections;
    private BigDecimal totalPremiumsCollected;
    private BigDecimal totalClaimsSettled;
    private long totalCustomers;

    public DashboardSummaryResponse() {
        this.totalPremiumsCollected = BigDecimal.ZERO;
        this.totalClaimsSettled = BigDecimal.ZERO;
    }

    public long getTotalPolicies() {
        return totalPolicies;
    }

    public void setTotalPolicies(long totalPolicies) {
        this.totalPolicies = totalPolicies;
    }

    public long getActivePolicies() {
        return activePolicies;
    }

    public void setActivePolicies(long activePolicies) {
        this.activePolicies = activePolicies;
    }

    public long getTotalVehicles() {
        return totalVehicles;
    }

    public void setTotalVehicles(long totalVehicles) {
        this.totalVehicles = totalVehicles;
    }

    public long getTotalClaims() {
        return totalClaims;
    }

    public void setTotalClaims(long totalClaims) {
        this.totalClaims = totalClaims;
    }

    public long getPendingClaims() {
        return pendingClaims;
    }

    public void setPendingClaims(long pendingClaims) {
        this.pendingClaims = pendingClaims;
    }

    public long getApprovedClaims() {
        return approvedClaims;
    }

    public void setApprovedClaims(long approvedClaims) {
        this.approvedClaims = approvedClaims;
    }

    public long getTotalInspections() {
        return totalInspections;
    }

    public void setTotalInspections(long totalInspections) {
        this.totalInspections = totalInspections;
    }

    public long getPendingInspections() {
        return pendingInspections;
    }

    public void setPendingInspections(long pendingInspections) {
        this.pendingInspections = pendingInspections;
    }

    public BigDecimal getTotalPremiumsCollected() {
        return totalPremiumsCollected;
    }

    public void setTotalPremiumsCollected(BigDecimal totalPremiumsCollected) {
        this.totalPremiumsCollected = totalPremiumsCollected;
    }

    public BigDecimal getTotalClaimsSettled() {
        return totalClaimsSettled;
    }

    public void setTotalClaimsSettled(BigDecimal totalClaimsSettled) {
        this.totalClaimsSettled = totalClaimsSettled;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }
}
