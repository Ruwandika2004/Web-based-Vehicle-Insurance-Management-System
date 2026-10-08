package com.saferide.insurance.entity.user;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "id")
public class Customer extends User {

    @Column(name = "preferred_communication", length = 20)
    private String preferredCommunication = "EMAIL";

    @Column(name = "loyalty_points")
    private int loyaltyPoints = 0;

    public Customer() {
        super();
        setRole(UserRole.ROLE_CUSTOMER);
    }

    public Customer(String username, String password, String email, String fullName, String phone) {
        super(username, password, email, fullName, phone, UserRole.ROLE_CUSTOMER);
        this.preferredCommunication = "EMAIL";
        this.loyaltyPoints = 0;
    }

    @Override
    public String getRoleDescription() {
        return "SafeRide Policyholder / Customer";
    }

    public String getPreferredCommunication() {
        return preferredCommunication;
    }

    public void setPreferredCommunication(String preferredCommunication) {
        this.preferredCommunication = preferredCommunication;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
}
