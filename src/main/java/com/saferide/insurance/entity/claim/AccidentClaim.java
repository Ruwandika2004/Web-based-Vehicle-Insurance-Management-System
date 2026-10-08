package com.saferide.insurance.entity.claim;

import com.saferide.insurance.entity.policy.Policy;
import com.saferide.insurance.entity.user.Customer;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "accident_claims")
@PrimaryKeyJoinColumn(name = "id")
public class AccidentClaim extends Claim {

    @Column(name = "third_party_vehicle_involved")
    private boolean thirdPartyVehicleInvolved = false;

    @Column(name = "police_report_number", length = 50)
    private String policeReportNumber;

    @Column(name = "driver_name_at_incident", nullable = false, length = 100)
    private String driverNameAtIncident;

    @Column(name = "collision_type", nullable = false, length = 50)
    private String collisionType;

    public AccidentClaim() {
        super();
        setClaimType(ClaimType.ACCIDENT);
    }

    public AccidentClaim(String claimReference, Policy policy, Customer customer,
                         ClaimStatus status, LocalDate incidentDate, String incidentLocation,
                         String incidentDescription, BigDecimal estimatedDamageAmount,
                         boolean thirdPartyVehicleInvolved, String policeReportNumber,
                         String driverNameAtIncident, String collisionType) {
        super(claimReference, policy, customer, ClaimType.ACCIDENT, status, incidentDate,
                incidentLocation, incidentDescription, estimatedDamageAmount);
        this.thirdPartyVehicleInvolved = thirdPartyVehicleInvolved;
        this.policeReportNumber = policeReportNumber;
        this.driverNameAtIncident = driverNameAtIncident;
        this.collisionType = collisionType;
    }

    @Override
    public BigDecimal determineMaxPayoutLimit() {
        // Max payout bounded by policy sum insured minus deductible
        if (getPolicy() != null && getPolicy().getSumInsured() != null) {
            BigDecimal max = getPolicy().getSumInsured().subtract(getPolicy().getDeductibleAmount());
            return max.max(BigDecimal.ZERO);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public boolean validateClaimEligibility() {
        // Driver name and valid policy are mandatory
        return driverNameAtIncident != null && !driverNameAtIncident.trim().isEmpty() &&
                getPolicy() != null && getPolicy().isCurrentlyActive();
    }

    public boolean isThirdPartyVehicleInvolved() {
        return thirdPartyVehicleInvolved;
    }

    public void setThirdPartyVehicleInvolved(boolean thirdPartyVehicleInvolved) {
        this.thirdPartyVehicleInvolved = thirdPartyVehicleInvolved;
    }

    public String getPoliceReportNumber() {
        return policeReportNumber;
    }

    public void setPoliceReportNumber(String policeReportNumber) {
        this.policeReportNumber = policeReportNumber;
    }

    public String getDriverNameAtIncident() {
        return driverNameAtIncident;
    }

    public void setDriverNameAtIncident(String driverNameAtIncident) {
        this.driverNameAtIncident = driverNameAtIncident;
    }

    public String getCollisionType() {
        return collisionType;
    }

    public void setCollisionType(String collisionType) {
        this.collisionType = collisionType;
    }
}
