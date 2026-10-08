package com.saferide.insurance.entity.vehicle;

import com.saferide.insurance.entity.user.Customer;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "private_cars")
@PrimaryKeyJoinColumn(name = "id")
public class PrivateCar extends Vehicle {

    @Column(name = "seating_capacity", nullable = false)
    private int seatingCapacity = 5;

    @Column(name = "has_anti_theft_device")
    private boolean antiTheftDevice = true;

    @Column(name = "body_type", length = 30)
    private String bodyType = "SEDAN";

    public PrivateCar() {
        super();
        setVehicleType(VehicleType.PRIVATE_CAR);
    }

    public PrivateCar(Customer customer, String make, String model, int manufactureYear,
                      String registrationNumber, String chassisNumber, int engineCapacityCc, String fuelType,
                      BigDecimal marketValue, int currentMileage, int seatingCapacity,
                      boolean antiTheftDevice, String bodyType) {
        super(customer, VehicleType.PRIVATE_CAR, make, model, manufactureYear, registrationNumber, chassisNumber,
                engineCapacityCc, fuelType, marketValue, currentMileage);
        this.seatingCapacity = seatingCapacity;
        this.antiTheftDevice = antiTheftDevice;
        this.bodyType = bodyType;
    }

    @Override
    public double calculateBaseRiskFactor() {
        // Lower risk for cars with anti-theft devices, higher for older cars
        double risk = 1.0;
        if (antiTheftDevice) {
            risk -= 0.10; // 10% safety discount
        }
        if (getVehicleAge() > 8) {
            risk += 0.15; // 15% loading for older cars
        }
        return risk;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public boolean isAntiTheftDevice() {
        return antiTheftDevice;
    }

    public void setAntiTheftDevice(boolean antiTheftDevice) {
        this.antiTheftDevice = antiTheftDevice;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }
}
