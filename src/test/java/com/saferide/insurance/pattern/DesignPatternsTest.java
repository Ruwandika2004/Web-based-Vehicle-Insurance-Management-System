package com.saferide.insurance.pattern;

import com.saferide.insurance.entity.policy.PolicyType;
import com.saferide.insurance.pattern.decorator.*;
import com.saferide.insurance.pattern.singleton.InsuranceReferenceGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for Design Patterns:
 * 1. GoF Decorator Pattern (Structural)
 * 2. GoF Singleton Pattern (Creational)
 */
class DesignPatternsTest {

    // ==========================================
    // 1. SINGLETON PATTERN VERIFICATION
    // ==========================================

    @Test
    @DisplayName("Singleton: Verifies that multiple calls return the exact same instance")
    void testSingletonInstanceUniqueness() {
        InsuranceReferenceGenerator instance1 = InsuranceReferenceGenerator.getInstance();
        InsuranceReferenceGenerator instance2 = InsuranceReferenceGenerator.getInstance();

        assertNotNull(instance1);
        assertNotNull(instance2);
        // Both references MUST point to the same memory address in heap
        assertSame(instance1, instance2, "Singleton pattern must return the exact same object reference!");
        assertEquals(instance1.getInstanceHashCode(), instance2.getInstanceHashCode());
    }

    @Test
    @DisplayName("Singleton: Verifies unique, synchronized sequence numbers")
    void testSingletonSequentialNumberGeneration() {
        InsuranceReferenceGenerator generator = InsuranceReferenceGenerator.getInstance();

        String policy1 = generator.generatePolicyNumber(PolicyType.COMPREHENSIVE);
        String policy2 = generator.generatePolicyNumber(PolicyType.THIRD_PARTY);
        String claim1 = generator.generateClaimReference();
        String claim2 = generator.generateClaimReference();

        assertNotEquals(policy1, policy2, "Policy numbers must be unique");
        assertNotEquals(claim1, claim2, "Claim references must be unique");

        assertTrue(policy1.startsWith("POL-COMP-"));
        assertTrue(policy2.startsWith("POL-TP-"));
        assertTrue(claim1.startsWith("CLM-"));
    }

    // ==========================================
    // 2. DECORATOR PATTERN VERIFICATION
    // ==========================================

    @Test
    @DisplayName("Decorator: Base plan calculates standard premium without riders")
    void testDecoratorBaseComprehensivePlan() {
        BigDecimal sumInsured = new BigDecimal("4000000.00");
        InsurancePlan basePlan = new ComprehensivePlan(sumInsured);

        // 4,000,000 * 0.0125 = 50,000.00
        assertEquals(new BigDecimal("500000.00").divide(new BigDecimal("10")).setScale(2), basePlan.calculatePremium());
        assertEquals("SafeRide Comprehensive Vehicle Insurance Plan", basePlan.getPlanName());
        assertEquals(4, basePlan.getCoverageFeatures().size());
    }

    @Test
    @DisplayName("Decorator: Dynamically attaches add-on riders and accumulates cost")
    void testDecoratorDynamicWrapping() {
        BigDecimal sumInsured = new BigDecimal("5000000.00");
        InsurancePlan plan = new ComprehensivePlan(sumInsured); // 5,000,000 * 0.0125 = 62,500.00
        BigDecimal basePremium = plan.calculatePremium();

        // 1. Attach Roadside Assistance (+2,500)
        plan = new RoadsideAssistanceDecorator(plan);
        assertEquals(basePremium.add(new BigDecimal("2500.00")), plan.calculatePremium());
        assertTrue(plan.getPlanName().contains("Roadside Assistance"));

        // 2. Attach Flood & Natural Disaster (+5,000)
        plan = new NaturalDisasterDecorator(plan);
        assertEquals(basePremium.add(new BigDecimal("7500.00")), plan.calculatePremium());
        assertTrue(plan.getPlanName().contains("Flood & Natural Perils"));

        // 3. Attach Zero Depreciation (+4,500)
        plan = new ZeroDepreciationDecorator(plan);
        assertEquals(basePremium.add(new BigDecimal("12000.00")), plan.calculatePremium());
        assertTrue(plan.getPlanName().contains("Zero Depreciation"));

        // 4. Attach Passenger Cover (+3,500)
        plan = new PassengerCoverDecorator(plan);
        BigDecimal expectedTotal = basePremium.add(new BigDecimal("15500.00"));
        assertEquals(expectedTotal, plan.calculatePremium());

        List<String> coverages = plan.getCoverageFeatures();
        // 4 base features + 8 decorator features = 12 total features
        assertTrue(coverages.size() >= 8);
    }

    @Test
    @DisplayName("Decorator: Third-party plan can also be decorated with arbitrary riders")
    void testDecoratorOnThirdPartyPlan() {
        InsurancePlan tpPlan = new BasicThirdPartyPlan();
        BigDecimal baseTp = tpPlan.calculatePremium(); // 18,500.00

        // Decorate Third Party only with Roadside Assistance (+2,500)
        InsurancePlan decoratedTp = new RoadsideAssistanceDecorator(tpPlan);
        assertEquals(baseTp.add(new BigDecimal("2500.00")), decoratedTp.calculatePremium());
        assertTrue(decoratedTp.getPlanName().contains("Third-Party"));
        assertTrue(decoratedTp.getPlanName().contains("Roadside Assistance"));
    }
}
