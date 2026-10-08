package com.saferide.insurance.entity.user;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "claims_officers")
@PrimaryKeyJoinColumn(name = "id")
public class ClaimsOfficer extends User {

    @Column(name = "employee_id", nullable = false, unique = true, length = 30)
    private String employeeId;

    @Column(nullable = false, length = 50)
    private String department;

    @Column(name = "adjudication_limit", precision = 12, scale = 2)
    private BigDecimal adjudicationLimit = new BigDecimal("500000.00");

    public ClaimsOfficer() {
        super();
        setRole(UserRole.ROLE_CLAIMS_OFFICER);
    }

    public ClaimsOfficer(String username, String password, String email, String fullName, String phone, String employeeId, String department, BigDecimal adjudicationLimit) {
        super(username, password, email, fullName, phone, UserRole.ROLE_CLAIMS_OFFICER);
        this.employeeId = employeeId;
        this.department = department;
        this.adjudicationLimit = adjudicationLimit;
    }

    @Override
    public String getRoleDescription() {
        return "Claims Adjudication and Assessment Officer";
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public BigDecimal getAdjudicationLimit() {
        return adjudicationLimit;
    }

    public void setAdjudicationLimit(BigDecimal adjudicationLimit) {
        this.adjudicationLimit = adjudicationLimit;
    }
}
