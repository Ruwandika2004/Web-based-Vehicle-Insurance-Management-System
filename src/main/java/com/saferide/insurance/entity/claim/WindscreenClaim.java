package com.saferide.insurance.entity.claim;

import com.saferide.insurance.entity.policy.Policy;
import com.saferide.insurance.entity.user.Customer;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "windscreen_claims")
@PrimaryKeyJoinColumn(name = "id")
public class WindscreenClaim extends Claim {

    @Column(name = "glass_type", nullable = false, length = 30)
    private String glassType = "FRONT_WINDSCREEN";

    @Column(name = "repair_workshop_name", nullable = false, length = 100)
    private String repairWorkshopName;

    @Column(name = "is_replacement")
    private boolean replacement = true;

    public WindscreenClaim() {
        super();
        setClaimType(ClaimType.WINDSCREEN);
    }

    public WindscreenClaim(String claimReference, Policy policy, Customer customer,
                           ClaimStatus status, LocalDate incidentDate, String incidentLocation,
                           String incidentDescription, BigDecimal estimatedDamageAmount,
                           String glassType, String repairWorkshopName, boolean replacement) {
        super(claimReference, policy, customer, ClaimType.WINDSCREEN, status, incidentDate,
                incidentLocation, incidentDescription, estimatedDamageAmount);
        this.glassType = glassType != null ? glassType : "FRONT_WINDSCREEN";
        this.repairWorkshopName = repairWorkshopName;
        this.replacement = replacement;
    }

    @Override
    public BigDecimal determineMaxPayoutLimit() {
        // Windscreen claims generally have standard sub-limits without impacting NCB
        return new BigDecimal("100000.00");
    }

    @Override
    public boolean validateClaimEligibility() {
        return repairWorkshopName != null && !repairWorkshopName.trim().isEmpty();
    }

    public String getGlassType() {
        return glassType;
    }

    public void setGlassType(String glassType) {
        this.glassType = glassType;
    }

    public String getRepairWorkshopName() {
        return repairWorkshopName;
    }

    public void setRepairWorkshopName(String repairWorkshopName) {
        this.repairWorkshopName = repairWorkshopName;
    }

    public boolean isReplacement() {
        return replacement;
    }

    public void setReplacement(boolean replacement) {
        this.replacement = replacement;
    }
}
