package com.saferide.insurance.entity.vehicle;

import com.saferide.insurance.entity.BaseEntity;
import com.saferide.insurance.entity.user.Customer;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Year;

/**
 * Abstract Base Class: Vehicle (Ravihara L.D.C. - IT25102267)
 * Demonstrates OOP Inheritance and Polymorphism for Vehicle Fleet management.
 */
@Entity
@Table(name = "vehicles")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Vehicle extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, length = 30)
    private VehicleType vehicleType;

    @Column(nullable = false, length = 50)
    private String make;

    @Column(nullable = false, length = 50)
    private String model;

    @Column(name = "manufacture_year", nullable = false)
    private int manufactureYear;

    @Column(name = "registration_number", nullable = false, unique = true, length = 30)
    private String registrationNumber;

    @Column(name = "chassis_number", nullable = false, unique = true, length = 50)
    private String chassisNumber;

    @Column(name = "engine_capacity_cc", nullable = false)
    private int engineCapacityCc;

    @Column(name = "fuel_type", nullable = false, length = 30)
    private String fuelType;

    @Column(name = "market_value", nullable = false, precision = 12, scale = 2)
    private BigDecimal marketValue;

    @Column(name = "current_mileage", nullable = false)
    private int currentMileage;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public Vehicle() {
    }

    public Vehicle(Customer customer, VehicleType vehicleType, String make, String model, int manufactureYear,
                   String registrationNumber, String chassisNumber, int engineCapacityCc, String fuelType,
                   BigDecimal marketValue, int currentMileage) {
        this.customer = customer;
        this.vehicleType = vehicleType;
        this.make = make;
        this.model = model;
        this.manufactureYear = manufactureYear;
        this.registrationNumber = registrationNumber;
        this.chassisNumber = chassisNumber;
        this.engineCapacityCc = engineCapacityCc;
        this.fuelType = fuelType;
        this.marketValue = marketValue;
        this.currentMileage = currentMileage;
        this.active = true;
    }

    // Polymorphic method to calculate risk factor based on vehicle characteristics
    public abstract double calculateBaseRiskFactor();

    public int getVehicleAge() {
        return Year.now().getValue() - manufactureYear;
    }

    // Getters and Setters
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(int manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public int getEngineCapacityCc() {
        return engineCapacityCc;
    }

    public void setEngineCapacityCc(int engineCapacityCc) {
        this.engineCapacityCc = engineCapacityCc;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(BigDecimal marketValue) {
        this.marketValue = marketValue;
    }

    public int getCurrentMileage() {
        return currentMileage;
    }

    public void setCurrentMileage(int currentMileage) {
        this.currentMileage = currentMileage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
