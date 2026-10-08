package com.saferide.insurance.dto.response;

import java.math.BigDecimal;

public class QuoteBreakdownResponse {

    private String quoteReference;
    private BigDecimal sumInsured;
    private BigDecimal baseRate;
    private BigDecimal basePremium;
    private BigDecimal riskLoadingAmount;
    private BigDecimal riskLoadingPercent;
    private BigDecimal ncbDiscountAmount;
    private BigDecimal ncbDiscountPercent;
    private BigDecimal specialPromoDiscountAmount;
    private BigDecimal processingFee;
    private BigDecimal finalAnnualPremium;
    private BigDecimal monthlyInstallment;

    public QuoteBreakdownResponse() {
    }

    public String getQuoteReference() {
        return quoteReference;
    }

    public void setQuoteReference(String quoteReference) {
        this.quoteReference = quoteReference;
    }

    public BigDecimal getSumInsured() {
        return sumInsured;
    }

    public void setSumInsured(BigDecimal sumInsured) {
        this.sumInsured = sumInsured;
    }

    public BigDecimal getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(BigDecimal baseRate) {
        this.baseRate = baseRate;
    }

    public BigDecimal getBasePremium() {
        return basePremium;
    }

    public void setBasePremium(BigDecimal basePremium) {
        this.basePremium = basePremium;
    }

    public BigDecimal getRiskLoadingAmount() {
        return riskLoadingAmount;
    }

    public void setRiskLoadingAmount(BigDecimal riskLoadingAmount) {
        this.riskLoadingAmount = riskLoadingAmount;
    }

    public BigDecimal getRiskLoadingPercent() {
        return riskLoadingPercent;
    }

    public void setRiskLoadingPercent(BigDecimal riskLoadingPercent) {
        this.riskLoadingPercent = riskLoadingPercent;
    }

    public BigDecimal getNcbDiscountAmount() {
        return ncbDiscountAmount;
    }

    public void setNcbDiscountAmount(BigDecimal ncbDiscountAmount) {
        this.ncbDiscountAmount = ncbDiscountAmount;
    }

    public BigDecimal getNcbDiscountPercent() {
        return ncbDiscountPercent;
    }

    public void setNcbDiscountPercent(BigDecimal ncbDiscountPercent) {
        this.ncbDiscountPercent = ncbDiscountPercent;
    }

    public BigDecimal getSpecialPromoDiscountAmount() {
        return specialPromoDiscountAmount;
    }

    public void setSpecialPromoDiscountAmount(BigDecimal specialPromoDiscountAmount) {
        this.specialPromoDiscountAmount = specialPromoDiscountAmount;
    }

    public BigDecimal getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(BigDecimal processingFee) {
        this.processingFee = processingFee;
    }

    public BigDecimal getFinalAnnualPremium() {
        return finalAnnualPremium;
    }

    public void setFinalAnnualPremium(BigDecimal finalAnnualPremium) {
        this.finalAnnualPremium = finalAnnualPremium;
    }

    public BigDecimal getMonthlyInstallment() {
        return monthlyInstallment;
    }

    public void setMonthlyInstallment(BigDecimal monthlyInstallment) {
        this.monthlyInstallment = monthlyInstallment;
    }
}
