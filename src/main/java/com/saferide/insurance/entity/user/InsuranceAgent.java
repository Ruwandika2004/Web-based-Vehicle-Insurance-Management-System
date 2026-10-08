package com.saferide.insurance.entity.user;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "insurance_agents")
@PrimaryKeyJoinColumn(name = "id")
public class InsuranceAgent extends User {

    @Column(name = "agent_license_number", nullable = false, unique = true, length = 50)
    private String agentLicenseNumber;

    @Column(name = "agency_branch", nullable = false, length = 50)
    private String agencyBranch;

    @Column(name = "commission_rate", precision = 5, scale = 2)
    private BigDecimal commissionRate = new BigDecimal("5.00");

    public InsuranceAgent() {
        super();
        setRole(UserRole.ROLE_INSURANCE_AGENT);
    }

    public InsuranceAgent(String username, String password, String email, String fullName, String phone, String agentLicenseNumber, String agencyBranch, BigDecimal commissionRate) {
        super(username, password, email, fullName, phone, UserRole.ROLE_INSURANCE_AGENT);
        this.agentLicenseNumber = agentLicenseNumber;
        this.agencyBranch = agencyBranch;
        this.commissionRate = commissionRate;
    }

    @Override
    public String getRoleDescription() {
        return "Certified SafeRide Insurance Field Agent";
    }

    public String getAgentLicenseNumber() {
        return agentLicenseNumber;
    }

    public void setAgentLicenseNumber(String agentLicenseNumber) {
        this.agentLicenseNumber = agentLicenseNumber;
    }

    public String getAgencyBranch() {
        return agencyBranch;
    }

    public void setAgencyBranch(String agencyBranch) {
        this.agencyBranch = agencyBranch;
    }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }
}
