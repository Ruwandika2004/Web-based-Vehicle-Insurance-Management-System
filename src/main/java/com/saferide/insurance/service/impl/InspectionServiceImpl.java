package com.saferide.insurance.service.impl;

import com.saferide.insurance.dto.request.InspectionRequest;
import com.saferide.insurance.entity.claim.Claim;
import com.saferide.insurance.entity.claim.ClaimStatus;
import com.saferide.insurance.entity.inspection.*;
import com.saferide.insurance.entity.user.User;
import com.saferide.insurance.entity.vehicle.Vehicle;
import com.saferide.insurance.exception.BadRequestException;
import com.saferide.insurance.exception.ResourceNotFoundException;
import com.saferide.insurance.repository.ClaimRepository;
import com.saferide.insurance.repository.InspectionRepository;
import com.saferide.insurance.repository.UserRepository;
import com.saferide.insurance.repository.VehicleRepository;
import com.saferide.insurance.service.InspectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saferide.insurance.pattern.singleton.InsuranceReferenceGenerator;

import java.time.LocalDate;
import java.util.List;



@Service
@Transactional
public class InspectionServiceImpl implements InspectionService {

    private final InspectionRepository inspectionRepository;
    private final VehicleRepository vehicleRepository;
    private final ClaimRepository claimRepository;
    private final UserRepository userRepository;

    public InspectionServiceImpl(InspectionRepository inspectionRepository,
                                 VehicleRepository vehicleRepository,
                                 ClaimRepository claimRepository,
                                 UserRepository userRepository) {
        this.inspectionRepository = inspectionRepository;
        this.vehicleRepository = vehicleRepository;
        this.claimRepository = claimRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Inspection createInspection(InspectionRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + request.getVehicleId()));

        Claim claim = null;
        if (request.getClaimId() != null) {
            claim = claimRepository.findById(request.getClaimId()).orElse(null);
        }

        User inspector = null;
        if (request.getInspectorId() != null) {
            inspector = userRepository.findById(request.getInspectorId()).orElse(null);
        }

        // SINGLETON PATTERN: Globally synchronized inspection reference generation
        String inspRef = InsuranceReferenceGenerator.getInstance().generateInspectionReference();

        Inspection inspection;
        if (request.getInspectionType() == InspectionType.PRE_POLICY) {
            inspection = new PrePolicyInspection(
                    inspRef,
                    vehicle,
                    inspector,
                    request.getStatus() != null ? request.getStatus() : InspectionStatus.PENDING,
                    request.getInspectionDate() != null ? request.getInspectionDate() : LocalDate.now(),
                    request.getInspectionLocation(),
                    request.getPhysicalConditionNotes() != null ? request.getPhysicalConditionNotes() : "Standard pre-issuance inspection scheduled",
                    request.getPassed() == null || request.getPassed(),
                    request.getOdometerVerified() == null || request.getOdometerVerified(),
                    request.getBodyConditionRating() != null ? request.getBodyConditionRating() : 8,
                    request.getMechanicalScore() != null ? request.getMechanicalScore() : 8
            );
        } else {
            inspection = new DamageAssessment(
                    inspRef,
                    vehicle,
                    claim,
                    inspector,
                    request.getStatus() != null ? request.getStatus() : InspectionStatus.PENDING,
                    request.getInspectionDate() != null ? request.getInspectionDate() : LocalDate.now(),
                    request.getInspectionLocation(),
                    request.getPhysicalConditionNotes() != null ? request.getPhysicalConditionNotes() : "Damage assessment ongoing",
                    request.getDamageSeverity() != null ? request.getDamageSeverity() : "MODERATE",
                    request.getEstimatedRepairCost() != null ? request.getEstimatedRepairCost() : java.math.BigDecimal.ZERO,
                    request.getPassed() != null && request.getPassed(),
                    request.getStructuralDamageFound() != null && request.getStructuralDamageFound(),
                    request.getPartsToReplace(),
                    request.getLaborCostEstimate() != null ? request.getLaborCostEstimate() : java.math.BigDecimal.ZERO,
                    request.getRecommendedAction() != null ? request.getRecommendedAction() : "REPAIR_AUTHORIZED"
            );

            // Update associated claim status to INSPECTION_SCHEDULED if applicable
            if (claim != null && claim.getStatus() == ClaimStatus.SUBMITTED) {
                claim.setStatus(ClaimStatus.INSPECTION_SCHEDULED);
                claimRepository.save(claim);
            }
        }

        return inspectionRepository.save(inspection);
    }

    @Override
    public Inspection getInspectionById(Long id) {
        return inspectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspection not found with ID: " + id));
    }

    @Override
    public Inspection getInspectionByReference(String inspectionReference) {
        return inspectionRepository.findByInspectionReference(inspectionReference)
                .orElseThrow(() -> new ResourceNotFoundException("Inspection not found with reference: " + inspectionReference));
    }

    @Override
    public List<Inspection> getInspectionsByVehicleId(Long vehicleId) {
        return inspectionRepository.findByVehicleId(vehicleId);
    }

    @Override
    public List<Inspection> getAllInspections() {
        return inspectionRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Inspection updateInspection(Long id, InspectionRequest request) {
        Inspection inspection = getInspectionById(id);

        if (!inspection.isModifiable()) {
            throw new BadRequestException("Cancelled inspections cannot be modified");
        }

        if (request.getStatus() != null) inspection.setStatus(request.getStatus());
        if (request.getInspectionDate() != null) inspection.setInspectionDate(request.getInspectionDate());
        if (request.getInspectionLocation() != null) inspection.setInspectionLocation(request.getInspectionLocation());
        if (request.getPhysicalConditionNotes() != null) inspection.setPhysicalConditionNotes(request.getPhysicalConditionNotes());
        if (request.getDamageSeverity() != null) inspection.setDamageSeverity(request.getDamageSeverity());
        if (request.getEstimatedRepairCost() != null) inspection.setEstimatedRepairCost(request.getEstimatedRepairCost());
        if (request.getPassed() != null) inspection.setPassed(request.getPassed());

        if (inspection instanceof PrePolicyInspection pre) {
            if (request.getOdometerVerified() != null) pre.setOdometerVerified(request.getOdometerVerified());
            if (request.getBodyConditionRating() != null) pre.setBodyConditionRating(request.getBodyConditionRating());
            if (request.getMechanicalScore() != null) pre.setMechanicalScore(request.getMechanicalScore());
        } else if (inspection instanceof DamageAssessment dmg) {
            if (request.getStructuralDamageFound() != null) dmg.setStructuralDamageFound(request.getStructuralDamageFound());
            if (request.getPartsToReplace() != null) dmg.setPartsToReplace(request.getPartsToReplace());
            if (request.getLaborCostEstimate() != null) dmg.setLaborCostEstimate(request.getLaborCostEstimate());
            if (request.getRecommendedAction() != null) dmg.setRecommendedAction(request.getRecommendedAction());
        }

        return inspectionRepository.save(inspection);
    }

    @Override
    public void cancelInspection(Long id) {
        Inspection inspection = getInspectionById(id);
        inspection.setStatus(InspectionStatus.CANCELLED);
        inspectionRepository.save(inspection);
    }
}
