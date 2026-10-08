package com.saferide.insurance.service;

import com.saferide.insurance.dto.request.ClaimRequest;
import com.saferide.insurance.dto.request.ClaimStatusUpdateRequest;
import com.saferide.insurance.entity.claim.Claim;

import java.util.List;

public interface ClaimService {
    Claim submitClaim(ClaimRequest request);
    Claim getClaimById(Long id);
    Claim getClaimByReference(String claimReference);
    List<Claim> getClaimsByCustomerId(Long customerId);
    List<Claim> getAllClaims();
    Claim updateClaimStatusAndNotes(Long id, ClaimStatusUpdateRequest request);
    void withdrawClaim(Long id, Long customerId);
}
