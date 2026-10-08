package com.saferide.insurance.service.impl;

import com.saferide.insurance.dto.request.PolicyRequest;
import com.saferide.insurance.entity.policy.*;
import com.saferide.insurance.entity.user.Customer;
import com.saferide.insurance.entity.user.User;
import com.saferide.insurance.entity.vehicle.Vehicle;
import com.saferide.insurance.exception.BadRequestException;
import com.saferide.insurance.exception.ResourceNotFoundException;
import com.saferide.insurance.repository.PolicyRepository;
import com.saferide.insurance.repository.UserRepository;
import com.saferide.insurance.repository.VehicleRepository;
import com.saferide.insurance.service.PolicyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saferide.insurance.pattern.singleton.InsuranceReferenceGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


/**
 * Service Implementation: Insurance Policy Management (Gamage S.M. - IT25102293)
 */
@Service
@Transactional
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public PolicyServiceImpl(PolicyRepository policyRepository, UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.policyRepository = policyRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Policy issuePolicy(PolicyRequest request) {
        User user = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + request.getCustomerId()));

        if (!(user instanceof Customer customer)) {
            throw new BadRequestException("Only registered Customers can be issued an insurance policy");
        }

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + request.getVehicleId()));

        // SINGLETON PATTERN: Globally synchronized, unique policy sequence generation
        String policyNumber = InsuranceReferenceGenerator.getInstance().generatePolicyNumber(request.getPolicyType());

        LocalDate startDate = request.getStartDate() != null ? request.getStartDate() : LocalDate.now();
        LocalDate endDate = request.getEndDate() != null ? request.getEndDate() : startDate.plusYears(1);

        BigDecimal base = request.getBasePremium() != null ? request.getBasePremium() : request.getSumInsured().multiply(new BigDecimal("0.015"));
        BigDecimal total = request.getTotalAnnualPremium() != null ? request.getTotalAnnualPremium() : base.multiply(new BigDecimal("1.10"));
        BigDecimal deductible = request.getDeductibleAmount() != null ? request.getDeductibleAmount() : new BigDecimal("5000.00");

        Policy policy;
        if (request.getPolicyType() == PolicyType.COMPREHENSIVE) {
            policy = new ComprehensivePolicy(
                    policyNumber,
                    customer,
                    vehicle,
                    PolicyStatus.ACTIVE,
                    startDate,
                    endDate,
                    request.getSumInsured(),
                    base,
                    total,
                    deductible,
                    request.getIncludesNaturalDisaster() == null || request.getIncludesNaturalDisaster(),
                    request.getIncludesTheft() == null || request.getIncludesTheft(),
                    request.getIncludesRiotStrike() == null || request.getIncludesRiotStrike(),
                    request.getRoadsideAssistanceLevel() != null ? request.getRoadsideAssistanceLevel() : "PLATINUM"
            );
        } else {
            policy = new ThirdPartyPolicy(
                    policyNumber,
                    customer,
                    vehicle,
                    PolicyStatus.ACTIVE,
                    startDate,
                    endDate,
                    request.getSumInsured(),
                    base,
                    total,
                    deductible,
                    request.getThirdPartyPropertyLimit() != null ? request.getThirdPartyPropertyLimit() : new BigDecimal("1000000.00"),
                    request.getLegalLiabilityCoverage() == null || request.getLegalLiabilityCoverage()
            );
        }

        return policyRepository.save(policy);
    }

    @Override
    public Policy getPolicyById(Long id) {
        return policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with ID: " + id));
    }

    @Override
    public Policy getPolicyByNumber(String policyNumber) {
        return policyRepository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with number: " + policyNumber));
    }

    @Override
    public List<Policy> getPoliciesByCustomerId(Long customerId) {
        return policyRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    @Override
    public Policy renewPolicy(Long id, int additionalYears) {
        Policy policy = getPolicyById(id);
        if (policy.getStatus() == PolicyStatus.CANCELLED) {
            throw new BadRequestException("Cancelled policies cannot be renewed. Please issue a new policy.");
        }

        int yearsToAdd = additionalYears > 0 ? additionalYears : 1;
        LocalDate currentEnd = policy.getEndDate();
        LocalDate newEnd = currentEnd.isAfter(LocalDate.now()) ? currentEnd.plusYears(yearsToAdd) : LocalDate.now().plusYears(yearsToAdd);

        policy.setEndDate(newEnd);
        policy.setStatus(PolicyStatus.RENEWED);

        return policyRepository.save(policy);
    }

    @Override
    public Policy cancelPolicy(Long id, String reason) {
        Policy policy = getPolicyById(id);
        policy.setStatus(PolicyStatus.CANCELLED);
        return policyRepository.save(policy);
    }
}
