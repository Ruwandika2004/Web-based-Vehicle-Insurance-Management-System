package com.saferide.insurance.repository;

import com.saferide.insurance.entity.policy.Policy;
import com.saferide.insurance.entity.policy.PolicyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {
    Optional<Policy> findByPolicyNumber(String policyNumber);
    List<Policy> findByCustomerId(Long customerId);
    List<Policy> findByVehicleId(Long vehicleId);
    List<Policy> findByStatus(PolicyStatus status);
    long countByStatus(PolicyStatus status);
    
    @Query("SELECT SUM(p.totalAnnualPremium) FROM Policy p WHERE p.status = 'ACTIVE' OR p.status = 'RENEWED'")
    BigDecimal sumActivePremiums();
}
