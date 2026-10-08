package com.saferide.insurance.repository;

import com.saferide.insurance.entity.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByCustomerIdAndActiveTrue(Long customerId);
    List<Vehicle> findByCustomerId(Long customerId);
    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);
    Optional<Vehicle> findByChassisNumber(String chassisNumber);
    boolean existsByRegistrationNumber(String registrationNumber);
    boolean existsByChassisNumber(String chassisNumber);
    long countByActiveTrue();
}
