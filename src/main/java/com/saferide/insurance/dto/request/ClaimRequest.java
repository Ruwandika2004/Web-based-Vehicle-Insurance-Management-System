package com.saferide.insurance.dto.request;

import com.saferide.insurance.entity.claim.ClaimType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ClaimRequest {

    private Long customerId;

    @NotNull(message = "Policy ID is required")
    private Long policyId;

    @NotNull(message = "Claim type is required")
    private ClaimType claimType;

    @NotNull(message = "Incident date is required")
    private LocalDate incidentDate;

    @NotBlank(message = "Incident location is required")
    private String incidentLocation;

    @NotBlank(message = "Incident description is required")
    private String incidentDescription;

    @NotNull(message = "Estimated damage amount is required")
    private BigDecimal estimatedDamageAmount;

    // Accident claim fields
    private Boolean thirdPartyVehicleInvolved = false;
    private String policeReportNumber;
    private String driverNameAtIncident;
    private String collisionType;

    // Theft claim fields
    private String policeStationReported;
    private String crimeReferenceNumber;
    private String recoveryStatus = "NOT_RECOVERED";
    private String lastKnownLocation;

    // Windscreen claim fields
    private String glassType = "FRONT_WINDSCREEN";
    private String repairWorkshopName;
    private Boolean replacement = true;

    public ClaimRequest() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
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

    public Boolean getThirdPartyVehicleInvolved() {
        return thirdPartyVehicleInvolved;
    }

    public void setThirdPartyVehicleInvolved(Boolean thirdPartyVehicleInvolved) {
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

    public Boolean getReplacement() {
        return replacement;
    }

    public void setReplacement(Boolean replacement) {
        this.replacement = replacement;
    }
}
