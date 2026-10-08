package com.saferide.insurance.service.impl;

import com.saferide.insurance.dto.request.ClaimRequest;
import com.saferide.insurance.dto.request.ClaimStatusUpdateRequest;
import com.saferide.insurance.entity.claim.*;
import com.saferide.insurance.entity.policy.Policy;
import com.saferide.insurance.entity.policy.PolicyStatus;
import com.saferide.insurance.entity.user.Customer;
import com.saferide.insurance.entity.user.User;
import com.saferide.insurance.exception.BadRequestException;
import com.saferide.insurance.exception.ResourceNotFoundException;
import com.saferide.insurance.exception.UnauthorizedException;
import com.saferide.insurance.repository.ClaimRepository;
import com.saferide.insurance.repository.PolicyRepository;
import com.saferide.insurance.repository.UserRepository;
import com.saferide.insurance.service.ClaimService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saferide.insurance.pattern.singleton.InsuranceReferenceGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;



@Service
@Transactional
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;

    public ClaimServiceImpl(ClaimRepository claimRepository, PolicyRepository policyRepository, UserRepository userRepository) {
        this.claimRepository = claimRepository;
        this.policyRepository = policyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Claim submitClaim(ClaimRequest request) {
        User user = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + request.getCustomerId()));

        if (!(user instanceof Customer customer)) {
            throw new BadRequestException("Only registered Customers can submit claims");
        }

        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with ID: " + request.getPolicyId()));

        if (policy.getStatus() != PolicyStatus.ACTIVE && policy.getStatus() != PolicyStatus.RENEWED) {
            throw new BadRequestException("Claims can only be filed against an ACTIVE or RENEWED policy");
        }

        if (request.getIncidentDate().isAfter(LocalDate.now())) {
            throw new BadRequestException("Incident date cannot be in the future");
        }

        // SINGLETON PATTERN: Thread-safe claim reference sequence generation
        String claimRef = InsuranceReferenceGenerator.getInstance().generateClaimReference();

        Claim claim;
        if (request.getClaimType() == ClaimType.THEFT) {
            claim = new TheftClaim(
                    claimRef,
                    policy,
                    customer,
                    ClaimStatus.SUBMITTED,
                    request.getIncidentDate(),
                    request.getIncidentLocation(),
                    request.getIncidentDescription(),
                    request.getEstimatedDamageAmount(),
                    request.getPoliceStationReported() != null ? request.getPoliceStationReported() : "General Police Division",
                    request.getCrimeReferenceNumber() != null ? request.getCrimeReferenceNumber() : "CR-AUTO-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
                    request.getRecoveryStatus() != null ? request.getRecoveryStatus() : "NOT_RECOVERED",
                    request.getLastKnownLocation() != null ? request.getLastKnownLocation() : request.getIncidentLocation()
            );
        } else if (request.getClaimType() == ClaimType.WINDSCREEN) {
            claim = new WindscreenClaim(
                    claimRef,
                    policy,
                    customer,
                    ClaimStatus.SUBMITTED,
                    request.getIncidentDate(),
                    request.getIncidentLocation(),
                    request.getIncidentDescription(),
                    request.getEstimatedDamageAmount(),
                    request.getGlassType() != null ? request.getGlassType() : "FRONT_WINDSCREEN",
                    request.getRepairWorkshopName() != null ? request.getRepairWorkshopName() : "Authorized Glass Center",
                    request.getReplacement() == null || request.getReplacement()
            );
        } else {
            claim = new AccidentClaim(
                    claimRef,
                    policy,
                    customer,
                    ClaimStatus.SUBMITTED,
                    request.getIncidentDate(),
                    request.getIncidentLocation(),
                    request.getIncidentDescription(),
                    request.getEstimatedDamageAmount(),
                    request.getThirdPartyVehicleInvolved() != null && request.getThirdPartyVehicleInvolved(),
                    request.getPoliceReportNumber() != null ? request.getPoliceReportNumber() : "PR-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
                    request.getDriverNameAtIncident() != null ? request.getDriverNameAtIncident() : customer.getFullName(),
                    request.getCollisionType() != null ? request.getCollisionType() : "GENERAL_COLLISION"
            );
        }

        // Validate domain rules via polymorphism
        if (!claim.validateClaimEligibility()) {
            throw new BadRequestException("Claim validation failed: please provide all mandatory details for " + request.getClaimType());
        }

        return claimRepository.save(claim);
    }

    @Override
    public Claim getClaimById(Long id) {
        return claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with ID: " + id));
    }

    @Override
    public Claim getClaimByReference(String claimReference) {
        return claimRepository.findByClaimReference(claimReference)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with reference: " + claimReference));
    }

    @Override
    public List<Claim> getClaimsByCustomerId(Long customerId) {
        return claimRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    @Override
    public List<Claim> getAllClaims() {
        return claimRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Claim updateClaimStatusAndNotes(Long id, ClaimStatusUpdateRequest request) {
        Claim claim = getClaimById(id);

        if (request.getStatus() != null) {
            claim.setStatus(request.getStatus());
        }

        if (request.getApprovedPayoutAmount() != null) {
            BigDecimal maxAllowed = claim.determineMaxPayoutLimit();
            if (maxAllowed.compareTo(BigDecimal.ZERO) > 0 && request.getApprovedPayoutAmount().compareTo(maxAllowed) > 0) {
                throw new BadRequestException("Approved payout exceeds max policy limit of LKR " + maxAllowed);
            }
            claim.setApprovedPayoutAmount(request.getApprovedPayoutAmount());
        }

        if (request.getAssessorNotes() != null) {
            claim.setAssessorNotes(request.getAssessorNotes());
        }

        if (request.getRejectionReason() != null) {
            claim.setRejectionReason(request.getRejectionReason());
        }

        if (request.getStatus() == ClaimStatus.SETTLED || request.getStatus() == ClaimStatus.APPROVED) {
            claim.setSettlementDate(request.getSettlementDate() != null ? request.getSettlementDate() : LocalDate.now());
        }

        return claimRepository.save(claim);
    }

    @Override
    public void withdrawClaim(Long id, Long customerId) {
        Claim claim = getClaimById(id);

        if (customerId != null && !claim.getCustomer().getId().equals(customerId)) {
            throw new UnauthorizedException("You are not authorized to withdraw this claim");
        }

        if (!claim.canBeWithdrawn()) {
            throw new BadRequestException("Claims in status '" + claim.getStatus() + "' cannot be withdrawn");
        }

        claim.setStatus(ClaimStatus.WITHDRAWN);
        claimRepository.save(claim);
    }
}
