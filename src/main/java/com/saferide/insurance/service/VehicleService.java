package com.saferide.insurance.service;

import com.saferide.insurance.dto.request.VehicleRequest;
import com.saferide.insurance.entity.vehicle.Vehicle;

import java.util.List;

public interface VehicleService {
    Vehicle registerVehicle(VehicleRequest request);
    Vehicle getVehicleById(Long id);
    List<Vehicle> getVehiclesByCustomerId(Long customerId);
    List<Vehicle> getAllVehicles();
    Vehicle updateVehicle(Long id, VehicleRequest request);
    void deleteVehicle(Long id);
}
