package com.saferide.insurance.entity.inspection;

import com.saferide.insurance.entity.BaseEntity;
import com.saferide.insurance.entity.claim.Claim;
import com.saferide.insurance.entity.user.User;
import com.saferide.insurance.entity.vehicle.Vehicle;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Abstract Base Class: Inspection (Senavirathna M.D.T.D. - IT25102348)
 * Demonstrates OOP Inheritance and Polymorphism in Vehicle Technical Assessment.
 */
@Entity
@Table(name = "inspections")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Inspection extends BaseEntity {

    @Column(name = "inspection_reference", nullable = false, unique = true, length = 50)
    private String inspectionReference;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "claim_id")
    private Claim claim;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inspector_id")
    private User inspector;

    @Enumerated(EnumType.STRING)
    @Column(name = "inspection_type", nullable = false, length = 30)
    private InspectionType inspectionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InspectionStatus status = InspectionStatus.PENDING;

    @Column(name = "inspection_date", nullable = false)
    private LocalDate inspectionDate;

    @Column(name = "inspection_location", nullable = false, length = 150)
    private String inspectionLocation;

    @Column(name = "physical_condition_notes", columnDefinition = "TEXT")
    private String physicalConditionNotes;

    @Column(name = "damage_severity", length = 30)
    private String damageSeverity = "MODERATE";

    @Column(name = "estimated_repair_cost", precision = 12, scale = 2)
    private BigDecimal estimatedRepairCost = BigDecimal.ZERO;

    @Column(name = "is_passed", nullable = false)
    private boolean passed = true;

    public Inspection() {
    }

    public Inspection(String inspectionReference, Vehicle vehicle, Claim claim, User inspector,
                      InspectionType inspectionType, InspectionStatus status, LocalDate inspectionDate,
                      String inspectionLocation, String physicalConditionNotes, String damageSeverity,
                      BigDecimal estimatedRepairCost, boolean passed) {
        this.inspectionReference = inspectionReference;
        this.vehicle = vehicle;
        this.claim = claim;
        this.inspector = inspector;
        this.inspectionType = inspectionType;
        this.status = status != null ? status : InspectionStatus.PENDING;
        this.inspectionDate = inspectionDate;
        this.inspectionLocation = inspectionLocation;
        this.physicalConditionNotes = physicalConditionNotes;
        this.damageSeverity = damageSeverity;
        this.estimatedRepairCost = estimatedRepairCost != null ? estimatedRepairCost : BigDecimal.ZERO;
        this.passed = passed;
    }

    // Polymorphic method to generate an overall condition score
    public abstract int calculateInspectionConditionScore();

    // Invariant check
    public boolean isModifiable() {
        return status != InspectionStatus.CANCELLED;
    }

    // Getters and Setters
    public String getInspectionReference() {
        return inspectionReference;
    }

    public void setInspectionReference(String inspectionReference) {
        this.inspectionReference = inspectionReference;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public User getInspector() {
        return inspector;
    }

    public void setInspector(User inspector) {
        this.inspector = inspector;
    }

    public InspectionType getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(InspectionType inspectionType) {
        this.inspectionType = inspectionType;
    }

    public InspectionStatus getStatus() {
        return status;
    }

    public void setStatus(InspectionStatus status) {
        this.status = status;
    }

    public LocalDate getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(LocalDate inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getInspectionLocation() {
        return inspectionLocation;
    }

    public void setInspectionLocation(String inspectionLocation) {
        this.inspectionLocation = inspectionLocation;
    }

    public String getPhysicalConditionNotes() {
        return physicalConditionNotes;
    }

    public void setPhysicalConditionNotes(String physicalConditionNotes) {
        this.physicalConditionNotes = physicalConditionNotes;
    }

    public String getDamageSeverity() {
        return damageSeverity;
    }

    public void setDamageSeverity(String damageSeverity) {
        this.damageSeverity = damageSeverity;
    }

    public BigDecimal getEstimatedRepairCost() {
        return estimatedRepairCost;
    }

    public void setEstimatedRepairCost(BigDecimal estimatedRepairCost) {
        this.estimatedRepairCost = estimatedRepairCost;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
