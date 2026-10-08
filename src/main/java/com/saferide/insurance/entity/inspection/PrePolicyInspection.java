package com.saferide.insurance.entity.inspection;

import com.saferide.insurance.entity.user.User;
import com.saferide.insurance.entity.vehicle.Vehicle;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pre_policy_inspections")
@PrimaryKeyJoinColumn(name = "id")
public class PrePolicyInspection extends Inspection {

    @Column(name = "odometer_verified")
    private boolean odometerVerified = true;

    @Column(name = "body_condition_rating")
    private int bodyConditionRating = 8; // 1 to 10 scale

    @Column(name = "mechanical_score")
    private int mechanicalScore = 8; // 1 to 10 scale

    public PrePolicyInspection() {
        super();
        setInspectionType(InspectionType.PRE_POLICY);
    }

    public PrePolicyInspection(String inspectionReference, Vehicle vehicle, User inspector,
                               InspectionStatus status, LocalDate inspectionDate, String inspectionLocation,
                               String physicalConditionNotes, boolean passed, boolean odometerVerified,
                               int bodyConditionRating, int mechanicalScore) {
        super(inspectionReference, vehicle, null, inspector, InspectionType.PRE_POLICY, status,
                inspectionDate, inspectionLocation, physicalConditionNotes, "NONE", BigDecimal.ZERO, passed);
        this.odometerVerified = odometerVerified;
        this.bodyConditionRating = bodyConditionRating;
        this.mechanicalScore = mechanicalScore;
    }

    @Override
    public int calculateInspectionConditionScore() {
        return (bodyConditionRating * 5) + (mechanicalScore * 5); // 0-100 total score
    }

    public boolean isOdometerVerified() {
        return odometerVerified;
    }

    public void setOdometerVerified(boolean odometerVerified) {
        this.odometerVerified = odometerVerified;
    }

    public int getBodyConditionRating() {
        return bodyConditionRating;
    }

    public void setBodyConditionRating(int bodyConditionRating) {
        this.bodyConditionRating = bodyConditionRating;
    }

    public int getMechanicalScore() {
        return mechanicalScore;
    }

    public void setMechanicalScore(int mechanicalScore) {
        this.mechanicalScore = mechanicalScore;
    }
}
