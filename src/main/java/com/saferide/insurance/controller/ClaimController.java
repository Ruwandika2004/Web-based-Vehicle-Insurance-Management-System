package com.saferide.insurance.controller;

import com.saferide.insurance.dto.request.ClaimRequest;
import com.saferide.insurance.dto.request.ClaimStatusUpdateRequest;
import com.saferide.insurance.dto.response.ApiResponse;
import com.saferide.insurance.entity.claim.Claim;
import com.saferide.insurance.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@CrossOrigin(origins = "*")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Claim>> submitClaim(@Valid @RequestBody ClaimRequest request) {
        Claim claim = claimService.submitClaim(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Insurance claim submitted successfully! Reference: " + claim.getClaimReference(), claim));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Claim>> getClaimById(@PathVariable Long id) {
        Claim claim = claimService.getClaimById(id);
        return ResponseEntity.ok(ApiResponse.success("Claim details retrieved", claim));
    }

    @GetMapping("/reference/{claimReference}")
    public ResponseEntity<ApiResponse<Claim>> getClaimByReference(@PathVariable String claimReference) {
        Claim claim = claimService.getClaimByReference(claimReference);
        return ResponseEntity.ok(ApiResponse.success("Claim details retrieved", claim));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<Claim>>> getClaimsByCustomerId(@PathVariable Long customerId) {
        List<Claim> claims = claimService.getClaimsByCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.success("Customer claims history retrieved", claims));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Claim>>> getAllClaims() {
        List<Claim> claims = claimService.getAllClaims();
        return ResponseEntity.ok(ApiResponse.success("All claims retrieved", claims));
    }

    @PutMapping("/{id}/adjudicate")
    public ResponseEntity<ApiResponse<Claim>> adjudicateClaim(@PathVariable Long id,
                                                              @Valid @RequestBody ClaimStatusUpdateRequest request) {
        Claim claim = claimService.updateClaimStatusAndNotes(id, request);
        return ResponseEntity.ok(ApiResponse.success("Claim status, adjudication notes, and payout updated successfully", claim));
    }

    @DeleteMapping("/{id}/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdrawClaim(@PathVariable Long id,
                                                           @RequestParam(required = false) Long customerId) {
        claimService.withdrawClaim(id, customerId);
        return ResponseEntity.ok(ApiResponse.success("Claim has been withdrawn successfully", null));
    }
}
