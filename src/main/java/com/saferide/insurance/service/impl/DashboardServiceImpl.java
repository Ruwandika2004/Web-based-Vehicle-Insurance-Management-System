package com.saferide.insurance.service.impl;

import com.saferide.insurance.dto.response.DashboardSummaryResponse;
import com.saferide.insurance.entity.claim.ClaimStatus;
import com.saferide.insurance.entity.inspection.InspectionStatus;
import com.saferide.insurance.entity.policy.PolicyStatus;
import com.saferide.insurance.entity.user.UserRole;
import com.saferide.insurance.repository.*;
import com.saferide.insurance.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final PolicyRepository policyRepository;
    private final VehicleRepository vehicleRepository;
    private final ClaimRepository claimRepository;
    private final InspectionRepository inspectionRepository;
    private final UserRepository userRepository;

    public DashboardServiceImpl(PolicyRepository policyRepository,
                                VehicleRepository vehicleRepository,
                                ClaimRepository claimRepository,
                                InspectionRepository inspectionRepository,
                                UserRepository userRepository) {
        this.policyRepository = policyRepository;
        this.vehicleRepository = vehicleRepository;
        this.claimRepository = claimRepository;
        this.inspectionRepository = inspectionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DashboardSummaryResponse getDashboardSummary() {
        DashboardSummaryResponse summary = new DashboardSummaryResponse();

        summary.setTotalPolicies(policyRepository.count());
        summary.setActivePolicies(policyRepository.countByStatus(PolicyStatus.ACTIVE) + policyRepository.countByStatus(PolicyStatus.RENEWED));
        summary.setTotalVehicles(vehicleRepository.countByActiveTrue());

        summary.setTotalClaims(claimRepository.count());
        summary.setPendingClaims(claimRepository.countByStatus(ClaimStatus.SUBMITTED) + claimRepository.countByStatus(ClaimStatus.UNDER_REVIEW));
        summary.setApprovedClaims(claimRepository.countByStatus(ClaimStatus.APPROVED) + claimRepository.countByStatus(ClaimStatus.SETTLED));

        summary.setTotalInspections(inspectionRepository.count());
        summary.setPendingInspections(inspectionRepository.countByStatus(InspectionStatus.PENDING) + inspectionRepository.countByStatus(InspectionStatus.IN_PROGRESS));

        BigDecimal totalPremiums = policyRepository.sumActivePremiums();
        summary.setTotalPremiumsCollected(totalPremiums != null ? totalPremiums : BigDecimal.ZERO);

        BigDecimal totalSettled = claimRepository.sumSettledClaims();
        summary.setTotalClaimsSettled(totalSettled != null ? totalSettled : BigDecimal.ZERO);

        summary.setTotalCustomers(userRepository.findByRole(UserRole.ROLE_CUSTOMER).size());

        return summary;
    }
}
