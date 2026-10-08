package com.saferide.insurance.repository;

import com.saferide.insurance.entity.inspection.Inspection;
import com.saferide.insurance.entity.inspection.InspectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Long> {
    Optional<Inspection> findByInspectionReference(String inspectionReference);
    List<Inspection> findByVehicleId(Long vehicleId);
    Optional<Inspection> findByClaimId(Long claimId);
    List<Inspection> findByInspectorId(Long inspectorId);
    List<Inspection> findByStatus(InspectionStatus status);
    List<Inspection> findAllByOrderByCreatedAtDesc();
    long countByStatus(InspectionStatus status);
}
