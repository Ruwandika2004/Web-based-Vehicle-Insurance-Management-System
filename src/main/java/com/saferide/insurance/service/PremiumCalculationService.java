package com.saferide.insurance.service;

import com.saferide.insurance.dto.request.QuoteCalculationRequest;
import com.saferide.insurance.dto.response.DecoratedPlanResponse;
import com.saferide.insurance.dto.response.QuoteBreakdownResponse;
import com.saferide.insurance.entity.policy.PolicyType;
import com.saferide.insurance.entity.quote.PremiumQuote;

import java.math.BigDecimal;
import java.util.List;

public interface PremiumCalculationService {
    QuoteBreakdownResponse calculateQuote(QuoteCalculationRequest request);
    PremiumQuote saveQuote(QuoteCalculationRequest request);
    PremiumQuote getQuoteByReference(String quoteReference);
    List<PremiumQuote> getQuotesByCustomerId(Long customerId);
    List<PremiumQuote> getAllQuotes();
    QuoteBreakdownResponse recalculateQuote(String quoteReference, QuoteCalculationRequest request);
    void deleteQuote(String quoteReference);
    DecoratedPlanResponse calculateWithDecorators(BigDecimal marketValue, PolicyType policyType,
                                                 boolean includeRoadside, boolean includeDisaster,
                                                 boolean includePassenger, boolean includeZeroDep);
}

