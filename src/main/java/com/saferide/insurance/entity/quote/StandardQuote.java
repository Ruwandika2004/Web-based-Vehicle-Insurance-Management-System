package com.saferide.insurance.entity.quote;

import com.saferide.insurance.entity.policy.PolicyType;
import com.saferide.insurance.entity.user.Customer;
import com.saferide.insurance.entity.vehicle.Vehicle;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "standard_quotes")
@PrimaryKeyJoinColumn(name = "id")
public class StandardQuote extends PremiumQuote {

    @Column(name = "standard_processing_fee", precision = 10, scale = 2)
    private BigDecimal standardProcessingFee = new BigDecimal("1500.00");

    public StandardQuote() {
        super();
        setQuoteType(QuoteType.STANDARD);
    }

    public StandardQuote(String quoteReference, Customer customer, Vehicle vehicle,
                         PolicyType coverageType, BigDecimal calculatedSumInsured, BigDecimal baseRate,
                         BigDecimal riskLoadingPercent, BigDecimal ncbDiscountPercent, LocalDate validUntil,
                         BigDecimal standardProcessingFee) {
        super(quoteReference, customer, vehicle, QuoteType.STANDARD, coverageType, calculatedSumInsured,
                baseRate, riskLoadingPercent, ncbDiscountPercent, validUntil);
        this.standardProcessingFee = standardProcessingFee != null ? standardProcessingFee : new BigDecimal("1500.00");
        setFinalPremium(calculateFinalPremium());
    }

    @Override
    public BigDecimal calculateFinalPremium() {
        BigDecimal raw = computeRawPremium();
        BigDecimal fee = standardProcessingFee != null ? standardProcessingFee : new BigDecimal("1500.00");
        return raw.add(fee);
    }

    public BigDecimal getStandardProcessingFee() {
        return standardProcessingFee;
    }

    public void setStandardProcessingFee(BigDecimal standardProcessingFee) {
        this.standardProcessingFee = standardProcessingFee;
    }
}
