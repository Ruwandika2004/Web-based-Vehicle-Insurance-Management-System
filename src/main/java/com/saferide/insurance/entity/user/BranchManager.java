package com.saferide.insurance.entity.user;

import jakarta.persistence.*;

@Entity
@Table(name = "branch_managers")
@PrimaryKeyJoinColumn(name = "id")
public class BranchManager extends User {

    @Column(name = "employee_id", nullable = false, unique = true, length = 30)
    private String employeeId;

    @Column(name = "branch_name", nullable = false, length = 80)
    private String branchName;

    @Column(name = "branch_code", nullable = false, length = 20)
    private String branchCode;

    public BranchManager() {
        super();
        setRole(UserRole.ROLE_BRANCH_MANAGER);
    }

    public BranchManager(String username, String password, String email, String fullName, String phone,
                         String employeeId, String branchName, String branchCode) {
        super(username, password, email, fullName, phone, UserRole.ROLE_BRANCH_MANAGER);
        this.employeeId = employeeId;
        this.branchName = branchName;
        this.branchCode = branchCode;
    }

    @Override
    public String getRoleDescription() {
        return "Branch Operations Manager";
    }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getBranchCode() { return branchCode; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }
}
