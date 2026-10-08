package com.saferide.insurance.service.impl;

import com.saferide.insurance.dto.request.QuoteCalculationRequest;
import com.saferide.insurance.dto.response.DecoratedPlanResponse;
import com.saferide.insurance.dto.response.QuoteBreakdownResponse;
import com.saferide.insurance.entity.policy.PolicyType;
import com.saferide.insurance.entity.quote.*;
import com.saferide.insurance.entity.user.Customer;
import com.saferide.insurance.entity.user.User;
import com.saferide.insurance.entity.vehicle.Vehicle;
import com.saferide.insurance.entity.vehicle.VehicleType;
import com.saferide.insurance.exception.BadRequestException;
import com.saferide.insurance.exception.ResourceNotFoundException;
import com.saferide.insurance.pattern.decorator.*;
import com.saferide.insurance.pattern.singleton.InsuranceReferenceGenerator;
import com.saferide.insurance.repository.PremiumQuoteRepository;
import com.saferide.insurance.repository.UserRepository;
import com.saferide.insurance.repository.VehicleRepository;
import com.saferide.insurance.service.PremiumCalculationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Service Implementation: Premium Calculation & Actuarial Quotation (Subasinghe S.A.K.G. - IT25102301)
 */
@Service
@Transactional
public class PremiumCalculationServiceImpl implements PremiumCalculationService {

    private final PremiumQuoteRepository quoteRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public PremiumCalculationServiceImpl(PremiumQuoteRepository quoteRepository,
                                         UserRepository userRepository,
                                         VehicleRepository vehicleRepository) {
        this.quoteRepository = quoteRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public QuoteBreakdownResponse calculateQuote(QuoteCalculationRequest request) {
        BigDecimal sumInsured = request.getMarketValue();
        if (sumInsured == null || sumInsured.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Valid market value is required for calculation");
        }

        // Determine base rate based on coverage and vehicle type
        BigDecimal baseRate;
        if (request.getCoverageType() == PolicyType.COMPREHENSIVE) {
            if (request.getVehicleType() == VehicleType.COMMERCIAL_VEHICLE) {
                baseRate = new BigDecimal("0.0180");
            } else if (request.getVehicleType() == VehicleType.MOTORCYCLE) {
                baseRate = new BigDecimal("0.0220");
            } else {
                baseRate = new BigDecimal("0.0125");
            }
        } else {
            // Third party flat percentage
            baseRate = new BigDecimal("0.0085");
        }

        // Calculate risk loading (e.g. vehicle age)
        BigDecimal riskLoadingPercent = BigDecimal.ZERO;
        int age = request.getVehicleAge() != null ? request.getVehicleAge() : 0;
        if (age > 10) {
            riskLoadingPercent = new BigDecimal("15.00");
        } else if (age > 5) {
            riskLoadingPercent = new BigDecimal("8.00");
        }

        // Base premium
        BigDecimal basePremium = sumInsured.multiply(baseRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal riskLoadingAmount = basePremium.multiply(riskLoadingPercent.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal subTotal = basePremium.add(riskLoadingAmount);

        // NCB discount
        BigDecimal ncbPercent = request.getNcbDiscountPercent() != null ? request.getNcbDiscountPercent() : BigDecimal.ZERO;
        BigDecimal ncbDiscountAmount = subTotal.multiply(ncbPercent.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal afterNcb = subTotal.subtract(ncbDiscountAmount);

        // Special promo or loyalty discount
        BigDecimal promoDiscountAmount = BigDecimal.ZERO;
        BigDecimal processingFee = (request.getQuoteType() == QuoteType.DISCOUNTED) ? BigDecimal.ZERO : new BigDecimal("1500.00");

        if (request.getQuoteType() == QuoteType.DISCOUNTED) {
            BigDecimal discountRate = new BigDecimal("5.00"); // 5% promotional discount
            if (request.getLoyaltyYears() != null && request.getLoyaltyYears() > 0) {
                discountRate = discountRate.add(new BigDecimal(String.valueOf(Math.min(request.getLoyaltyYears() * 1.5, 10.0))));
            }
            promoDiscountAmount = afterNcb.multiply(discountRate.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal finalAnnualPremium = afterNcb.subtract(promoDiscountAmount).add(processingFee).setScale(2, RoundingMode.HALF_UP);
        BigDecimal monthlyInstallment = finalAnnualPremium.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);

        QuoteBreakdownResponse response = new QuoteBreakdownResponse();
        // SINGLETON PATTERN: Utilize globally synchronized unique reference generator
        response.setQuoteReference(InsuranceReferenceGenerator.getInstance().generateQuoteReference());
        response.setSumInsured(sumInsured);
        response.setBaseRate(baseRate);
        response.setBasePremium(basePremium);
        response.setRiskLoadingAmount(riskLoadingAmount);
        response.setRiskLoadingPercent(riskLoadingPercent);
        response.setNcbDiscountAmount(ncbDiscountAmount);
        response.setNcbDiscountPercent(ncbPercent);
        response.setSpecialPromoDiscountAmount(promoDiscountAmount);
        response.setProcessingFee(processingFee);
        response.setFinalAnnualPremium(finalAnnualPremium);
        response.setMonthlyInstallment(monthlyInstallment);

        return response;
    }

    @Override
    public PremiumQuote saveQuote(QuoteCalculationRequest request) {
        if (request.getCustomerId() == null) {
            throw new BadRequestException("Customer ID is required to save quote");
        }

        User user = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + request.getCustomerId()));

        if (!(user instanceof Customer customer)) {
            throw new BadRequestException("Only customers can hold saved quotes");
        }

        Vehicle vehicle = null;
        if (request.getVehicleId() != null) {
            vehicle = vehicleRepository.findById(request.getVehicleId()).orElse(null);
        }

        QuoteBreakdownResponse calculated = calculateQuote(request);
        // SINGLETON PATTERN: Generate synchronized, persistent quote reference
        String quoteRef = InsuranceReferenceGenerator.getInstance().generateQuoteReference();

        PremiumQuote quote;
        if (request.getQuoteType() == QuoteType.DISCOUNTED) {
            quote = new DiscountedQuote(
                    quoteRef,
                    customer,
                    vehicle,
                    request.getCoverageType(),
                    request.getMarketValue(),
                    calculated.getBaseRate(),
                    calculated.getRiskLoadingPercent(),
                    calculated.getNcbDiscountPercent(),
                    LocalDate.now().plusDays(30),
                    request.getPromoCode() != null ? request.getPromoCode() : "SPECIAL-PROMO",
                    new BigDecimal("5.00"),
                    request.getLoyaltyYears() != null ? request.getLoyaltyYears() : 0
            );
        } else {
            quote = new StandardQuote(
                    quoteRef,
                    customer,
                    vehicle,
                    request.getCoverageType(),
                    request.getMarketValue(),
                    calculated.getBaseRate(),
                    calculated.getRiskLoadingPercent(),
                    calculated.getNcbDiscountPercent(),
                    LocalDate.now().plusDays(30),
                    calculated.getProcessingFee()
            );
        }

        quote.setFinalPremium(calculated.getFinalAnnualPremium());
        return quoteRepository.save(quote);
    }

    @Override
    public PremiumQuote getQuoteByReference(String quoteReference) {
        return quoteRepository.findByQuoteReference(quoteReference)
                .orElseThrow(() -> new ResourceNotFoundException("Quote not found with reference: " + quoteReference));
    }

    @Override
    public List<PremiumQuote> getQuotesByCustomerId(Long customerId) {
        return quoteRepository.findByCustomerId(customerId);
    }

    @Override
    public List<PremiumQuote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    @Override
    public QuoteBreakdownResponse recalculateQuote(String quoteReference, QuoteCalculationRequest request) {
        PremiumQuote quote = getQuoteByReference(quoteReference);
        if (quote.isAppliedToPolicy()) {
            throw new BadRequestException("Quote has already been converted into an active policy and cannot be modified.");
        }

        QuoteBreakdownResponse freshCalc = calculateQuote(request);
        quote.setCalculatedSumInsured(request.getMarketValue());
        quote.setBaseRate(freshCalc.getBaseRate());
        quote.setRiskLoadingPercent(freshCalc.getRiskLoadingPercent());
        quote.setNcbDiscountPercent(freshCalc.getNcbDiscountPercent());
        quote.setFinalPremium(freshCalc.getFinalAnnualPremium());

        quoteRepository.save(quote);
        freshCalc.setQuoteReference(quoteReference);
        return freshCalc;
    }

    @Override
    public void deleteQuote(String quoteReference) {
        PremiumQuote quote = getQuoteByReference(quoteReference);
        if (quote.isAppliedToPolicy()) {
            throw new BadRequestException("Cannot delete a quote that was applied to an issued policy");
        }
        quoteRepository.delete(quote);
    }

    /**
     * DECORATOR & SINGLETON PATTERN INTEGRATION:
     * Dynamically wraps the base insurance plan with selected add-on decorators at runtime,
     * calculating cumulative premium and collecting active features without subclass explosion.
     */
    @Override
    public DecoratedPlanResponse calculateWithDecorators(BigDecimal marketValue, PolicyType policyType,
                                                         boolean includeRoadside, boolean includeDisaster,
                                                         boolean includePassenger, boolean includeZeroDep) {
        BigDecimal sumInsured = marketValue != null ? marketValue : new BigDecimal("4500000.00");
        List<String> appliedAddons = new ArrayList<>();

        // 1. Instantiate the Base Component
        InsurancePlan plan;
        if (policyType == PolicyType.COMPREHENSIVE) {
            plan = new ComprehensivePlan(sumInsured);
        } else {
            plan = new BasicThirdPartyPlan();
        }

        BigDecimal basePremium = plan.calculatePremium();

        // 2. Dynamically attach Decorators at runtime based on customer choice
        if (includeRoadside) {
            plan = new RoadsideAssistanceDecorator(plan);
            appliedAddons.add("24/7 Roadside Assistance & Towing (+ LKR 2,500.00)");
        }
        if (includeDisaster) {
            plan = new NaturalDisasterDecorator(plan);
            appliedAddons.add("Flood & Natural Calamities Rider (+ LKR 5,000.00)");
        }
        if (includePassenger) {
            plan = new PassengerCoverDecorator(plan);
            appliedAddons.add("Passenger Personal Accident & Medical (+ LKR 3,500.00)");
        }
        if (includeZeroDep) {
            plan = new ZeroDepreciationDecorator(plan);
            appliedAddons.add("Zero Depreciation Bumper-to-Bumper (+ LKR 4,500.00)");
        }

        // 3. SINGLETON PATTERN: Generate a unique, thread-safe quotation reference
        String quoteRef = InsuranceReferenceGenerator.getInstance().generateQuoteReference();

        String patternExplanation = "Calculated using GoF Decorator Pattern (wrapping " + 
                (policyType == PolicyType.COMPREHENSIVE ? "ComprehensivePlan" : "BasicThirdPartyPlan") + 
                " with " + appliedAddons.size() + " dynamic rider decorators) and GoF Singleton Pattern (" + 
                "InsuranceReferenceGenerator DCL instance).";

        return new DecoratedPlanResponse(
                quoteRef,
                plan.getPlanName(),
                basePremium,
                plan.calculatePremium(),
                appliedAddons,
                plan.getCoverageFeatures(),
                patternExplanation
        );
    }
}
