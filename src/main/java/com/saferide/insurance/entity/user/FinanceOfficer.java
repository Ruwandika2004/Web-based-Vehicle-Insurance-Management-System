package com.saferide.insurance.entity.user;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "finance_officers")
@PrimaryKeyJoinColumn(name = "id")
public class FinanceOfficer extends User {

    @Column(name = "employee_id", nullable = false, unique = true, length = 30)
    private String employeeId;

    @Column(nullable = false, length = 50)
    private String department;

    @Column(name = "transaction_limit", precision = 12, scale = 2)
    private BigDecimal transactionLimit = new BigDecimal("2000000.00");

    public FinanceOfficer() {
        super();
        setRole(UserRole.ROLE_FINANCE_OFFICER);
    }

    public FinanceOfficer(String username, String password, String email, String fullName, String phone,
                          String employeeId, String department, BigDecimal transactionLimit) {
        super(username, password, email, fullName, phone, UserRole.ROLE_FINANCE_OFFICER);
        this.employeeId = employeeId;
        this.department = department;
        this.transactionLimit = transactionLimit;
    }

    @Override
    public String getRoleDescription() {
        return "Finance & Payments Officer";
    }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public BigDecimal getTransactionLimit() { return transactionLimit; }
    public void setTransactionLimit(BigDecimal transactionLimit) { this.transactionLimit = transactionLimit; }
}
