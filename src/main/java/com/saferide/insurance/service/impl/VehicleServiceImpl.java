package com.saferide.insurance.service.impl;

import com.saferide.insurance.dto.request.VehicleRequest;
import com.saferide.insurance.entity.user.Customer;
import com.saferide.insurance.entity.user.User;
import com.saferide.insurance.entity.vehicle.*;
import com.saferide.insurance.exception.BadRequestException;
import com.saferide.insurance.exception.ResourceNotFoundException;
import com.saferide.insurance.repository.UserRepository;
import com.saferide.insurance.repository.VehicleRepository;
import com.saferide.insurance.service.VehicleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service Implementation: Vehicle Management (Ravihara L.D.C. - IT25102267)
 */
@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Vehicle registerVehicle(VehicleRequest request) {
        User user = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + request.getCustomerId()));

        if (!(user instanceof Customer customer)) {
            throw new BadRequestException("User must be a Customer to register a vehicle");
        }

        if (vehicleRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new BadRequestException("Registration number '" + request.getRegistrationNumber() + "' is already in the system.");
        }

        if (vehicleRepository.existsByChassisNumber(request.getChassisNumber())) {
            throw new BadRequestException("Chassis number '" + request.getChassisNumber() + "' is already registered.");
        }

        Vehicle vehicle;
        if (request.getVehicleType() == VehicleType.COMMERCIAL_VEHICLE) {
            vehicle = new CommercialVehicle(
                    customer,
                    request.getMake(),
                    request.getModel(),
                    request.getManufactureYear(),
                    request.getRegistrationNumber().toUpperCase(),
                    request.getChassisNumber().toUpperCase(),
                    request.getEngineCapacityCc(),
                    request.getFuelType(),
                    request.getMarketValue(),
                    request.getCurrentMileage(),
                    request.getTonnageCapacity() != null ? request.getTonnageCapacity() : new BigDecimal("3.5"),
                    request.getGoodsPermitNumber() != null ? request.getGoodsPermitNumber() : "N/A",
                    request.getCommercialUseType() != null ? request.getCommercialUseType() : "GENERAL_COMMERCIAL"
            );
        } else if (request.getVehicleType() == VehicleType.MOTORCYCLE) {
            vehicle = new Motorcycle(
                    customer,
                    request.getMake(),
                    request.getModel(),
                    request.getManufactureYear(),
                    request.getRegistrationNumber().toUpperCase(),
                    request.getChassisNumber().toUpperCase(),
                    request.getEngineCapacityCc(),
                    request.getFuelType(),
                    request.getMarketValue(),
                    request.getCurrentMileage(),
                    request.getHelmetBox() != null && request.getHelmetBox(),
                    request.getEngineStrokeType() != null ? request.getEngineStrokeType() : "FOUR_STROKE"
            );
        } else {
            vehicle = new PrivateCar(
                    customer,
                    request.getMake(),
                    request.getModel(),
                    request.getManufactureYear(),
                    request.getRegistrationNumber().toUpperCase(),
                    request.getChassisNumber().toUpperCase(),
                    request.getEngineCapacityCc(),
                    request.getFuelType(),
                    request.getMarketValue(),
                    request.getCurrentMileage(),
                    request.getSeatingCapacity() != null ? request.getSeatingCapacity() : 5,
                    request.getAntiTheftDevice() == null || request.getAntiTheftDevice(),
                    request.getBodyType() != null ? request.getBodyType() : "SEDAN"
            );
        }

        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));
    }

    @Override
    public List<Vehicle> getVehiclesByCustomerId(Long customerId) {
        return vehicleRepository.findByCustomerIdAndActiveTrue(customerId);
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle updateVehicle(Long id, VehicleRequest request) {
        Vehicle vehicle = getVehicleById(id);

        if (request.getMake() != null) vehicle.setMake(request.getMake());
        if (request.getModel() != null) vehicle.setModel(request.getModel());
        if (request.getManufactureYear() != null) vehicle.setManufactureYear(request.getManufactureYear());
        if (request.getEngineCapacityCc() != null) vehicle.setEngineCapacityCc(request.getEngineCapacityCc());
        if (request.getFuelType() != null) vehicle.setFuelType(request.getFuelType());
        if (request.getMarketValue() != null) vehicle.setMarketValue(request.getMarketValue());
        if (request.getCurrentMileage() != null) vehicle.setCurrentMileage(request.getCurrentMileage());

        if (vehicle instanceof PrivateCar privateCar) {
            if (request.getSeatingCapacity() != null) privateCar.setSeatingCapacity(request.getSeatingCapacity());
            if (request.getAntiTheftDevice() != null) privateCar.setAntiTheftDevice(request.getAntiTheftDevice());
            if (request.getBodyType() != null) privateCar.setBodyType(request.getBodyType());
        } else if (vehicle instanceof CommercialVehicle comm) {
            if (request.getTonnageCapacity() != null) comm.setTonnageCapacity(request.getTonnageCapacity());
            if (request.getGoodsPermitNumber() != null) comm.setGoodsPermitNumber(request.getGoodsPermitNumber());
            if (request.getCommercialUseType() != null) comm.setCommercialUseType(request.getCommercialUseType());
        } else if (vehicle instanceof Motorcycle moto) {
            if (request.getHelmetBox() != null) moto.setHelmetBox(request.getHelmetBox());
            if (request.getEngineStrokeType() != null) moto.setEngineStrokeType(request.getEngineStrokeType());
        }

        return vehicleRepository.save(vehicle);
    }

    @Override
    public void deleteVehicle(Long id) {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setActive(false);
        vehicleRepository.save(vehicle);
    }
}
