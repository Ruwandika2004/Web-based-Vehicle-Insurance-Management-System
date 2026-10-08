package com.saferide.insurance.controller;

import com.saferide.insurance.dto.request.PolicyRequest;
import com.saferide.insurance.dto.response.ApiResponse;
import com.saferide.insurance.entity.policy.Policy;
import com.saferide.insurance.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller: Insurance Policy Management (Gamage S.M. - IT25102293)
 * CRUD:
 * - Create: POST /api/policies
 * - Read: GET /api/policies/{id}, GET /api/policies/number/{policyNumber}, GET /api/policies/customer/{customerId}, GET /api/policies
 * - Update: PUT /api/policies/{id}/renew
 * - Delete: DELETE /api/policies/{id}
 */
@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = "*")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Policy>> issuePolicy(@Valid @RequestBody PolicyRequest request) {
        Policy policy = policyService.issuePolicy(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Insurance Policy issued successfully! Number: " + policy.getPolicyNumber(), policy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Policy>> getPolicyById(@PathVariable Long id) {
        Policy policy = policyService.getPolicyById(id);
        return ResponseEntity.ok(ApiResponse.success("Policy fetched successfully", policy));
    }

    @GetMapping("/number/{policyNumber}")
    public ResponseEntity<ApiResponse<Policy>> getPolicyByNumber(@PathVariable String policyNumber) {
        Policy policy = policyService.getPolicyByNumber(policyNumber);
        return ResponseEntity.ok(ApiResponse.success("Policy fetched successfully", policy));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<Policy>>> getPoliciesByCustomerId(@PathVariable Long customerId) {
        List<Policy> policies = policyService.getPoliciesByCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.success("Customer policies retrieved", policies));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Policy>>> getAllPolicies() {
        List<Policy> policies = policyService.getAllPolicies();
        return ResponseEntity.ok(ApiResponse.success("All policies retrieved", policies));
    }

    @PutMapping("/{id}/renew")
    public ResponseEntity<ApiResponse<Policy>> renewPolicy(@PathVariable Long id,
                                                           @RequestParam(defaultValue = "1") int additionalYears) {
        Policy policy = policyService.renewPolicy(id, additionalYears);
        return ResponseEntity.ok(ApiResponse.success("Policy successfully renewed for " + additionalYears + " year(s)", policy));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Policy>> cancelPolicy(@PathVariable Long id,
                                                            @RequestParam(defaultValue = "Customer cancellation request") String reason) {
        Policy policy = policyService.cancelPolicy(id, reason);
        return ResponseEntity.ok(ApiResponse.success("Policy has been cancelled", policy));
    }
}
