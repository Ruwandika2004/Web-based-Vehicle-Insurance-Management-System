package com.saferide.insurance.entity.vehicle;

import com.saferide.insurance.entity.user.Customer;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "motorcycles")
@PrimaryKeyJoinColumn(name = "id")
public class Motorcycle extends Vehicle {

    @Column(name = "has_helmet_box")
    private boolean helmetBox = false;

    @Column(name = "engine_stroke_type", length = 20)
    private String engineStrokeType = "FOUR_STROKE";

    public Motorcycle() {
        super();
        setVehicleType(VehicleType.MOTORCYCLE);
    }

    public Motorcycle(Customer customer, String make, String model, int manufactureYear,
                      String registrationNumber, String chassisNumber, int engineCapacityCc,
                      String fuelType, BigDecimal marketValue, int currentMileage,
                      boolean helmetBox, String engineStrokeType) {
        super(customer, VehicleType.MOTORCYCLE, make, model, manufactureYear, registrationNumber,
                chassisNumber, engineCapacityCc, fuelType, marketValue, currentMileage);
        this.helmetBox = helmetBox;
        this.engineStrokeType = engineStrokeType;
    }

    @Override
    public double calculateBaseRiskFactor() {
        // Two wheelers have higher accident vulnerability
        return 1.45;
    }

    public boolean isHelmetBox() {
        return helmetBox;
    }

    public void setHelmetBox(boolean helmetBox) {
        this.helmetBox = helmetBox;
    }

    public String getEngineStrokeType() {
        return engineStrokeType;
    }

    public void setEngineStrokeType(String engineStrokeType) {
        this.engineStrokeType = engineStrokeType;
    }
}
