package com.saferide.insurance.entity.claim;

import com.saferide.insurance.entity.BaseEntity;
import com.saferide.insurance.entity.policy.Policy;
import com.saferide.insurance.entity.user.Customer;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "claims")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Claim extends BaseEntity {

    @Column(name = "claim_reference", nullable = false, unique = true, length = 50)
    private String claimReference;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_type", nullable = false, length = 30)
    private ClaimType claimType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ClaimStatus status = ClaimStatus.SUBMITTED;

    @Column(name = "incident_date", nullable = false)
    private LocalDate incidentDate;

    @Column(name = "incident_location", nullable = false, length = 150)
    private String incidentLocation;

    @Column(name = "incident_description", nullable = false, columnDefinition = "TEXT")
    private String incidentDescription;

    @Column(name = "estimated_damage_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal estimatedDamageAmount;

    @Column(name = "approved_payout_amount", precision = 12, scale = 2)
    private BigDecimal approvedPayoutAmount = BigDecimal.ZERO;

    @Column(name = "assessor_notes", columnDefinition = "TEXT")
    private String assessorNotes;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "settlement_date")
    private LocalDate settlementDate;

    public Claim() {
    }

    public Claim(String claimReference, Policy policy, Customer customer, ClaimType claimType,
                 ClaimStatus status, LocalDate incidentDate, String incidentLocation,
                 String incidentDescription, BigDecimal estimatedDamageAmount) {
        this.claimReference = claimReference;
        this.policy = policy;
        this.customer = customer;
        this.claimType = claimType;
        this.status = status != null ? status : ClaimStatus.SUBMITTED;
        this.incidentDate = incidentDate;
        this.incidentLocation = incidentLocation;
        this.incidentDescription = incidentDescription;
        this.estimatedDamageAmount = estimatedDamageAmount;
        this.approvedPayoutAmount = BigDecimal.ZERO;
    }

    // Polymorphic method to determine maximum eligible payout based on claim type
    public abstract BigDecimal determineMaxPayoutLimit();

    // Polymorphic method to validate specific eligibility rules
    public abstract boolean validateClaimEligibility();

    // Invariant: Customer can withdraw claim only if pending / not settled
    public boolean canBeWithdrawn() {
        return this.status == ClaimStatus.SUBMITTED || this.status == ClaimStatus.UNDER_REVIEW;
    }

    // Getters and Setters
    public String getClaimReference() {
        return claimReference;
    }

    public void setClaimReference(String claimReference) {
        this.claimReference = claimReference;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public LocalDate getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(LocalDate incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getIncidentLocation() {
        return incidentLocation;
    }

    public void setIncidentLocation(String incidentLocation) {
        this.incidentLocation = incidentLocation;
    }

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }

    public BigDecimal getEstimatedDamageAmount() {
        return estimatedDamageAmount;
    }

    public void setEstimatedDamageAmount(BigDecimal estimatedDamageAmount) {
        this.estimatedDamageAmount = estimatedDamageAmount;
    }

    public BigDecimal getApprovedPayoutAmount() {
        return approvedPayoutAmount;
    }

    public void setApprovedPayoutAmount(BigDecimal approvedPayoutAmount) {
        this.approvedPayoutAmount = approvedPayoutAmount;
    }

    public String getAssessorNotes() {
        return assessorNotes;
    }

    public void setAssessorNotes(String assessorNotes) {
        this.assessorNotes = assessorNotes;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }
}
