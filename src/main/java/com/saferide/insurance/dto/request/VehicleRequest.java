package com.saferide.insurance.dto.request;

import com.saferide.insurance.entity.vehicle.VehicleType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class VehicleRequest {

    private Long customerId;

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;

    @NotBlank(message = "Make is required")
    private String make;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Manufacture year is required")
    @Min(value = 1990, message = "Manufacture year must be 1990 or later")
    private Integer manufactureYear;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "Chassis number is required")
    private String chassisNumber;

    @NotNull(message = "Engine capacity (CC) is required")
    private Integer engineCapacityCc;

    @NotBlank(message = "Fuel type is required")
    private String fuelType;

    @NotNull(message = "Market value is required")
    private BigDecimal marketValue;

    @NotNull(message = "Current mileage is required")
    private Integer currentMileage;

    // Private car specific fields
    private Integer seatingCapacity = 5;
    private Boolean antiTheftDevice = true;
    private String bodyType = "SEDAN";

    // Commercial vehicle specific fields
    private BigDecimal tonnageCapacity;
    private String goodsPermitNumber;
    private String commercialUseType;

    // Motorcycle specific fields
    private Boolean helmetBox = false;
    private String engineStrokeType = "FOUR_STROKE";

    public VehicleRequest() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public Integer getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(Integer manufactureYear) {
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

    public Integer getEngineCapacityCc() {
        return engineCapacityCc;
    }

    public void setEngineCapacityCc(Integer engineCapacityCc) {
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

    public Integer getCurrentMileage() {
        return currentMileage;
    }

    public void setCurrentMileage(Integer currentMileage) {
        this.currentMileage = currentMileage;
    }

    public Integer getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(Integer seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public Boolean getAntiTheftDevice() {
        return antiTheftDevice;
    }

    public void setAntiTheftDevice(Boolean antiTheftDevice) {
        this.antiTheftDevice = antiTheftDevice;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
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

    public Boolean getHelmetBox() {
        return helmetBox;
    }

    public void setHelmetBox(Boolean helmetBox) {
        this.helmetBox = helmetBox;
    }

    public String getEngineStrokeType() {
        return engineStrokeType;
    }

    public void setEngineStrokeType(String engineStrokeType) {
        this.engineStrokeType = engineStrokeType;
    }
}
