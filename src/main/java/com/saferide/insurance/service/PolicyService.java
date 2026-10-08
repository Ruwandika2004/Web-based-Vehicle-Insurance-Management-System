package com.saferide.insurance.service;

import com.saferide.insurance.dto.request.PolicyRequest;
import com.saferide.insurance.entity.policy.Policy;

import java.util.List;

public interface PolicyService {
    Policy issuePolicy(PolicyRequest request);
    Policy getPolicyById(Long id);
    Policy getPolicyByNumber(String policyNumber);
    List<Policy> getPoliciesByCustomerId(Long customerId);
    List<Policy> getAllPolicies();
    Policy renewPolicy(Long id, int additionalYears);
    Policy cancelPolicy(Long id, String reason);
}
