package com.saferide.insurance.dto.request;

import com.saferide.insurance.entity.inspection.InspectionStatus;
import com.saferide.insurance.entity.inspection.InspectionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class InspectionRequest {

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    private Long claimId;
    private Long inspectorId;

    @NotNull(message = "Inspection type is required")
    private InspectionType inspectionType;

    private InspectionStatus status = InspectionStatus.PENDING;

    @NotNull(message = "Inspection date is required")
    private LocalDate inspectionDate;

    @NotBlank(message = "Inspection location is required")
    private String inspectionLocation;

    private String physicalConditionNotes;
    private String damageSeverity = "MODERATE";
    private BigDecimal estimatedRepairCost = BigDecimal.ZERO;
    private Boolean passed = true;

    // Pre-policy inspection fields
    private Boolean odometerVerified = true;
    private Integer bodyConditionRating = 8;
    private Integer mechanicalScore = 8;

    // Damage assessment fields
    private Boolean structuralDamageFound = false;
    private String partsToReplace;
    private BigDecimal laborCostEstimate = BigDecimal.ZERO;
    private String recommendedAction = "REPAIR_AUTHORIZED";

    public InspectionRequest() {
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getClaimId() {
        return claimId;
    }

    public void setClaimId(Long claimId) {
        this.claimId = claimId;
    }

    public Long getInspectorId() {
        return inspectorId;
    }

    public void setInspectorId(Long inspectorId) {
        this.inspectorId = inspectorId;
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

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public Boolean getOdometerVerified() {
        return odometerVerified;
    }

    public void setOdometerVerified(Boolean odometerVerified) {
        this.odometerVerified = odometerVerified;
    }

    public Integer getBodyConditionRating() {
        return bodyConditionRating;
    }

    public void setBodyConditionRating(Integer bodyConditionRating) {
        this.bodyConditionRating = bodyConditionRating;
    }

    public Integer getMechanicalScore() {
        return mechanicalScore;
    }

    public void setMechanicalScore(Integer mechanicalScore) {
        this.mechanicalScore = mechanicalScore;
    }

    public Boolean getStructuralDamageFound() {
        return structuralDamageFound;
    }

    public void setStructuralDamageFound(Boolean structuralDamageFound) {
        this.structuralDamageFound = structuralDamageFound;
    }

    public String getPartsToReplace() {
        return partsToReplace;
    }

    public void setPartsToReplace(String partsToReplace) {
        this.partsToReplace = partsToReplace;
    }

    public BigDecimal getLaborCostEstimate() {
        return laborCostEstimate;
    }

    public void setLaborCostEstimate(BigDecimal laborCostEstimate) {
        this.laborCostEstimate = laborCostEstimate;
    }

    public String getRecommendedAction() {
        return recommendedAction;
    }

    public void setRecommendedAction(String recommendedAction) {
        this.recommendedAction = recommendedAction;
    }
}
