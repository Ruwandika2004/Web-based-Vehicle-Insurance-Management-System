package com.saferide.insurance.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class DecoratedPlanResponse {

    private String generatedReference;
    private String planName;
    private BigDecimal basePremium;
    private BigDecimal totalDecoratedPremium;
    private List<String> appliedAddons;
    private List<String> coverageFeatures;
    private String designPatternNote;

    public DecoratedPlanResponse() {
    }

    public DecoratedPlanResponse(String generatedReference, String planName, BigDecimal basePremium,
                                 BigDecimal totalDecoratedPremium, List<String> appliedAddons,
                                 List<String> coverageFeatures, String designPatternNote) {
        this.generatedReference = generatedReference;
        this.planName = planName;
        this.basePremium = basePremium;
        this.totalDecoratedPremium = totalDecoratedPremium;
        this.appliedAddons = appliedAddons;
        this.coverageFeatures = coverageFeatures;
        this.designPatternNote = designPatternNote;
    }

    public String getGeneratedReference() {
        return generatedReference;
    }

    public void setGeneratedReference(String generatedReference) {
        this.generatedReference = generatedReference;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public BigDecimal getBasePremium() {
        return basePremium;
    }

    public void setBasePremium(BigDecimal basePremium) {
        this.basePremium = basePremium;
    }

    public BigDecimal getTotalDecoratedPremium() {
        return totalDecoratedPremium;
    }

    public void setTotalDecoratedPremium(BigDecimal totalDecoratedPremium) {
        this.totalDecoratedPremium = totalDecoratedPremium;
    }

    public List<String> getAppliedAddons() {
        return appliedAddons;
    }

    public void setAppliedAddons(List<String> appliedAddons) {
        this.appliedAddons = appliedAddons;
    }

    public List<String> getCoverageFeatures() {
        return coverageFeatures;
    }

    public void setCoverageFeatures(List<String> coverageFeatures) {
        this.coverageFeatures = coverageFeatures;
    }

    public String getDesignPatternNote() {
        return designPatternNote;
    }

    public void setDesignPatternNote(String designPatternNote) {
        this.designPatternNote = designPatternNote;
    }
}
