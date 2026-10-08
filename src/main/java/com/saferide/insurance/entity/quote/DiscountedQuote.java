package com.saferide.insurance.entity.quote;

import com.saferide.insurance.entity.policy.PolicyType;
import com.saferide.insurance.entity.user.Customer;
import com.saferide.insurance.entity.vehicle.Vehicle;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Table(name = "discounted_quotes")
@PrimaryKeyJoinColumn(name = "id")
public class DiscountedQuote extends PremiumQuote {

    @Column(name = "promo_code", length = 30)
    private String promoCode;

    @Column(name = "special_discount_rate", precision = 5, scale = 2)
    private BigDecimal specialDiscountRate = BigDecimal.ZERO;

    @Column(name = "loyalty_years")
    private int loyaltyYears = 0;

    public DiscountedQuote() {
        super();
        setQuoteType(QuoteType.DISCOUNTED);
    }

    public DiscountedQuote(String quoteReference, Customer customer, Vehicle vehicle,
                           PolicyType coverageType, BigDecimal calculatedSumInsured, BigDecimal baseRate,
                           BigDecimal riskLoadingPercent, BigDecimal ncbDiscountPercent, LocalDate validUntil,
                           String promoCode, BigDecimal specialDiscountRate, int loyaltyYears) {
        super(quoteReference, customer, vehicle, QuoteType.DISCOUNTED, coverageType, calculatedSumInsured,
                baseRate, riskLoadingPercent, ncbDiscountPercent, validUntil);
        this.promoCode = promoCode;
        this.specialDiscountRate = specialDiscountRate != null ? specialDiscountRate : BigDecimal.ZERO;
        this.loyaltyYears = loyaltyYears;
        setFinalPremium(calculateFinalPremium());
    }

    @Override
    public BigDecimal calculateFinalPremium() {
        BigDecimal raw = computeRawPremium();
        // Additional promo / loyalty deduction
        BigDecimal additionalRate = specialDiscountRate != null ? specialDiscountRate : BigDecimal.ZERO;
        if (loyaltyYears > 0) {
            additionalRate = additionalRate.add(new BigDecimal(String.valueOf(Math.min(loyaltyYears * 1.5, 10.0))));
        }
        BigDecimal extraDeduction = raw.multiply(additionalRate.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
        return raw.subtract(extraDeduction).setScale(2, RoundingMode.HALF_UP);
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public BigDecimal getSpecialDiscountRate() {
        return specialDiscountRate;
    }

    public void setSpecialDiscountRate(BigDecimal specialDiscountRate) {
        this.specialDiscountRate = specialDiscountRate;
    }

    public int getLoyaltyYears() {
        return loyaltyYears;
    }

    public void setLoyaltyYears(int loyaltyYears) {
        this.loyaltyYears = loyaltyYears;
    }
}
