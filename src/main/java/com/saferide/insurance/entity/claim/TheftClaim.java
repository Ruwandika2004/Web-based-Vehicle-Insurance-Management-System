package com.saferide.insurance.entity.claim;

import com.saferide.insurance.entity.policy.Policy;
import com.saferide.insurance.entity.user.Customer;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "theft_claims")
@PrimaryKeyJoinColumn(name = "id")
public class TheftClaim extends Claim {

    @Column(name = "police_station_reported", nullable = false, length = 100)
    private String policeStationReported;

    @Column(name = "crime_reference_number", nullable = false, length = 50)
    private String crimeReferenceNumber;

    @Column(name = "recovery_status", length = 30)
    private String recoveryStatus = "NOT_RECOVERED";

    @Column(name = "last_known_location", nullable = false, length = 150)
    private String lastKnownLocation;

    public TheftClaim() {
        super();
        setClaimType(ClaimType.THEFT);
    }

    public TheftClaim(String claimReference, Policy policy, Customer customer,
                      ClaimStatus status, LocalDate incidentDate, String incidentLocation,
                      String incidentDescription, BigDecimal estimatedDamageAmount,
                      String policeStationReported, String crimeReferenceNumber,
                      String recoveryStatus, String lastKnownLocation) {
        super(claimReference, policy, customer, ClaimType.THEFT, status, incidentDate,
                incidentLocation, incidentDescription, estimatedDamageAmount);
        this.policeStationReported = policeStationReported;
        this.crimeReferenceNumber = crimeReferenceNumber;
        this.recoveryStatus = recoveryStatus != null ? recoveryStatus : "NOT_RECOVERED";
        this.lastKnownLocation = lastKnownLocation;
    }

    @Override
    public BigDecimal determineMaxPayoutLimit() {
        // Full market value / sum insured if total vehicle loss
        if (getPolicy() != null && getPolicy().getSumInsured() != null) {
            return getPolicy().getSumInsured();
        }
        return BigDecimal.ZERO;
    }

    @Override
    public boolean validateClaimEligibility() {
        // Crime reference number and police station are strictly required for theft
        return crimeReferenceNumber != null && !crimeReferenceNumber.trim().isEmpty() &&
                policeStationReported != null && !policeStationReported.trim().isEmpty();
    }

    public String getPoliceStationReported() {
        return policeStationReported;
    }

    public void setPoliceStationReported(String policeStationReported) {
        this.policeStationReported = policeStationReported;
    }

    public String getCrimeReferenceNumber() {
        return crimeReferenceNumber;
    }

    public void setCrimeReferenceNumber(String crimeReferenceNumber) {
        this.crimeReferenceNumber = crimeReferenceNumber;
    }

    public String getRecoveryStatus() {
        return recoveryStatus;
    }

    public void setRecoveryStatus(String recoveryStatus) {
        this.recoveryStatus = recoveryStatus;
    }

    public String getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(String lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }
}
