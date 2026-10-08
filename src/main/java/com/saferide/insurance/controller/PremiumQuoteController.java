package com.saferide.insurance.controller;

import com.saferide.insurance.dto.request.QuoteCalculationRequest;
import com.saferide.insurance.dto.response.ApiResponse;
import com.saferide.insurance.dto.response.DecoratedPlanResponse;
import com.saferide.insurance.dto.response.QuoteBreakdownResponse;
import com.saferide.insurance.entity.policy.PolicyType;
import com.saferide.insurance.entity.quote.PremiumQuote;
import com.saferide.insurance.service.PremiumCalculationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller: Premium Calculation & Actuarial Quotation (Subasinghe S.A.K.G. - IT25102301)
 * CRUD:
 * - Create: POST /api/quotes/calculate (instant calc), POST /api/quotes/save (persist quote)
 * - Read: GET /api/quotes/{quoteReference}, GET /api/quotes/customer/{customerId}, GET /api/quotes
 * - Update: PUT /api/quotes/{quoteReference}/recalculate
 * - Delete: DELETE /api/quotes/{quoteReference}
 */
@RestController
@RequestMapping("/api/quotes")
@CrossOrigin(origins = "*")
public class PremiumQuoteController {

    private final PremiumCalculationService calculationService;

    public PremiumQuoteController(PremiumCalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<ApiResponse<QuoteBreakdownResponse>> calculateInstantQuote(@Valid @RequestBody QuoteCalculationRequest request) {
        QuoteBreakdownResponse breakdown = calculationService.calculateQuote(request);
        return ResponseEntity.ok(ApiResponse.success("Premium calculated successfully", breakdown));
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<PremiumQuote>> saveQuote(@Valid @RequestBody QuoteCalculationRequest request) {
        PremiumQuote quote = calculationService.saveQuote(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Quote saved successfully with Reference: " + quote.getQuoteReference(), quote));
    }

    @GetMapping("/{quoteReference}")
    public ResponseEntity<ApiResponse<PremiumQuote>> getQuoteByReference(@PathVariable String quoteReference) {
        PremiumQuote quote = calculationService.getQuoteByReference(quoteReference);
        return ResponseEntity.ok(ApiResponse.success("Quote retrieved successfully", quote));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<PremiumQuote>>> getQuotesByCustomerId(@PathVariable Long customerId) {
        List<PremiumQuote> quotes = calculationService.getQuotesByCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.success("Customer quotes retrieved", quotes));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PremiumQuote>>> getAllQuotes() {
        List<PremiumQuote> quotes = calculationService.getAllQuotes();
        return ResponseEntity.ok(ApiResponse.success("All quotes retrieved", quotes));
    }

    @PutMapping("/{quoteReference}/recalculate")
    public ResponseEntity<ApiResponse<QuoteBreakdownResponse>> recalculateQuote(@PathVariable String quoteReference,
                                                                                @Valid @RequestBody QuoteCalculationRequest request) {
        QuoteBreakdownResponse freshBreakdown = calculationService.recalculateQuote(quoteReference, request);
        return ResponseEntity.ok(ApiResponse.success("Quote recalculation updated successfully", freshBreakdown));
    }

    @DeleteMapping("/{quoteReference}")
    public ResponseEntity<ApiResponse<Void>> deleteDraftQuote(@PathVariable String quoteReference) {
        calculationService.deleteQuote(quoteReference);
        return ResponseEntity.ok(ApiResponse.success("Draft quote deleted successfully", null));
    }

    /**
     * DESIGN PATTERNS LIVE DEMO ENDPOINT:
     * Demonstrates both GoF Decorator and GoF Singleton patterns in real-time.
     * Can be tested via Browser / Postman / Swagger / Curl:
     * GET /api/quotes/patterns-demo?marketValue=5000000&policyType=COMPREHENSIVE&roadside=true&naturalDisaster=true&passengerCover=true&zeroDepreciation=true
     */
    @GetMapping("/patterns-demo")
    public ResponseEntity<ApiResponse<DecoratedPlanResponse>> demonstrateDesignPatterns(
            @RequestParam(defaultValue = "5000000") BigDecimal marketValue,
            @RequestParam(defaultValue = "COMPREHENSIVE") PolicyType policyType,
            @RequestParam(defaultValue = "true") boolean roadside,
            @RequestParam(defaultValue = "true") boolean naturalDisaster,
            @RequestParam(defaultValue = "true") boolean passengerCover,
            @RequestParam(defaultValue = "true") boolean zeroDepreciation) {
        DecoratedPlanResponse response = calculationService.calculateWithDecorators(
                marketValue, policyType, roadside, naturalDisaster, passengerCover, zeroDepreciation);
        return ResponseEntity.ok(ApiResponse.success("Design patterns executed successfully (GoF Decorator + GoF Singleton)", response));
    }
}
