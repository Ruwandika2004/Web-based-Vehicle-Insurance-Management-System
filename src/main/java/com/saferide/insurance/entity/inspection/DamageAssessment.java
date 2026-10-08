package com.saferide.insurance.entity.inspection;

import com.saferide.insurance.entity.claim.Claim;
import com.saferide.insurance.entity.user.User;
import com.saferide.insurance.entity.vehicle.Vehicle;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "damage_assessments")
@PrimaryKeyJoinColumn(name = "id")
public class DamageAssessment extends Inspection {

    @Column(name = "structural_damage_found")
    private boolean structuralDamageFound = false;

    @Column(name = "parts_to_replace", columnDefinition = "TEXT")
    private String partsToReplace;

    @Column(name = "labor_cost_estimate", precision = 12, scale = 2)
    private BigDecimal laborCostEstimate = BigDecimal.ZERO;

    @Column(name = "recommended_action", length = 50)
    private String recommendedAction = "REPAIR_AUTHORIZED";

    public DamageAssessment() {
        super();
        setInspectionType(InspectionType.DAMAGE_ASSESSMENT);
    }

    public DamageAssessment(String inspectionReference, Vehicle vehicle, Claim claim, User inspector,
                            InspectionStatus status, LocalDate inspectionDate, String inspectionLocation,
                            String physicalConditionNotes, String damageSeverity, BigDecimal estimatedRepairCost,
                            boolean passed, boolean structuralDamageFound, String partsToReplace,
                            BigDecimal laborCostEstimate, String recommendedAction) {
        super(inspectionReference, vehicle, claim, inspector, InspectionType.DAMAGE_ASSESSMENT, status,
                inspectionDate, inspectionLocation, physicalConditionNotes, damageSeverity,
                estimatedRepairCost, passed);
        this.structuralDamageFound = structuralDamageFound;
        this.partsToReplace = partsToReplace;
        this.laborCostEstimate = laborCostEstimate != null ? laborCostEstimate : BigDecimal.ZERO;
        this.recommendedAction = recommendedAction;
    }

    @Override
    public int calculateInspectionConditionScore() {
        int score = 80;
        if (structuralDamageFound) score -= 40;
        if ("SEVERE".equalsIgnoreCase(getDamageSeverity())) score -= 30;
        else if ("MODERATE".equalsIgnoreCase(getDamageSeverity())) score -= 15;
        return Math.max(0, score);
    }

    public boolean isStructuralDamageFound() {
        return structuralDamageFound;
    }

    public void setStructuralDamageFound(boolean structuralDamageFound) {
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
