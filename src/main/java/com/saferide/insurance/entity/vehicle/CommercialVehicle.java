package com.saferide.insurance.entity.vehicle;

import com.saferide.insurance.entity.user.Customer;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "commercial_vehicles")
@PrimaryKeyJoinColumn(name = "id")
public class CommercialVehicle extends Vehicle {

    @Column(name = "tonnage_capacity", nullable = false, precision = 8, scale = 2)
    private BigDecimal tonnageCapacity;

    @Column(name = "goods_permit_number", nullable = false, length = 50)
    private String goodsPermitNumber;

    @Column(name = "commercial_use_type", nullable = false, length = 50)
    private String commercialUseType;

    public CommercialVehicle() {
        super();
        setVehicleType(VehicleType.COMMERCIAL_VEHICLE);
    }

    public CommercialVehicle(Customer customer, String make, String model, int manufactureYear,
                             String registrationNumber, String chassisNumber, int engineCapacityCc,
                             String fuelType, BigDecimal marketValue, int currentMileage,
                             BigDecimal tonnageCapacity, String goodsPermitNumber, String commercialUseType) {
        super(customer, VehicleType.COMMERCIAL_VEHICLE, make, model, manufactureYear, registrationNumber,
                chassisNumber, engineCapacityCc, fuelType, marketValue, currentMileage);
        this.tonnageCapacity = tonnageCapacity;
        this.goodsPermitNumber = goodsPermitNumber;
        this.commercialUseType = commercialUseType;
    }

    @Override
    public double calculateBaseRiskFactor() {
        // Commercial vehicles have higher operational risk based on tonnage
        double risk = 1.35;
        if (tonnageCapacity != null && tonnageCapacity.doubleValue() > 5.0) {
            risk += 0.20; // Heavy duty loading
        }
        return risk;
    }

    public BigDecimal getTonnageCapacity() {
        return tonnageCapacity;
    }

    public void setTonnageCapacity(BigDecimal tonnageCapacity) {
        this.tonnageCapacity = tonnageCapacity;
    }

    public String getGoodsPermitNumber() {
        return goodsPermitNumber;
    }

    public void setGoodsPermitNumber(String goodsPermitNumber) {
        this.goodsPermitNumber = goodsPermitNumber;
    }

    public String getCommercialUseType() {
        return commercialUseType;
    }

    public void setCommercialUseType(String commercialUseType) {
        this.commercialUseType = commercialUseType;
    }
}
