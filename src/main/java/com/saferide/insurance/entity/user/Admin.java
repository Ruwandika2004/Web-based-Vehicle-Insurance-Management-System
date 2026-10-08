package com.saferide.insurance.entity.user;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "id")
public class Admin extends User {

    @Column(name = "admin_level", nullable = false, length = 30)
    private String adminLevel = "SUPER_ADMIN";

    @Column(nullable = false, length = 50)
    private String department = "Executive IT Operations";

    public Admin() {
        super();
        setRole(UserRole.ROLE_ADMIN);
    }

    public Admin(String username, String password, String email, String fullName, String phone, String adminLevel, String department) {
        super(username, password, email, fullName, phone, UserRole.ROLE_ADMIN);
        this.adminLevel = adminLevel;
        this.department = department;
    }

    @Override
    public String getRoleDescription() {
        return "Executive System Administrator";
    }

    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
