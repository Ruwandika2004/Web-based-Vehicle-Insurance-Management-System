package com.saferide.insurance.repository;

import com.saferide.insurance.entity.claim.Claim;
import com.saferide.insurance.entity.claim.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    Optional<Claim> findByClaimReference(String claimReference);
    List<Claim> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<Claim> findByPolicyId(Long policyId);
    List<Claim> findByStatus(ClaimStatus status);
    List<Claim> findAllByOrderByCreatedAtDesc();
    long countByStatus(ClaimStatus status);

    @Query("SELECT SUM(c.approvedPayoutAmount) FROM Claim c WHERE c.status = 'APPROVED' OR c.status = 'SETTLED'")
    BigDecimal sumSettledClaims();
}
