package com.saferide.insurance.controller;

import com.saferide.insurance.dto.request.VehicleRequest;
import com.saferide.insurance.dto.response.ApiResponse;
import com.saferide.insurance.entity.vehicle.Vehicle;
import com.saferide.insurance.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Vehicle>> registerVehicle(@Valid @RequestBody VehicleRequest request) {
        Vehicle vehicle = vehicleService.registerVehicle(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Vehicle registered successfully", vehicle));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Vehicle>> getVehicleById(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(ApiResponse.success("Vehicle fetched successfully", vehicle));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<Vehicle>>> getVehiclesByCustomerId(@PathVariable Long customerId) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.success("Customer vehicles retrieved", vehicles));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Vehicle>>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(ApiResponse.success("All registered vehicles retrieved", vehicles));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Vehicle>> updateVehicle(@PathVariable Long id,
                                                              @Valid @RequestBody VehicleRequest request) {
        Vehicle vehicle = vehicleService.updateVehicle(id, request);
        return ResponseEntity.ok(ApiResponse.success("Vehicle details updated successfully", vehicle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok(ApiResponse.success("Vehicle removed from account successfully", null));
    }
}
