package com.saferide.insurance.pattern.singleton;

import com.saferide.insurance.entity.policy.PolicyType;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;


public class InsuranceReferenceGenerator {

    // 1. Volatile static instance variable
    private static volatile InsuranceReferenceGenerator instance;

    // Sequential atomic counters for thread-safety without lock contention
    private final AtomicLong policySequence;
    private final AtomicLong claimSequence;
    private final AtomicLong quoteSequence;
    private final AtomicLong inspectionSequence;

    // 2. Private constructor prevents instantiation from other classes
    private InsuranceReferenceGenerator() {
        // Prevent instantiation via reflection attacks
        if (instance != null) {
            throw new IllegalStateException("InsuranceReferenceGenerator instance already exists! Use getInstance().");
        }
        // Initialize sequence counters starting from baseline values
        this.policySequence = new AtomicLong(1000);
        this.claimSequence = new AtomicLong(500);
        this.quoteSequence = new AtomicLong(2000);
        this.inspectionSequence = new AtomicLong(300);
    }

    // 3. Double-Checked Locking for thread-safe lazy initialization
    public static InsuranceReferenceGenerator getInstance() {
        if (instance == null) { // First check (no lock)
            synchronized (InsuranceReferenceGenerator.class) {
                if (instance == null) { // Second check (with lock)
                    instance = new InsuranceReferenceGenerator();
                }
            }
        }
        return instance;
    }

    /**
     * Generates a unique, sequential Policy Number.
     * Example: POL-COMP-2026-001001 or POL-TP-2026-001002
     */
    public String generatePolicyNumber(PolicyType policyType) {
        int year = LocalDate.now().getYear();
        String prefix = (policyType == PolicyType.COMPREHENSIVE) ? "POL-COMP" : "POL-TP";
        long seq = policySequence.incrementAndGet();
        return String.format("%s-%d-%06d", prefix, year, seq);
    }

    /**
     * Generates a unique, sequential Claim Reference ID.
     * Example: CLM-2026-000501
     */
    public String generateClaimReference() {
        int year = LocalDate.now().getYear();
        long seq = claimSequence.incrementAndGet();
        return String.format("CLM-%d-%06d", year, seq);
    }

    /**
     * Generates a unique, sequential Quotation Reference ID.
     * Example: QUO-2026-002001
     */
    public String generateQuoteReference() {
        int year = LocalDate.now().getYear();
        long seq = quoteSequence.incrementAndGet();
        return String.format("QUO-%d-%06d", year, seq);
    }

    /**
     * Generates a unique, sequential Inspection Reference ID.
     * Example: INSP-2026-000301
     */
    public String generateInspectionReference() {
        int year = LocalDate.now().getYear();
        long seq = inspectionSequence.incrementAndGet();
        return String.format("INSP-%d-%06d", year, seq);
    }

    /**
     * Diagnostic helper for verifying singleton instance identity in tests or vivas.
     */
    public int getInstanceHashCode() {
        return System.identityHashCode(this);
    }
}
